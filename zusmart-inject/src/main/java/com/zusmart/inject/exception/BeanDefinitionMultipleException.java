package com.zusmart.inject.exception;

public class BeanDefinitionMultipleException extends BeanDefinitionException {

	private static final long serialVersionUID = 1L;

	public BeanDefinitionMultipleException() {
		super();
	}

	public BeanDefinitionMultipleException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanDefinitionMultipleException(String message) {
		super(message);
	}

	public BeanDefinitionMultipleException(Throwable cause) {
		super(cause);
	}

}