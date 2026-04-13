package com.auction.dao;

import com.auction.core.DatabaseConnection;
import com.auction.entities.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for PAYMENT table.
 */
public class PaymentDao {

    public boolean createPayment(Payment payment) {
        String sql = "INSERT INTO PAYMENT (payment_id, payment_date, amount, status, item_id, buyer_id) " +
                     "VALUES (payment_seq.NEXTVAL, CURRENT_TIMESTAMP, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, payment.getAmount());
            ps.setString(2, payment.getStatus());
            ps.setInt(3, payment.getItemId());
            ps.setInt(4, payment.getBuyerId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PaymentDao] Error creating payment: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStatus(int paymentId, String status) {
        String sql = "UPDATE PAYMENT SET status = ? WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, paymentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PaymentDao] Error updating payment: " + e.getMessage());
            return false;
        }
    }

    public List<Payment> getPaymentsByBuyer(int buyerId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM PAYMENT WHERE buyer_id = ? ORDER BY payment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDao] Error fetching payments: " + e.getMessage());
        }
        return list;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("payment_id"),
            rs.getInt("buyer_id"),
            rs.getInt("item_id"),
            rs.getDouble("amount")
        );
    }
}
