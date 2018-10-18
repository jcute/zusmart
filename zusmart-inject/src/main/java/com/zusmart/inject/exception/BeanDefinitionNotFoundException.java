package com.zusmart.inject.exception;

public class BeanDefinitionNotFoundException extends BeanDefinitionException {

	private static final long serialVersionUID = 1L;

	public BeanDefinitionNotFoundException() {
		super();
	}

	public BeanDefinitionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanDefinitionNotFoundException(String message) {
		super(message);
	}

	public BeanDefinitionNotFoundException(Throwable cause) {
		super(cause);
	}

}