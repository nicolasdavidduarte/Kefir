package org.kefir.DTOs;

import java.util.Date;

public class CustomerDTO {
  private final int id;
  private final String name;
  private final String firstName1;
  private final String lastName1;
  private final String firstName2;
  private final String lastName2;
  private final String nameShort;
  private final int personType;
  private final int customerType;
  private final int status;
  private final Date lastModificationDate;
  private final int coreUser;

  // Constructor to directly map the entity to DTO
  public CustomerDTO(
          int id,
          String name,
          String firstName1,
          String firstName2,
          String lastName1,
          String lastName2,
          String nameShort,
          int personType,
          int customerType,
          int status,
          Date lastModificationDate,
          int coreUser) {
    this.id = id;
    this.name = name;
    this.firstName1 = firstName1;
    this.lastName1 = lastName1;
    this.firstName2 = firstName2;
    this.lastName2 = lastName2;
    this.nameShort = nameShort;
    this.personType = personType;
    this.customerType = customerType;
    this.status = status;
    this.lastModificationDate = lastModificationDate;
    this.coreUser = coreUser;
  }

  // Getters for each field
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getFirstName1() {
    return firstName1;
  }

  public String getLastName1() {
    return lastName1;
  }

  public String getFirstName2() {
    return firstName2;
  }

  public String getLastName2() {
    return lastName2;
  }

  public String getNameShort() {
    return nameShort;
  }

  public int getPersonType() {
    return personType;
  }

  public int getCustomerType() {
    return customerType;
  }

  public int getStatus() {
    return status;
  }

  public Date getLastModificationDate() {
    return lastModificationDate;
  }

  public int getCoreUser() {
    return coreUser;
  }
}
