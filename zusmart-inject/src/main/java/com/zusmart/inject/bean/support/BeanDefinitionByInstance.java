package com.zusmart.inject.bean.support;

import com.zusmart.inject.bean.BeanDefinitionCreator;
import com.zusmart.inject.bean.BeanDefinitionHandler;
import com.zusmart.inject.bean.BeanFactory;
import com.zusmart.inject.bean.BeanScope;

public class BeanDefinitionByInstance extends AbstractBeanDefinition{

	private final BeanDefinitionCreator beanDefinitionCreator;
	private final BeanDefinitionHandler beanDefinitionHandler;

	public BeanDefinitionByInstance(BeanFactory beanFactory,Class<?> beanType,String beanName,BeanScope beanScope,Object beanInstance){
		super(beanFactory,beanType,beanName,beanScope);
		if(null == beanInstance){
			throw new IllegalArgumentException("Bean instance must not be null");
		}
		this.beanDefinitionCreator = new BeanDefinitionCreatorByInstance(this,beanInstance);
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