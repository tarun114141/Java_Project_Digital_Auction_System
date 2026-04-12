package com.auction.entities;

import com.auction.core.BaseEntity;
import java.util.Date;

/**
 * Represents a bid placed by a user on an item.
 */
public class Bid extends BaseEntity {
    private int userId;
    private int itemId;
    private double amount;
    private Date bidTime;

    public Bid(int id, int userId, int itemId, double amount) {
        super(id);
        this.userId = userId;
        this.itemId = itemId;
        this.amount = amount;
        this.bidTime = new Date();
    }

    @Override
    public String getSummary() {
        return "Bid of $" + amount + " on Item ID " + itemId + " by User ID " + userId;
    }

    // Getters
    public int getUserId() { return userId; }
    public int getItemId() { return itemId; }
    public double getAmount() { return amount; }
    public Date getBidTime() { return bidTime; }
}
