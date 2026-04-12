package frontend;

import com.auction.services.AuctionSystem;
import com.auction.entities.*;

import javax.swing.*;
import java.util.Date;

public class AppRunner {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1. Initialize Backend
        AuctionSystem system = new AuctionSystem();
        setupDummyData(system);

        // 2. Launch GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(system);
            frame.setVisible(true);
            frame.navigateTo("LOGIN");
        });
    }

    private static void setupDummyData(AuctionSystem system) {
        try {
            // Add category
            system.addCategory(new Category(1, "Art", "Fine arts and paintings"));
            system.addCategory(new Category(2, "Tech", "Gadgets and tech"));

            // Add basic users
            system.registerUser(new User(1, "Alice", "alice@example.com", "password", "123456789", "123 Main St", "BUYER"));
            system.registerUser(new Admin(2, "Admin Bob", "admin@auction.com", "admin", "987654321", "456 Main St"));

            // Create auction
            AuctionEvent event = new AuctionEvent(1, "Spring Clearance", "Clearance sale", new Date(System.currentTimeMillis() - 100000), new Date(System.currentTimeMillis() + 3600000), 2);
            system.createAuction(event);
            system.approveAuction(1, 2);

            // Add items
            system.listItem(new Item(1, "Mona Lisa Copy", "A beautiful replica", 100.0, "img.jpg", 1, 1, 1));
            system.listItem(new Item(2, "Vintage Macbook", "Working condition 2008 macbook", 150.0, "img.jpg", 1, 2, 1));
            system.listItem(new Item(3, "Rolex Watch", "Stainless steel, excellent condition", 800.0, "img.jpg", 1, 1, 1));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
