package com.zusmart.inject.bean.support;

import java.lang.reflect.Constructor;

import com.zusmart.inject.bean.BeanDefinition;
import com.zusmart.inject.bean.BeanFactoryProcessor;
import com.zusmart.inject.exception.BeanInstanceCreateException;
import com.zusmart.inject.proxy.ProxyManager;
import com.zusmart.inject.util.ReflectionUtils;

/**
 * BeanCreator实现,通过指定的class创建Bean实例
 * 
 * @author koko
 * 
 */
public class BeanDefinitionCreatorByClass extends AbstractBeanDefinitionCreator {

	public BeanDefinitionCreatorByClass(BeanDefinition beanDefinition) {
		super(beanDefinition);
	}

	@Override
	protected Object doCreateBeanInstance() throws BeanInstanceCreateException {
		try {
			BeanDefinition beanDefinition = this.getBeanDefinition();
			Class<?> beanType = beanDefinition.getBeanType();
			BeanFactoryProcessor beanFactoryProcessor = beanDefinition.getBeanFactory().getBeanFactoryProcessor();
			BeanDefinition[] interceptors = beanFactoryProcessor.searchInterceptor(beanType);
			Constructor<?> constructor = beanFactoryProcessor.getBeanConstructor(beanType);
			Object[] arguments = beanFactoryProcessor.getConstructorArguments(constructor);
			if (null == interceptors || interceptors.length == 0) {
				return ReflectionUtils.invokeConstructor(constructor, arguments);
			} else {
				return ProxyManager.createProxy(beanType, interceptors, constructor.getParameterTypes(), arguments);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanInstanceCreateException(e.getMessage());
		}
	}

}