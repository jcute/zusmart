package com.zusmart.inject.exception;

public class BeanDefinitionException extends InjectException {

	private static final long serialVersionUID = -3924685159634952876L;

	public BeanDefinitionException() {
		super();
	}

	public BeanDefinitionException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanDefinitionException(String message) {
		super(message);
	}

	public BeanDefinitionException(Throwable cause) {
		super(cause);
	}

}