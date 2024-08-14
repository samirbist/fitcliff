package com.gym.fitcliff.exception;

import lombok.Getter;

@Getter
public class CustomerException extends RuntimeException {

	/** */
	private static final long serialVersionUID = 1L;

	private String customerName;

	public CustomerException(String message, String customerName) {
		super(message);
		this.customerName = customerName;
	}
}
