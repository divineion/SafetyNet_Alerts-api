package com.safetynet.safetynetalertsapi.exceptions;

public class InvalidAddressException extends Throwable {
    /**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAddressException(String message) {
        super(message);
    }
}
