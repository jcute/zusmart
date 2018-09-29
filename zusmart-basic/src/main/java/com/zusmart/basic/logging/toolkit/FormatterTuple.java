package com.zusmart.basic.logging.toolkit;

public class FormatterTuple {

	private final String message;
	private final Throwable throwable;

	public FormatterTuple(String message, Throwable throwable) {
		this.message = message;
		this.throwable = throwable;
	}

	public String getMessage() {
		return message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

}