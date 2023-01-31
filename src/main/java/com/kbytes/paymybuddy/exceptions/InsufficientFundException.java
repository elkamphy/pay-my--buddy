package com.kbytes.paymybuddy.exceptions;

public class InsufficientFundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientFundException(String message) {
		super(message);
	}
}
