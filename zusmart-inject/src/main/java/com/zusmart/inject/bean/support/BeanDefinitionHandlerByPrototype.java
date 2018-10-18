package com.zusmart.inject.bean.support;

import java.util.LinkedHashSet;
import java.util.Set;

import com.zusmart.inject.bean.BeanDefinition;
import com.zusmart.inject.bean.BeanDefinitionCreator;
import com.zusmart.inject.bean.BeanFactoryProcessor;
import com.zusmart.inject.exception.BeanInstanceCreateException;
import com.zusmart.inject.exception.BeanInstanceNotFoundException;
import com.zusmart.inject.exception.ContextDestoryException;
import com.zusmart.inject.exception.ContextInitialException;

public class BeanDefinitionHandlerByPrototype extends AbstractBeanDefinitionHandler{

	private BeanFactoryProcessor beanFactoryProcessor;
	private Set<Object> beanInstances;

	public BeanDefinitionHandlerByPrototype(BeanDefinition beanDefinition,BeanDefinitionCreator beanDefinitionCreator){
		super(beanDefinition,beanDefinitionCreator);
	}

	@Override
	protected void doInitial() throws ContextInitialException{
		this.beanInstances = new LinkedHashSet<Object>();
		this.beanFactoryProcessor = this.getBeanDefinition().getBeanFactory().getBeanFactoryProcessor();
	}

	@Override
	protected void doDestory() throws ContextDestoryException{
		if(null != this.beanInstances && this.beanInstances.size() > 0){
			for(Object beanInstance : this.beanInstances){
				this.beanFactoryProcessor.invokeDestoryMethods(beanInstance);
			}
			this.beanInstances.clear();
		}
		this.beanInstances = null;
	}

	@Override
	protected Object doGetBeanInstance() throws BeanInstanceNotFoundException{
		try{
			Object beanInstance = this.getBeanDefinitionCreator().createBeanInstace();
			this.beanFactoryProcessor.injectBeanInstance(beanInstance);
			this.beanFactoryProcessor.invokeInitialMethods(beanInstance);
			this.beanInstances.add(beanInstance);
			return beanInstance;
		}catch(BeanInstanceCreateException e){
			throw new BeanInstanceNotFoundException(e.getMessage());
		}
	}

}