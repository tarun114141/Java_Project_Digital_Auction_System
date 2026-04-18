package entities;

import core.BaseEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item listed for auction.
 */
public class Item extends BaseEntity {
    private String name;
    private String description;
    private double startingPrice; // maps to base_price
    private String imageUrl;
    private String status;
    private int sellerId;
    private int categoryId;
    private int auctionId;
    private List<Bid> bids = new ArrayList<>();

    public Item(int id, String name, String description, double startingPrice, String imageUrl, int sellerId, int categoryId, int auctionId) {
        super(id);
        this.name = name;
        this.description = description;
        this.startingPrice = startingPrice;
        this.imageUrl = imageUrl;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.auctionId = auctionId;
        this.status = "AVAILABLE";
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public List<Bid> getBids() {
        return bids;
    }

    public Bid getHighestBid() {
        Bid highest = null;
        for (Bid b : bids) {
            if (highest == null || b.getAmount() > highest.getAmount()) {
                highest = b;
            }
        }
        return highest;
    }

    @Override
    public String getSummary() {
        return "Item: " + name + " | Starting Price: $" + startingPrice;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getStartingPrice() { return startingPrice; }
    public String getImageUrl() { return imageUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getSellerId() { return sellerId; }
    public int getCategoryId() { return categoryId; }
    public int getAuctionId() { return auctionId; }
}
