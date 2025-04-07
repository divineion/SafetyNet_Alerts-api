package com.safetynet.safetynetalertsapi.repositories;

public class InvalidAddressException extends Throwable {
    public InvalidAddressException(String message) {
        super(message);
    }
}
