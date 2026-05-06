package com.kefir.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "person_type")
public class PersonType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  // Constructors
  public PersonType() {}

  public PersonType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
