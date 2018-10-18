package com.zusmart.inject.exception;

public class BeanInstanceException extends InjectException {

	private static final long serialVersionUID = -5754066868748701376L;

	public BeanInstanceException() {
		super();
	}

	public BeanInstanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanInstanceException(String message) {
		super(message);
	}

	public BeanInstanceException(Throwable cause) {
		super(cause);
	}

}