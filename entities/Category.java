package com.auction.entities;

import com.auction.core.BaseEntity;

/**
 * Represents a category for items.
 */
public class Category extends BaseEntity {
    private String name;
    private String description;

    public Category(int id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    @Override
    public String getSummary() {
        return "Category: " + name;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
}
