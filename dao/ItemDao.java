package dao;

import core.DatabaseConnection;
import entities.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for ITEM table.
 */
public class ItemDao {

    public boolean addItem(Item item) {
        String sql = "INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id) " +
                     "VALUES (item_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setDouble(3, item.getStartingPrice());
            ps.setString(4, item.getImageUrl());
            ps.setString(5, item.getStatus());
            ps.setInt(6, item.getSellerId());
            ps.setInt(7, item.getAuctionId());
            ps.setInt(8, item.getCategoryId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[ItemDao] Error adding item: " + e.getMessage());
            return false;
        }
    }

    public List<Item> getAllAvailableItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM ITEM WHERE status = 'AVAILABLE'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) items.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[ItemDao] Error fetching items: " + e.getMessage());
        }
        return items;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM ITEM";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) items.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[ItemDao] Error fetching all items: " + e.getMessage());
        }
        return items;
    }

    public List<Item> searchItems(String keyword) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM ITEM WHERE LOWER(name) LIKE ? OR LOWER(description) LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ItemDao] Error searching items: " + e.getMessage());
        }
        return items;
    }

    public List<Item> getItemsByAuction(int auctionId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM ITEM WHERE auction_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, auctionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ItemDao] Error fetching items by auction: " + e.getMessage());
        }
        return items;
    }

    public List<Item> getItemsBySeller(int sellerId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM ITEM WHERE seller_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ItemDao] Error fetching items by seller: " + e.getMessage());
        }
        return items;
    }

    public boolean updateItemStatus(int itemId, String status) {
        String sql = "UPDATE ITEM SET status = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ItemDao] Error updating item status: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM ITEM WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ItemDao] Error deleting item: " + e.getMessage());
            return false;
        }
    }

    public List<Item> getFilteredAvailableItems(String keyword, int categoryId) {
        List<Item> items = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ITEM WHERE status = 'AVAILABLE'");
        
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = categoryId > 0;
        
        if (hasKeyword) {
            sql.append(" AND (LOWER(name) LIKE ? OR LOWER(description) LIKE ?)");
        }
        if (hasCategory) {
            sql.append(" AND category_id = ?");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
             
            int paramIdx = 1;
            if (hasKeyword) {
                String kw = "%" + keyword.toLowerCase() + "%";
                ps.setString(paramIdx++, kw);
                ps.setString(paramIdx++, kw);
            }
            if (hasCategory) {
                ps.setInt(paramIdx++, categoryId);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ItemDao] Error fetching filtered items: " + e.getMessage());
        }
        return items;
    }

    private Item mapRow(ResultSet rs) throws SQLException {
        return new Item(
            rs.getInt("item_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("base_price"),
            rs.getString("image_url"),
            rs.getInt("seller_id"),
            rs.getInt("auction_id"),
            rs.getInt("category_id")
        );
    }
}
