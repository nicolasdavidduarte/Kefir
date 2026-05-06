package com.kefir.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_type")
public class CustomerType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  // Constructors
  public CustomerType() {}

  public CustomerType(int id, String name) {
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
