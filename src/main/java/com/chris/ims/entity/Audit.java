package com.chris.ims.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "t_audit")
public class Audit {
  
  enum Type {
    CREATE,
    UPDATE,
    DELETE
  }
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "entity_class", nullable = false)
  private Class<? extends AbstractEntity> entityClass;

  @Column(name = "entity_id", nullable = false)
  private Long entityId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "when", nullable = false)
  private LocalDateTime when;

}