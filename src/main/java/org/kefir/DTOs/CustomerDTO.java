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

    // Constructor
    public CustomerDTO(int id, String name, String firstName1, String firstName2, String lastName1, String lastName2, String nameShort, int personType, int customerType, String name1, String firstName11, String lastName11, String firstName21, String lastName21, String nameShort1, int personType1, int customerType1, int status, Date lastModificationDate, int coreUser) {
        this.id = id;
        this.name = name1;
        this.firstName1 = firstName11;
        this.lastName1 = lastName11;
        this.firstName2 = firstName21;
        this.lastName2 = lastName21;
        this.nameShort = nameShort1;
        this.personType = personType1;
        this.customerType = customerType1;
        this.status = status;
        this.lastModificationDate = lastModificationDate;
        this.coreUser = coreUser;
    }

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