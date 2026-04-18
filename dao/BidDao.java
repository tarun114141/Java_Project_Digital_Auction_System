package dao;

import core.DatabaseConnection;
import entities.Bid;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for BID table.
 */
public class BidDao {

    public boolean placeBid(Bid bid) {
        String sql = "INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id) " +
                     "VALUES (bid_seq.NEXTVAL, ?, CURRENT_TIMESTAMP, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, bid.getAmount());
            ps.setInt(2, bid.getItemId());
            ps.setInt(3, bid.getBuyerId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[BidDao] Error placing bid: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns the highest bid for a given item.
     */
    public Bid getHighestBid(int itemId) {
        String sql = "SELECT * FROM (SELECT * FROM BID WHERE item_id = ? ORDER BY bid_amount DESC) WHERE ROWNUM = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("[BidDao] Error fetching highest bid: " + e.getMessage());
        }
        return null;
    }

    public List<Bid> getBidsForItem(int itemId) {
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT * FROM BID WHERE item_id = ? ORDER BY bid_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) bids.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[BidDao] Error fetching bids: " + e.getMessage());
        }
        return bids;
    }

    public List<Bid> getBidsByBuyer(int buyerId) {
        List<Bid> bids = new ArrayList<>();
        String sql = "SELECT * FROM BID WHERE buyer_id = ? ORDER BY bid_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) bids.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[BidDao] Error fetching buyer bids: " + e.getMessage());
        }
        return bids;
    }

    public List<String> getOutbidItemNames(int buyerId) {
        List<String> outbidItems = new ArrayList<>();
        String sql = "SELECT i.name " +
                     "FROM ITEM i " +
                     "WHERE i.status = 'AVAILABLE' " +
                     "  AND EXISTS (SELECT 1 FROM BID WHERE item_id = i.item_id AND buyer_id = ?) " +
                     "  AND (SELECT buyer_id FROM (SELECT buyer_id, bid_amount FROM BID WHERE item_id = i.item_id ORDER BY bid_amount DESC) WHERE ROWNUM = 1) != ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ps.setInt(2, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    outbidItems.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[BidDao] Error fetching outbid items: " + e.getMessage());
        }
        return outbidItems;
    }

    private Bid mapRow(ResultSet rs) throws SQLException {
        return new Bid(
            rs.getInt("bid_id"),
            rs.getInt("buyer_id"),
            rs.getInt("item_id"),
            rs.getDouble("bid_amount")
        );
    }
}
