package com.safetynet.safetynetalertsapi.exceptions;

public class IdentityMismatchException extends Throwable {
    /**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public IdentityMismatchException(String message) {
        super(message);
    }
}
