package com.chris.ims.entity;

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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@Getter
@Setter(AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class AbstractEntity {

  public static final String GROUP_ALL = "all";

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

  @Column(name = "keyword", nullable = false, length = 555)
  private String keyword;

  public String getName() {
    return "";
  }

  @PreUpdate
  @PrePersist
  protected void preSave() {
    StringJoiner keywordJoiner = new StringJoiner("~");
    try {
      Class<?> currentClass = getClass();
      while (currentClass != null) {
        generateKeywordsFromClass(currentClass, keywordJoiner);
        currentClass = currentClass.getSuperclass();
      }
      this.keyword = keywordJoiner.toString();
    } catch (Exception e) {
      log.error("exception while generating keywords" + this + ": " + e.getMessage(), e);
    }
  }

  @PostLoad
  protected void postLoad() {
    log.debug("loaded: " + this);
  }


  private <T> void generateKeywordsFromClass(@NotNull Class<T> currentClass, StringJoiner keywordJoiner) throws IllegalAccessException {
    for (Field field : currentClass.getDeclaredFields()) {
      field.setAccessible(true);
      if (field.isAnnotationPresent(Keyword.class)) {
        if (SpecEntity.class.isAssignableFrom(field.getType())) {
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
    return String.format("%s [%d]: %s ", getClass().getSimpleName(), getId(), getName());
  }
}
