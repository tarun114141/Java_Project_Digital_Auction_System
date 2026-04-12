package com.auction.entities;

/**
 * Represents an Admin in the system.
 */
public class Admin extends User {
    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public String getSummary() {
        return "Administrator: " + getName();
    }
}
