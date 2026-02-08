package com.vks.cbp.exception;

public class NoSuchPostOfficeException extends RuntimeException {

	public NoSuchPostOfficeException(String message) {
		super(message);
	}

	public NoSuchPostOfficeException(String message, Throwable cause) {
		super(message, cause);
	}

}
