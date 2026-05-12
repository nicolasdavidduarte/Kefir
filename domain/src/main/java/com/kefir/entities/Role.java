package com.kefir.entities;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  @ManyToMany(mappedBy = "roles")
  private Set<CoreUser> users;

  public Role() {}

  public Role(String name) {
    this.name = name;
  }
}
