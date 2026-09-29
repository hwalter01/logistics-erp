package com.logistics.orderservice.exceptions;

public class MasterDataUnavailableException extends RuntimeException {

    public MasterDataUnavailableException(Throwable cause) {
        super("Master Data Service unavailable", cause);
    }
}