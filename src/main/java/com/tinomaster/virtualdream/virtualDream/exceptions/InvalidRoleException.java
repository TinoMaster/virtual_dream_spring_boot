package com.tinomaster.virtualdream.virtualDream.exceptions;

public class InvalidRoleException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidRoleException(String message) {
		super(message);
	}

	public InvalidRoleException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRoleException(Throwable cause) {
		super(cause);
	}

}
