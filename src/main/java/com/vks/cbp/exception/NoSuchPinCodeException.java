package com.vks.cbp.exception;

public class NoSuchPinCodeException extends RuntimeException {

	public NoSuchPinCodeException(String message) {
		super(message);
	}

	public NoSuchPinCodeException(String message, Throwable cause) {
		super(message, cause);
	}

}
