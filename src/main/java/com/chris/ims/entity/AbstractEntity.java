package com.chris.ims.entity;

import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.entity.annotations.Res;
import com.chris.ims.entity.exception.CxException;
import com.chris.ims.entity.utils.Resource;
import io.swagger.v3.core.util.ReflectionUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@SoftDelete(columnName = "deleted")
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

  public static final String GROUP_ALL = "all";
  public static final int F_ID = Resource.F_ID;
  public static final int F_CREATED_ON = Resource.F_CREATED_ON;
  public static final int F_UPDATED_ON = Resource.F_UPDATED_ON;

  public static void scripts(DbTable table) {
    table.findField(F_ID).setEnabled(false);
    table.findField(F_CREATED_ON).setEnabled(false);
    table.findField(F_UPDATED_ON).setEnabled(false);
  }

  protected AbstractEntity() {
    table = DbTable.byCode(getClass());
  }

  @Transient
  private final DbTable table;

  @Transient
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private AbstractEntity original;

  @Transient
  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  private Mode mode = Mode.NEW;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  @Res("id")
  private Long id;

  @CreationTimestamp
  @Column(name = "created_on", nullable = false, updatable = false)
  @Res("createdOn")
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on", nullable = false)
  @Res("updatedOn")
  private LocalDateTime updatedOn;

  @Getter(AccessLevel.PACKAGE)
  @Column(name = "keyword", nullable = false, length = 555)
  private String keyword;

  public String getName() {
    return "";
  }

  @SuppressWarnings("unchecked")
  protected <T extends AbstractEntity> T loadOriginal() {
    return (T) this.original;
  }

  @PreUpdate
  @PrePersist
  protected void preSave() {
    log.info("saving: " + this);

    // generate keyword
    StringJoiner keywordJoiner = new StringJoiner("~");
    try {
      generateKeywordsFromClass(keywordJoiner);
      this.keyword = keywordJoiner.toString();
    } catch (Exception e) {
      log.error("exception while generating keywords" + this + ": " + e.getMessage(), e);
    }
  }

  @PostLoad
  protected void postLoad() {
    this.mode = Mode.NORMAL;

    log.info("loaded: " + this);
  }

  void validateInternal() {
    log.info("validating: " + this);
    for (EntField field : getTable().getFields().values()) {
      validateInternal(field);
    }
    validate();
  }

  protected void validate() {

  }

  void validateInternal(EntField field) {
    if (field.isSubEntity()) {
      Iterable<?> collection = getField(field);
      validateCollection(field, collection);
    }

    if (AbstractEntity.class.isAssignableFrom(field.getType())) {
      AbstractEntity entity = getEntity(field);
      if (entity.isEditing())
        entity.validateInternal();
    }

    if (field.isColumn()) {
      Column column = field.getColumn();
      if (!column.nullable() && isNull(field) && !field.isField(F_ID, F_CREATED_ON, F_UPDATED_ON))
        throw CxException.missing(this, field);
    }

    if (field.isJoinColumn()) {
      JoinColumn joinColumn = field.getJoinColumn();
      if (!joinColumn.nullable() && isNull(field))
        throw CxException.missing(this, field);
    }

    validate(field);
  }

  public void validate(EntField field) {

  }

  public void fieldChanged(EntField field, Object newValue, Object oldValue) {

  }

  private void validateCollection(EntField field, Iterable<?> collection) {
    for (Object obj : collection) {
      if (obj != null) {
        if (SubEntity.class.isAssignableFrom(obj.getClass())) {
          // can be validated
          SubEntity entity = (SubEntity) obj;
          entity.validateInternal();
        } else {
          throw new IllegalStateException(field + ": should be type of " + SubEntity.class.getSimpleName());
        }
      }
    }
  }

  private void generateKeywordsFromClass(StringJoiner keywordJoiner) throws IllegalAccessException {
    // loops over class fields using reflection
    for (Field field : ReflectionUtils.getDeclaredFields(getClass())) {
      field.trySetAccessible();
      if (field.isAnnotationPresent(Keyword.class)) {
        if (SpecEntity.class.isAssignableFrom(field.getType())) {
          // if the field type extends SpecEntity then use the name of that spec in the keyword
          SpecEntity specKeyword = (SpecEntity) field.get(this);
          if (specKeyword != null)
            keywordJoiner.add(specKeyword.getName());
        } else {
          Object object = field.get(this);
          if (object != null)
            keywordJoiner.add(object.toString());
        }
      }
    }
  }

  @Override
  public final boolean equals(Object object) {
    if (this == object) return true;
    if (object == null) return false;
    Class<?> oEffectiveClass = object instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    if (object instanceof AbstractEntity entity)
      return Objects.equals(getId(), entity.getId());
    return false;
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }

  @Override
  public String toString() {
    if (this instanceof SpecEntity)
      return String.format("%s [%d]: MODE '%s' | %s  ", getClass().getSimpleName(), getId(), getMode(), getName());
    return String.format("%s [%d]: MODE '%s'", getClass().getSimpleName(), getId(), getMode());
  }

  @SuppressWarnings("unchecked")
  public final <T extends AbstractEntity> T edit() {
    if (isEditing()) {
      return (T) this;
    } else if (canEdit()) {
      if (this instanceof SubEntity subEntity) {
        subEntity.getParent().edit();
      }

      this.original = this.copy();
      this.mode = Mode.EDIT;
      return (T) this;
    } else {
      throw CxException.badRequest(getClass(), cantEditMessage());
    }
  }

  @SuppressWarnings("unchecked")
  protected final <T extends AbstractEntity> T copy() {
    try {
      Class<? extends AbstractEntity> tClass = getClass();
      AbstractEntity clone = tClass.getConstructor().newInstance();
      for (EntField field : getTable().getFields().values()) {
        field.set(clone, field.get(this));
      }
      return (T) clone;
    } catch (Exception e) {
      log.error("exception while copying entity: " + this + ": " + e.getMessage(), e);
      return null;
    }
  }

  public boolean canEdit() {
    return true;
  }

  public final boolean isEditing() {
    return this.mode == Mode.EDIT || this.mode == Mode.NEW;
  }

  public final boolean isNew() {
    return this.mode == Mode.NEW;
  }

  protected String cantEditMessage() {
    return "can't modify entity";
  }

  public EntField field(int id) {
    return getTable().findField(id);
  }

  public final String getString(int id) {
    return getString(field(id));
  }

  public final String getString(EntField field) {
    Object value = getField(field);
    if (value instanceof String string)
      return string;
    return null;
  }

  public <T extends AbstractEntity> T getEntity(int id) {
    return getEntity(field(id));
  }

  public <T extends AbstractEntity> T getEntity(EntField field) {
    return getField(field);
  }

  public final  <T> T getField(int id) {
    return getField(field(id));
  }

  @SuppressWarnings("unchecked")
  public final <T> T getField(EntField field) {
    return (T) field.get(this);
  }

  public final void setField(int id, Object value) {
    setField(field(id), value);
  }

  public final void setField(EntField field, Object value) {
    field.set(this, value);
  }

  public final boolean isNull(int id) {
    return isNull(field(id));
  }

  public final boolean isNull(EntField field) {
    return field.get(this) == null;
  }

  public String getLabel() {
    return getTable().getLabel();
  }

  public Long getId() {
    return getField(F_ID);
  }

  public LocalDateTime getCreatedOn() {
    return getField(F_CREATED_ON);
  }

  public LocalDateTime getUpdatedOn() {
    return getField(F_UPDATED_ON);
  }
}
