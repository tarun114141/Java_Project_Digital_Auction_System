package com.auction.entities;

/**
 * Represents an Admin in the system.
 */
public class Admin extends User {
    public Admin(int id, String name, String email, String password, String phone, String address) {
        super(id, name, email, password, phone, address, "ADMIN");
    }

    @Override
    public String getSummary() {
        return "Administrator: " + getName();
    }
}
