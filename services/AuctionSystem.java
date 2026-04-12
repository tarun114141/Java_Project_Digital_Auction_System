package com.auction.services;

import com.auction.entities.*;
import com.auction.exceptions.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Auction Management System.
 * Uses ArrayLists for data storage as requested.
 */
public class AuctionSystem implements AuctionManager {
    private List<User> users = new ArrayList<>();
    private List<AuctionEvent> auctions = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<Bid> bids = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    @Override
    public void registerUser(User user) {
        users.add(user);
        System.out.println("User registered: " + user.getName());
    }

    @Override
    public boolean login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                boolean success = u.login(email, password);
                if (success) {
                    System.out.println("User logged in: " + u.getName());
                    return true;
                }
            }
        }
        System.out.println("Login failed for: " + email);
        return false;
    }

    @Override
    public void createAuction(AuctionEvent event) throws AuctionException {
        if (event == null) throw new AuctionException("Auction event cannot be null.");
        auctions.add(event);
        System.out.println("Auction event created: " + event.getTitle());
    }

    @Override
    public void approveAuction(int auctionId, int adminId) throws AuctionException {
        User admin = findUserById(adminId);
        if (admin == null || !admin.getRole().equals("ADMIN")) {
            throw new AuctionException("Only admins can approve auctions.");
        }
        
        AuctionEvent event = findAuctionById(auctionId);
        if (event == null) throw new AuctionException("Auction not found.");
        
        event.setStatus("ONGOING");
        System.out.println("Auction approved and ONGOING: " + event.getTitle());
    }

    @Override
    public void closeAuction(int auctionId, int adminId) throws AuctionException {
        User admin = findUserById(adminId);
        if (admin == null || !admin.getRole().equals("ADMIN")) {
            throw new AuctionException("Only admins can close auctions.");
        }

        AuctionEvent event = findAuctionById(auctionId);
        if (event == null) throw new AuctionException("Auction not found.");

        event.setStatus("COMPLETED");
        System.out.println("Auction COMPLETED: " + event.getTitle());

        // Process winners for each item in the auction
        for (Item item : event.getItems()) {
            Bid winningBid = item.getHighestBid();
            if (winningBid != null) {
                System.out.println("Winner for " + item.getName() + " is Buyer ID " + winningBid.getBuyerId());
                // Automatically create a payment record
                Payment payment = new Payment(payments.size() + 1, winningBid.getBuyerId(), item.getId(), winningBid.getAmount());
                processPayment(payment);
            } else {
                System.out.println("No bids for " + item.getName());
            }
        }
    }

    @Override
    public void listItem(Item item) throws AuctionException {
        if (item == null) throw new AuctionException("Item cannot be null.");
        
        AuctionEvent event = findAuctionById(item.getAuctionId());
        if (event == null) throw new AuctionException("Auction not found for item.");
        
        items.add(item);
        event.addItem(item); // Link item to auction
        System.out.println("Item listed: " + item.getName() + " in " + event.getTitle());
    }

    @Override
    public void placeBid(Bid bid) throws AuctionException {
        Item item = findItemById(bid.getItemId());
        if (item == null) throw new AuctionException("Item not found.");

        AuctionEvent event = findAuctionById(item.getAuctionId());
        if (event == null || !event.isActive()) {
            throw new AuctionException("Auction is not active or has expired.");
        }

        Bid currentHighest = item.getHighestBid();
        double minPrice = (currentHighest != null) ? currentHighest.getAmount() : item.getStartingPrice();

        if (bid.getAmount() <= minPrice) {
            throw new InvalidBidException("Bid amount must be higher than current price: $" + minPrice);
        }

        bids.add(bid);
        item.addBid(bid); // Link bid to item
        System.out.println("New bid placed: $" + bid.getAmount() + " on " + item.getName());
    }

    @Override
    public void processPayment(Payment payment) throws AuctionException {
        if (payment == null || payment.getAmount() <= 0) {
            throw new AuctionException("Invalid payment details.");
        }
        payments.add(payment);
        System.out.println("Payment processed: $" + payment.getAmount() + " for Buyer ID " + payment.getBuyerId());
    }

    @Override
    public void addCategory(Category category) {
        categories.add(category);
        System.out.println("Category added: " + category.getName());
    }

    @Override
    public List<Item> browseItems() {
        return new ArrayList<>(items);
    }

    @Override
    public List<Item> searchItems(String keyword) {
        List<Item> result = new ArrayList<>();
        for (Item i : items) {
            if (i.getName().toLowerCase().contains(keyword.toLowerCase()) || 
                i.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(i);
            }
        }
        return result;
    }

    @Override
    public List<Item> getItemsByCategory(int categoryId) {
        List<Item> filtered = new ArrayList<>();
        for (Item i : items) {
            if (i.getCategoryId() == categoryId) filtered.add(i);
        }
        return filtered;
    }

    @Override
    public List<Payment> getPaymentHistory(int userId) {
        List<Payment> history = new ArrayList<>();
        for (Payment p : payments) {
            if (p.getBuyerId() == userId) history.add(p);
        }
        return history;
    }

    @Override
    public Bid getHighestBid(int itemId) {
        Item item = findItemById(itemId);
        return (item != null) ? item.getHighestBid() : null;
    }

    @Override
    public User getWinner(int itemId) throws AuctionException {
        Item item = findItemById(itemId);
        if (item == null) throw new AuctionException("Item not found.");
        
        AuctionEvent event = findAuctionById(item.getAuctionId());
        if (event == null || !"COMPLETED".equals(event.getStatus())) {
            throw new AuctionException("Auction is still ongoing or pending.");
        }

        Bid winningBid = item.getHighestBid();
        if (winningBid == null) return null;

        return findUserById(winningBid.getBuyerId());
    }

    private Item findItemById(int id) {
        for (Item i : items) {
            if (i.getId() == id) return i;
        }
        return null;
    }

    private User findUserById(int id) {
        for (User u : users) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    private AuctionEvent findAuctionById(int id) {
        for (AuctionEvent a : auctions) {
            if (a.getId() == id) return a;
        }
        return null;
    }
}
