package com.chris.ims.entity;

import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.entity.annotations.Res;
import com.chris.ims.entity.annotations.SubEntityList;
import com.chris.ims.entity.exception.BxException;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@Getter
@Setter(AccessLevel.PRIVATE)
@SoftDelete(columnName = "deleted")
@MappedSuperclass
public abstract class AbstractEntity {

  public static final String GROUP_ALL = "all";

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
  private Long id;

  @CreationTimestamp
  @Column(name = "created_on", nullable = false, updatable = false)
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on", nullable = false)
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

  protected void validate() {
    log.info("validating: " + this);

    validateSubEntity();
  }

  private void validateSubEntity() {
    // loops over class fields using reflection
    for (Field field : ReflectionUtils.getDeclaredFields(getClass())) {
      field.setAccessible(true);
      try {
        if (field.isAnnotationPresent(SubEntityList.class) && (Iterable.class.isAssignableFrom(field.getType()))) {
          Iterable<?> collection = (Iterable<?>) field.get(this);
          validateCollection(field, collection);
        }
      } catch (Exception e) {
        if (e instanceof BxException bxException)
          throw bxException;

        log.error("exception while validating field on " + this + ": " + field.getName() + ": " + e.getMessage(), e);
      }
    }
  }

  private static void validateCollection(Field field, Iterable<?> collection) {
    for (Object obj : collection) {
      if (obj != null) {
        if (SubEntity.class.isAssignableFrom(obj.getClass())) {
          // can be validated
          SubEntity entity = (SubEntity) obj;
          entity.validate();
        } else {
          throw new IllegalStateException(field + ": should be type of " + SubEntity.class.getSimpleName());
        }
      }
    }
  }

  private void generateKeywordsFromClass(StringJoiner keywordJoiner) throws IllegalAccessException {
    // loops over class fields using reflection
    for (Field field : ReflectionUtils.getDeclaredFields(getClass())) {
      field.setAccessible(true);
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
      throw BxException.badRequest(getClass(), cantEditMessage());
    }
  }

  @SuppressWarnings("unchecked")
  protected final <T extends AbstractEntity> T copy() {
    try {
      Class<? extends AbstractEntity> tClass = getClass();
      AbstractEntity clone = tClass.getConstructor().newInstance();
      for (Field field : ReflectionUtils.getDeclaredFields(tClass)) {
        field.setAccessible(true);
        int fieldMod = field.getModifiers();
        if (!Modifier.isFinal(fieldMod) && !Modifier.isStatic(fieldMod)) {
          field.set(clone, field.get(this));
        }
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

  public Field field(String name) {
    for (Field field : ReflectionUtils.getDeclaredFields(getClass())) {
      Res res = field.getAnnotation(Res.class);
      if (res != null && res.value().equals(name)) {
        return field;
      }
    }
    return null;
  }
}
