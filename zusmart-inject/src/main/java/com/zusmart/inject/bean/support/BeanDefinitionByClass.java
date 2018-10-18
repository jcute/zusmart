package com.zusmart.inject.bean.support;

import com.zusmart.inject.bean.BeanDefinitionCreator;
import com.zusmart.inject.bean.BeanDefinitionHandler;
import com.zusmart.inject.bean.BeanFactory;
import com.zusmart.inject.bean.BeanScope;

public class BeanDefinitionByClass extends AbstractBeanDefinition{

	private final BeanDefinitionCreator beanDefinitionCreator;
	private final BeanDefinitionHandler beanDefinitionHandler;

	public BeanDefinitionByClass(BeanFactory beanFactory,Class<?> beanType,String beanName,BeanScope beanScope){
		super(beanFactory,beanType,beanName,beanScope);
		this.beanDefinitionCreator = new BeanDefinitionCreatorByClass(this);
		if(this.isSingleton()){
			this.beanDefinitionHandler = new BeanDefinitionHandlerBySingleton(this,this.beanDefinitionCreator);
		}else{
			this.beanDefinitionHandler = new BeanDefinitionHandlerByPrototype(this,this.beanDefinitionCreator);
		}
	}

	@Override
	public BeanDefinitionCreator getBeanDefinitionCreator(){
		return this.beanDefinitionCreator;
	}

	@Override
	public BeanDefinitionHandler getBeanDefinitionHandler(){
		return this.beanDefinitionHandler;
	}

}