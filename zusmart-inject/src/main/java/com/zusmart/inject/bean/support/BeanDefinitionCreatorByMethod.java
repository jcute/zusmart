package com.zusmart.inject.bean.support;

import java.lang.reflect.Method;

import com.zusmart.inject.bean.BeanDefinition;
import com.zusmart.inject.bean.BeanFactoryProcessor;
import com.zusmart.inject.exception.BeanInstanceCreateException;
import com.zusmart.inject.proxy.ProxyManager;
import com.zusmart.inject.util.ReflectionUtils;

/**
 * BeanCreator实现,通过指定的method创建Bean实例
 * 
 * @author koko
 * 
 */
public class BeanDefinitionCreatorByMethod extends AbstractBeanDefinitionCreator {

	private final BeanDefinition parentBeanDefinition;
	private final Method beanCreateMethod;

	/**
	 * 在构造函数中需要指定method对相Bean定义,后续创建需要使用,Method也必须传入,需要在创建时通过反射调用
	 * 
	 * @param beanDefinition
	 * @param parentBeanDefinition
	 * @param beanCreateMethod
	 */
	public BeanDefinitionCreatorByMethod(BeanDefinition beanDefinition, BeanDefinition parentBeanDefinition, Method beanCreateMethod) {
		super(beanDefinition);
		if (null == parentBeanDefinition) {
			throw new IllegalArgumentException("Parent bean definition must not be null");
		}
		if (null == beanCreateMethod) {
			throw new IllegalArgumentException("Bean create method must not be null");
		}
		this.parentBeanDefinition = parentBeanDefinition;
		this.beanCreateMethod = beanCreateMethod;
	}

	public final BeanDefinition getParentBeanDefinition() {
		return this.parentBeanDefinition;
	}

	public final Method getBeanCreateMethod() {
		return this.beanCreateMethod;
	}

	@Override
	protected Object doCreateBeanInstance() throws BeanInstanceCreateException {
		try {

			BeanDefinition beanDefinition = this.getBeanDefinition();
			Class<?> beanType = beanDefinition.getBeanType();
			BeanFactoryProcessor beanFactoryProcessor = beanDefinition.getBeanFactory().getBeanFactoryProcessor();
			BeanDefinition[] interceptors = beanFactoryProcessor.searchInterceptor(beanType);
			if (null == interceptors || interceptors.length == 0) {
				Object[] arguments = beanFactoryProcessor.getMethodArguments(this.beanCreateMethod);
				ReflectionUtils.makeAccessible(this.beanCreateMethod);
				return ReflectionUtils.invokeMethod(this.beanCreateMethod, this.parentBeanDefinition.getBeanInstance(), arguments);
			} else {
				return ProxyManager.createProxy(beanType, interceptors);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanInstanceCreateException(e.getMessage());
		}
	}

}