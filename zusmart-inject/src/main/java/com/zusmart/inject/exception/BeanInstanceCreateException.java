package com.zusmart.inject.exception;

public class BeanInstanceCreateException extends BeanInstanceException {

	private static final long serialVersionUID = -9210666503075054494L;

	public BeanInstanceCreateException() {
		super();
	}

	public BeanInstanceCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanInstanceCreateException(String message) {
		super(message);
	}

	public BeanInstanceCreateException(Throwable cause) {
		super(cause);
	}

}