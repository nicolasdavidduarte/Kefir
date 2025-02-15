package org.kefir.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String firstName1;
    private String firstName2;
    private String lastName1;
    private String lastName2;
    private String nameShort;
    private int personType;
    private int customerType;
    private int status;
    private Date lastModificationDate;
    private int coreUser;

    // Constructors
    public Customer() {}

    public Customer(int id, String name, String firstName1, String firstName2, String lastName1, String lastName2, String nameShort, int personType, int customerType, int status, Date lastModificationDate, int coreUser) {
        this.id = id;
        this.name = name;
        this.firstName1 = firstName1;
        this.firstName2 = firstName2;
        this.lastName1 = lastName1;
        this.nameShort = nameShort;
        this.personType = personType;
        this.customerType = customerType;
        this.status = status;
        this.lastModificationDate = lastModificationDate;
        this.coreUser = coreUser;
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

    public String getFirstName1() {
        return firstName1;
    }

    public void setFirstName1(String firstName1) {
        this.firstName1 = firstName1;
    }

    public String getFirstName2() {
        return firstName2;
    }

    public void setFirstName2(String firstName2) {
        this.firstName2 = firstName2;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public int getCoreUser() {
        return coreUser;
    }

    public void setCoreUser(int coreUser) {
        this.coreUser = coreUser;
    }
}
