package com.auction.exceptions;

/**
 * Exception thrown when a bid is invalid.
 */
public class InvalidBidException extends AuctionException {
    public InvalidBidException(String message) {
        super(message);
    }
}
