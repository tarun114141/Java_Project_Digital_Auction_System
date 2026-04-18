package core;

import java.util.Date;

/**
 * Abstract base class for all entities in the system.
 * Demonstrates the use of abstract classes in Java.
 */
public abstract class BaseEntity {
    private int id;
    private Date createdAt;

    public BaseEntity(int id) {
        this.id = id;
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Abstract method to be implemented by subclasses to provide a summary.
     */
    public abstract String getSummary();
}
