package com.safetynet.safetynetalertsapi.exceptions;

public class ResourceNotFoundException extends Throwable {
    /**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
