package com.auction.main;

import com.auction.entities.*;
import com.auction.services.*;
import com.auction.exceptions.*;
import java.util.Date;

/**
 * Main class to demonstrate the Digital Auction Management System.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("--- Digital Auction Management System Backend Logic ---");

        AuctionSystem system = new AuctionSystem();

        // 1. Setup Categories
        Category electronics = new Category(1, "Electronics", "Gadgets and tech");
        system.addCategory(electronics);

        // 2. Register Users and Admins
        User user1 = new User(1, "Alice", "alice@example.com", "pass123");
        User user2 = new User(3, "Charlie", "charlie@example.com", "pass456");
        Admin admin1 = new Admin(2, "Bob (Admin)", "admin@auction.com", "admin789");

        system.registerUser(user1);
        system.registerUser(user2);
        system.registerUser(admin1);

        // 3. Authentication
        System.out.println("\n--- Authentication ---");
        system.login("alice@example.com", "pass123");
        system.login("charlie@example.com", "wrong_pass");

        try {
            // 4. Create and Approve Auction
            AuctionEvent event = new AuctionEvent(1, "Vintage Tech Auction", new Date(System.currentTimeMillis() - 1000), new Date(System.currentTimeMillis() + 3600000));
            system.createAuction(event);
            system.approveAuction(1, 2); // Admin Bob approves auction 1

            // 5. List Items
            Item item1 = new Item(1, "Macintosh 128K", "Original 1984 Macintosh", 500.0, 1, 1);
            system.listItem(item1);

            // 6. Bidding process
            System.out.println("\n--- Bidding Session ---");
            Bid bid1 = new Bid(1, 1, 1, 550.0);
            system.placeBid(bid1);

            Bid bid2 = new Bid(2, 3, 1, 650.0);
            system.placeBid(bid2);

            // Try invalid bid (too low)
            try {
                Bid bid3 = new Bid(3, 1, 1, 600.0);
                system.placeBid(bid3);
            } catch (InvalidBidException e) {
                System.out.println("Validation caught: " + e.getMessage());
            }

            // 7. Search Feature
            System.out.println("\n--- Search Results for 'Macintosh' ---");
            for (Item i : system.searchItems("Macintosh")) {
                System.out.println(i.getSummary());
            }

            // 8. Close Auction and Select Winner
            System.out.println("\n--- Closing Auction ---");
            system.closeAuction(1, 2); // Admin Bob closes auction 1

            // 9. View Winner
            User winner = system.getWinner(1);
            if (winner != null) {
                System.out.println("Official Winner for Item 1: " + winner.getName());
            }

            // 10. View Payment History
            System.out.println("\n--- Charlie's Payment History ---");
            for (Payment p : system.getPaymentHistory(3)) {
                System.out.println(p.getSummary());
            }

        } catch (AuctionException e) {
            System.err.println("Auction Error: " + e.getMessage());
        }
    }
}
