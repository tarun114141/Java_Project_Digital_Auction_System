package com.auction.services;

import com.auction.entities.*;
import com.auction.exceptions.*;
import java.util.List;

/**
 * Interface for Auction Management operations.
 */
public interface AuctionManager {
    void registerUser(User user);
    boolean login(String email, String password);
    void createAuction(AuctionEvent event) throws AuctionException;
    void approveAuction(int auctionId, int adminId) throws AuctionException;
    void closeAuction(int auctionId, int adminId) throws AuctionException;
    void listItem(Item item) throws AuctionException;
    void placeBid(Bid bid) throws AuctionException;
    void processPayment(Payment payment) throws AuctionException;
    void addCategory(Category category);
    
    List<Item> browseItems();
    List<Item> searchItems(String keyword);
    List<Item> getItemsByCategory(int categoryId);
    List<Payment> getPaymentHistory(int userId);
    Bid getHighestBid(int itemId);
    User getWinner(int itemId) throws AuctionException;
}
