package com.kefir.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq_gen")
  @SequenceGenerator(name = "role_id_seq_gen", sequenceName = "role_id_seq", allocationSize = 1)
  private int id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
  @JsonIgnore
  private Set<User> users;

  public Role() {}

  public Role(String name) {
    this.name = name;
  }
}
