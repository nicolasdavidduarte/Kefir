package org.kefir.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "system_user")
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String fullName;
    private boolean enabled;

    // Constructors
    public SystemUser() {}

    public SystemUser(int id, String username, String password, String fullName, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.enabled = enabled;
    }

    // Getters and Setters
    public int getSystemUserId() {
        return id;
    }

    public void setSystemUserId(int id) {
        this.id = id;
    }

    public String getSystemUsername() {
        return username;
    }

    public void setSystemUserName(String name) {
        this.username = username;
    }

    public String getSystemUserPassword() {
        return password;
    }

    public void setSystemUserPassword(String password) {
        this.password = password;
    }

    public String getSystemUserFullName() {
        return fullName;
    }

    public void setSystemUserFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isSystemUserEnabled() {
        return enabled;
    }

    public void setSystemUserEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
