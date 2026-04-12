package com.auction.entities;

import com.auction.core.BaseEntity;
import com.auction.core.Authenticatable;

/**
 * Represents a User in the system.
 */
public class User extends BaseEntity implements Authenticatable {
    private String name;
    private String email;
    private String password;
    private boolean isLoggedIn;

    public User(int id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.isLoggedIn = false;
    }

    @Override
    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            this.isLoggedIn = true;
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        this.isLoggedIn = false;
    }

    @Override
    public String getRole() {
        return "USER";
    }

    @Override
    public String getSummary() {
        return "User: " + name + " (" + email + ")";
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isLoggedIn() { return isLoggedIn; }
}
