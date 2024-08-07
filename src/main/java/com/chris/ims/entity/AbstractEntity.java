package com.chris.ims.entity;

import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.entity.annotations.SubEntity;
import com.chris.ims.entity.exception.BxException;
import io.swagger.v3.core.util.ReflectionUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * base class for all entities in the system. It provided basic functionality such as
 * auto generated ids, creation and update timestamps, keyword generation,
 * and a mechanism for validating entities.
 */
@Slf4j
@Getter
@Setter(AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class AbstractEntity {

  public static final String GROUP_ALL = "all";

  /**
   * the original instance of this entity. This is used to detect changes
   */
  @Transient
  @Getter(AccessLevel.NONE)
  private AbstractEntity original;

  /**
   * the id of this entity
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  /**
   * the creation timestamp of this entity
   */
  @CreationTimestamp
  @Column(name = "created_on", nullable = false, updatable = false)
  private LocalDateTime createdOn;

  /**
   * the update timestamp of this entity
   */
  @UpdateTimestamp
  @Column(name = "updated_on", nullable = false)
  private LocalDateTime updatedOn;

  /**
   * the keyword of this entity used for searching
   */
  @Column(name = "keyword", nullable = false, length = 555)
  private String keyword;

  /**
   * Returns the name of this entity.
   * @return the name of this entity
   */
  public String getName() {
    return "";
  }

  /**
   * loads the original entity
   * @return the original entity before being changed
   */
  @SuppressWarnings("unchecked")
  protected <T extends AbstractEntity> T loadOriginal() {
    return (T) this.original;
  }

  /**
   * called before saving entity
   */
  @PreUpdate
  @PrePersist
  protected void preSave() {
    log.info("saving: " + this);

    // generate keyword
    StringJoiner keywordJoiner = new StringJoiner("~");
    try {
      generateKeywordsFromClass(getClass(), keywordJoiner);
      this.keyword = keywordJoiner.toString();
    } catch (Exception e) {
      log.error("exception while generating keywords" + this + ": " + e.getMessage(), e);
    }
  }

  /**
   * called after loading entity
   */
  @PostLoad
  protected void postLoad() {
    log.info("loaded: " + this);

    this.original = this.copy();
  }

  /**
   * validates this entity, this method is called before saving in the {@link AbstractEntityFacade}
   */
  protected void validate() {
    log.info("validating: " + this);

    validateSubEntity(getClass());
  }

  /**
   * validates sub entities in collections
   *
   * @param tClass the current class to validate sub entities in
   */
  private <T> void validateSubEntity(Class<T> tClass) {
    // loops over class fields using reflection
    for (Field field : ReflectionUtils.getDeclaredFields(tClass)) {
      field.setAccessible(true);
      try {
        if (field.isAnnotationPresent(SubEntity.class) && (Iterable.class.isAssignableFrom(field.getType()))) {
            Iterable<?> collection = (Iterable<?>) field.get(this);
            for (Object obj : collection) {
              if (obj != null) {
                if (AbstractEntity.class.isAssignableFrom(obj.getClass())) {
                  // can be validated
                  AbstractEntity entity = (AbstractEntity) obj;
                  entity.validate();
                } else break;
              }
            }
        }
      } catch (Exception e) {
        if (e instanceof BxException bxException)
          throw bxException;

        log.error("exception while validating field on " + this + ": " + field.getName() + ": " + e.getMessage(), e);
      }
    }
  }

  /**
   * auto generates keywords for this entity
   *
   * @param tClass the current entity class to generate keywords from
   * @param keywordJoiner the joiner for keywords
   * @throws IllegalAccessException if an exception occurs while accessing the fields
   */
  private <T> void generateKeywordsFromClass(@NotNull Class<T> tClass, StringJoiner keywordJoiner) throws IllegalAccessException {
    // loops over class fields using reflection
    for (Field field : ReflectionUtils.getDeclaredFields(tClass)) {
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
      return String.format("%s [%d]: %s ", getClass().getSimpleName(), getId(), getName());
    return String.format("%s [%d]", getClass().getSimpleName(), getId());
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractEntity> T copy() {
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

  public boolean canModify() {
    return true;
  }

  public void checkEditMode() {
    if (!canModify()) {
      throw BxException.badRequest(getClass(), canModifyMessage(), this);
    }
  }

  protected String canModifyMessage() {
    return "can't modify entity";
  }
}
