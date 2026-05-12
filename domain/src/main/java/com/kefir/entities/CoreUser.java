package com.kefir.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "core_user")
@NoArgsConstructor
@AllArgsConstructor
public class CoreUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String username;
  private String password;
  private String fullName;
  private boolean enabled;

  @ManyToMany
  @JoinTable(
          name = "user_role",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();
}
