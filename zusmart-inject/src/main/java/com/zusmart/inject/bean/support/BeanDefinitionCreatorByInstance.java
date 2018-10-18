package com.zusmart.inject.bean.support;

import com.zusmart.inject.bean.BeanDefinition;
import com.zusmart.inject.bean.BeanFactoryProcessor;
import com.zusmart.inject.exception.BeanInstanceCreateException;
import com.zusmart.inject.proxy.ProxyManager;

/**
 * BeanCreator实现,通过指定的instance创建Bean实例
 * 
 * @author koko
 * 
 */
public class BeanDefinitionCreatorByInstance extends AbstractBeanDefinitionCreator {

	private final Object beanInstance;

	public BeanDefinitionCreatorByInstance(BeanDefinition beanDefinition, Object beanInstance) {
		super(beanDefinition);
		if (null == beanInstance) {
			throw new IllegalArgumentException("Bean instance must not be null");
		}
		this.beanInstance = beanInstance;
	}

	@Override
	protected Object doCreateBeanInstance() throws BeanInstanceCreateException {

		BeanDefinition beanDefinition = this.getBeanDefinition();
		Class<?> beanType = beanDefinition.getBeanType();
		BeanFactoryProcessor beanFactoryProcessor = beanDefinition.getBeanFactory().getBeanFactoryProcessor();
		BeanDefinition[] interceptors = beanFactoryProcessor.searchInterceptor(beanType);
		if (null == interceptors || interceptors.length == 0) {
			return this.beanInstance;
		} else {
			return ProxyManager.createProxy(this.beanInstance, interceptors);
		}

	}

}