package com.zusmart.inject.exception;

public class BeanDefinitionReferenceCycleException extends BeanDefinitionException {

	private static final long serialVersionUID = 1L;

	public BeanDefinitionReferenceCycleException() {
		super();
	}

	public BeanDefinitionReferenceCycleException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanDefinitionReferenceCycleException(String message) {
		super(message);
	}

	public BeanDefinitionReferenceCycleException(Throwable cause) {
		super(cause);
	}

}
