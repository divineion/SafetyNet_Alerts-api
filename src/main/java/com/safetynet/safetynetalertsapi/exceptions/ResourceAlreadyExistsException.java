package com.safetynet.safetynetalertsapi.exceptions;

public class ResourceAlreadyExistsException extends Throwable {
    /**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
