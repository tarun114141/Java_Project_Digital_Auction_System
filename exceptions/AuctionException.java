package exceptions;

/**
 * Custom exception for auction-related errors.
 */
public class AuctionException extends Exception {
    public AuctionException(String message) {
        super(message);
    }
}
