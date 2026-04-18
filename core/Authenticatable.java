package core;

/**
 * Interface for entities that require authentication.
 * Demonstrates the use of interfaces in Java.
 */
public interface Authenticatable {
    boolean login(String email, String password);
    void logout();
    String getRole();
}
