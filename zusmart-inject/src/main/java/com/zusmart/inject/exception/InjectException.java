package com.zusmart.inject.exception;

public class InjectException extends Exception {

	private static final long serialVersionUID = 2982822617807458785L;

	public InjectException() {
		super();
	}

	public InjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public InjectException(String message) {
		super(message);
	}

	public InjectException(Throwable cause) {
		super(cause);
	}

}