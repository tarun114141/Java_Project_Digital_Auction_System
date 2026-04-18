package dao;

import core.DatabaseConnection;
import entities.AuctionEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for AUCTION_EVENT table.
 */
public class AuctionDao {

    public boolean createAuction(AuctionEvent event) {
        String sql = "INSERT INTO AUCTION_EVENT (auction_id, title, description, start_time, end_time, status, conducted_by) " +
                     "VALUES (auction_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setTimestamp(3, new Timestamp(event.getStartTime().getTime()));
            ps.setTimestamp(4, new Timestamp(event.getEndTime().getTime()));
            ps.setString(5, event.getStatus());
            ps.setInt(6, event.getConductedBy());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[AuctionDao] Error creating auction: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStatus(int auctionId, String status) {
        String sql = "UPDATE AUCTION_EVENT SET status = ? WHERE auction_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, auctionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AuctionDao] Error updating auction status: " + e.getMessage());
            return false;
        }
    }

    public AuctionEvent findById(int auctionId) {
        String sql = "SELECT * FROM AUCTION_EVENT WHERE auction_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, auctionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("[AuctionDao] Error finding auction: " + e.getMessage());
        }
        return null;
    }

    public List<AuctionEvent> getActiveAuctions() {
        List<AuctionEvent> list = new ArrayList<>();
        String sql = "SELECT * FROM AUCTION_EVENT WHERE status = 'ONGOING'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[AuctionDao] Error fetching active auctions: " + e.getMessage());
        }
        return list;
    }

    public List<AuctionEvent> getAllAuctions() {
        List<AuctionEvent> list = new ArrayList<>();
        String sql = "SELECT * FROM AUCTION_EVENT ORDER BY start_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[AuctionDao] Error fetching all auctions: " + e.getMessage());
        }
        return list;
    }

    private AuctionEvent mapRow(ResultSet rs) throws SQLException {
        return new AuctionEvent(
            rs.getInt("auction_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getTimestamp("start_time"),
            rs.getTimestamp("end_time"),
            rs.getInt("conducted_by")
        );
    }
}
