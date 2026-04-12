package com.auction.entities;

import com.auction.core.BaseEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an auction event.
 */
public class AuctionEvent extends BaseEntity {
    private String title;
    private Date startTime;
    private Date endTime;
    private String status;
    private List<Item> items = new ArrayList<>();

    public AuctionEvent(int id, String title, Date startTime, Date endTime) {
        super(id);
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "PENDING";
    }

    public boolean isActive() {
        Date now = new Date();
        return "ACTIVE".equals(status) && now.after(startTime) && now.before(endTime);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String getSummary() {
        return "Auction: " + title + " [" + status + "] | Items: " + items.size();
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
