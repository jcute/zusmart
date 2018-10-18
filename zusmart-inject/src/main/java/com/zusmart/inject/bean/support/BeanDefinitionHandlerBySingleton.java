package com.zusmart.inject.bean.support;

import com.zusmart.inject.bean.BeanDefinition;
import com.zusmart.inject.bean.BeanDefinitionCreator;
import com.zusmart.inject.bean.BeanFactoryProcessor;
import com.zusmart.inject.exception.BeanInstanceCreateException;
import com.zusmart.inject.exception.BeanInstanceNotFoundException;
import com.zusmart.inject.exception.ContextCreateException;
import com.zusmart.inject.exception.ContextDestoryException;
import com.zusmart.inject.exception.ContextInitialException;
import com.zusmart.inject.exception.ContextInjectException;

public class BeanDefinitionHandlerBySingleton extends AbstractBeanDefinitionHandler{

	private BeanFactoryProcessor beanFactoryProcessor;
	private Object beanInstance;

	public BeanDefinitionHandlerBySingleton(BeanDefinition beanDefinition,BeanDefinitionCreator beanDefinitionCreator){
		super(beanDefinition,beanDefinitionCreator);
	}

	@Override
	protected void doInitial() throws ContextInitialException{
		this.beanFactoryProcessor = this.getBeanDefinition().getBeanFactory().getBeanFactoryProcessor();
	}

	@Override
	protected void doCreate() throws ContextCreateException{
		try{
			this.beanInstance = this.getBeanDefinitionCreator().createBeanInstace();
		}catch(BeanInstanceCreateException e){
			throw new ContextCreateException(e.getMessage());
		}
	}

	@Override
	protected void doInject() throws ContextInjectException{
		this.beanFactoryProcessor.injectBeanInstance(this.beanInstance);
		this.beanFactoryProcessor.invokeInitialMethods(this.beanInstance);
	}

	@Override
	protected void doDestory() throws ContextDestoryException{
		this.beanFactoryProcessor.invokeDestoryMethods(this.beanInstance);
		this.beanInstance = null;
	}

	@Override
	protected Object doGetBeanInstance() throws BeanInstanceNotFoundException{
		return this.beanInstance;
	}

}