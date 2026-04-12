package com.auction.entities;

import com.auction.core.BaseEntity;
import java.util.Date;

/**
 * Represents a payment for a winning bid.
 */
public class Payment extends BaseEntity {
    private int userId;
    private int bidId;
    private double amount;
    private Date paymentDate;

    public Payment(int id, int userId, int bidId, double amount) {
        super(id);
        this.userId = userId;
        this.bidId = bidId;
        this.amount = amount;
        this.paymentDate = new Date();
    }

    @Override
    public String getSummary() {
        return "Payment of $" + amount + " by User ID " + userId + " for Bid ID " + bidId;
    }

    public int getUserId() { return userId; }
    public double getAmount() { return amount; }
    public Date getPaymentDate() { return paymentDate; }
}
