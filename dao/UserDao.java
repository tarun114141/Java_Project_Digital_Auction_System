package com.auction.dao;

import com.auction.core.DatabaseConnection;
import com.auction.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for USERS table.
 * Handles all DB operations related to users.
 */
public class UserDao {

    /**
     * Inserts a new user into the USERS table.
     * Uses the users_seq Oracle sequence for the primary key.
     */
    public boolean registerUser(User user) {
        String sql = "INSERT INTO USERS (user_id, name, email, password, phone, address, role) " +
                     "VALUES (users_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getRole());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[UserDao] Error inserting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates a user by email and password.
     * Returns the User object if credentials match, null otherwise.
     */
    public User login(String email, String password) {
        String sql = "SELECT * FROM USERS WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] Error during login: " + e.getMessage());
        }
        return null;
    }

    /**
     * Finds a user by their ID.
     */
    public User findById(int userId) {
        String sql = "SELECT * FROM USERS WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] Error finding user by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Returns all registered users.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] Error fetching all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Checks if an email already exists in the DB.
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] Error checking email: " + e.getMessage());
        }
        return false;
    }

    /**
     * Helper - maps a ResultSet row to a User object.
     */
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getString("role")
        );
    }
}
