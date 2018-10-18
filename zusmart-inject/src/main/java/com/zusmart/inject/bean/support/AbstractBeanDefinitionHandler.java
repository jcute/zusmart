package com.zusmart.inject.bean.support;

import com.zusmart.inject.bean.BeanDefinition;
import com.zusmart.inject.bean.BeanDefinitionCreator;
import com.zusmart.inject.bean.BeanDefinitionHandler;
import com.zusmart.inject.exception.BeanInstanceNotFoundException;
import com.zusmart.inject.exception.ContextCreateException;
import com.zusmart.inject.exception.ContextDestoryException;
import com.zusmart.inject.exception.ContextInitialException;
import com.zusmart.inject.exception.ContextInjectException;

public abstract class AbstractBeanDefinitionHandler implements BeanDefinitionHandler{

	private final BeanDefinition beanDefinition;
	private final BeanDefinitionCreator beanDefinitionCreator;

	public AbstractBeanDefinitionHandler(BeanDefinition beanDefinition,BeanDefinitionCreator beanDefinitionCreator){
		if(null == beanDefinition){
			throw new IllegalArgumentException("Bean definition must not be null");
		}
		if(null == beanDefinitionCreator){
			throw new IllegalArgumentException("Bean definition creator must not be null");
		}
		this.beanDefinition = beanDefinition;
		this.beanDefinitionCreator = beanDefinitionCreator;
	}

	@Override
	public Object getBeanInstance() throws BeanInstanceNotFoundException{
		Object beanInstance = this.doGetBeanInstance();
		if(null == beanInstance){// 此处判断是否为空,避免因用户实现导致返回Null的情况
			throw new BeanInstanceNotFoundException(this.beanDefinition.toString());
		}
		return beanInstance;
	}

	@Override
	public BeanDefinitionCreator getBeanDefinitionCreator(){
		return this.beanDefinitionCreator;
	}

	@Override
	public BeanDefinition getBeanDefinition(){
		return this.beanDefinition;
	}

	@Override
	public void initial() throws ContextInitialException{
		this.doInitial();
	}

	@Override
	public void destory() throws ContextDestoryException{
		this.doDestory();
	}

	@Override
	public void create() throws ContextCreateException{
		this.doCreate();
	}

	@Override
	public void inject() throws ContextInjectException{
		this.doInject();
	}

	// 对接口方法进行抽象实现,子类包装
	protected void doInitial() throws ContextInitialException{
	}

	protected void doDestory() throws ContextDestoryException{
	}

	protected void doCreate() throws ContextCreateException{
	}

	protected void doInject() throws ContextInjectException{
	}

	protected abstract Object doGetBeanInstance() throws BeanInstanceNotFoundException;

}