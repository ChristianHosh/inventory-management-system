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
import java.time.Instant;
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
  private Instant createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on", nullable = false)
  private Instant updatedOn;

  @Column(name = "keyword", nullable = false, length = 555)
  private String keyword;

  public String getName() {
    return "";
  }

  public String getCode() {
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
      log.error("Exception while generating keywords: " + e.getMessage(), e);
    }
  }

  private void generateKeywordsFromClass(@NotNull Class<?> currentClass, StringJoiner keywordJoiner) throws Exception {
    for (Field field : currentClass.getDeclaredFields()) {
      if (field.isAnnotationPresent(Keyword.class)) {
        if (field.getType().isAssignableFrom(SpecEntity.class)) {
          SpecEntity specKeyword = (SpecEntity) field.get(this);
          if (specKeyword != null) {
            keywordJoiner.add(specKeyword.getCode());
            keywordJoiner.add(specKeyword.getName());
          }
        } else {
          keywordJoiner.add(field.get(this).toString());
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
    AbstractEntity that = (AbstractEntity) object;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }

  @Override
  public String toString() {
    return String.format("[%d] [%s]: %s, %s ", getId(), getCode(), getClass().getSimpleName(), getName());
  }
}
