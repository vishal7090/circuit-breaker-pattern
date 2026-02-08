package com.vks.cbp.exception;

public class InvalidPinCodeException extends RuntimeException {

	public InvalidPinCodeException(String message) {
		super(message);
	}

	public InvalidPinCodeException(String message, Throwable cause) {
		super(message, cause);
	}

}
