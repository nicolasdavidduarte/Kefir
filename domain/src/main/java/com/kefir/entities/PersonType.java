package com.kefir.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "person_type")
@NoArgsConstructor
@AllArgsConstructor
public class PersonType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;
}
