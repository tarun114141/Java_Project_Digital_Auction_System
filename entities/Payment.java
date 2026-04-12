package com.auction.entities;

import com.auction.core.BaseEntity;
import java.util.Date;

/**
 * Represents a payment for a winning bid.
 */
public class Payment extends BaseEntity {
    private int buyerId;
    private int itemId;
    private double amount;
    private Date paymentDate;
    private String status;

    public Payment(int id, int buyerId, int itemId, double amount) {
        super(id);
        this.buyerId = buyerId;
        this.itemId = itemId;
        this.amount = amount;
        this.paymentDate = new Date();
        this.status = "PENDING";
    }

    @Override
    public String getSummary() {
        return "Payment of $" + amount + " [" + status + "] by Buyer ID " + buyerId + " for Item ID " + itemId;
    }

    public int getBuyerId() { return buyerId; }
    public int getUserId() { return buyerId; } // compatibility
    public int getItemId() { return itemId; }
    public double getAmount() { return amount; }
    public Date getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
