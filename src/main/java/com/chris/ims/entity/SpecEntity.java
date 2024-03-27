package com.chris.ims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.util.Strings;

@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
public abstract class SpecEntity extends AbstractEntity {

  @Keyword
  @Column(name = "code", nullable = false, unique = true)
  private String code;

  @Keyword
  @Column(name = "name")
  private String name;

  @Override
  protected void preSave() {
    super.preSave();
    if (this.code == null) {
      this.code = Strings.left(this.getId().toString(), 10);
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends SpecEntity> T setCode(String code) {
    this.code = code;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public <T extends SpecEntity> T setName(String name) {
    this.name = name;
    return (T) this;
  }

}