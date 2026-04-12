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
    private String phone;
    private String address;
    private String role; // ADMIN / SELLER / BUYER
    private double rating;
    private boolean isLoggedIn;

    public User(int id, String name, String email, String password, String phone, String address, String role) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.rating = 0.0;
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
        return role;
    }

    @Override
    public String getSummary() {
        return "User: " + name + " (" + email + ") - Role: " + role;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public boolean isLoggedIn() { return isLoggedIn; }
}
