package com.zusmart.inject.bean.support;

import java.util.LinkedHashSet;
import java.util.Set;

import com.zusmart.inject.bean.BeanDefinition;
import com.zusmart.inject.bean.BeanFactory;
import com.zusmart.inject.bean.BeanFactoryProcessor;
import com.zusmart.inject.config.ConfigManager;
import com.zusmart.inject.context.ApplicationContext;
import com.zusmart.inject.exception.BeanDefinitionReferenceCycleException;
import com.zusmart.inject.exception.BeanDefinitionSortException;
import com.zusmart.inject.exception.ContextCreateException;
import com.zusmart.inject.exception.ContextDestoryException;
import com.zusmart.inject.exception.ContextInitialException;
import com.zusmart.inject.exception.ContextInjectException;
import com.zusmart.inject.plugin.PluginManager;

public abstract class AbstractBeanFactory extends AbstractBeanFactoryManager implements BeanFactory{

	private final ApplicationContext applicationContext;
	private final BeanFactoryProcessor beanFactoryProcessor;
	private final PluginManager pluginManager;
	private final ConfigManager configManager;
	private final Set<BeanDefinition> beanDefinitionsSorted;

	public AbstractBeanFactory(ApplicationContext applicationContext){
		if(null == applicationContext){
			throw new IllegalArgumentException("Application context must not be null");
		}
		this.applicationContext = applicationContext;
		this.beanFactoryProcessor = this.createBeanFactoryProcessor();
		this.pluginManager = this.createPluginManager();
		this.configManager = this.createConfigManager();
		this.beanDefinitionsSorted = new LinkedHashSet<BeanDefinition>();
	}

	@Override
	public ApplicationContext getApplicationContext(){
		return this.applicationContext;
	}

	@Override
	public BeanFactoryProcessor getBeanFactoryProcessor(){
		return this.beanFactoryProcessor;
	}

	@Override
	public void initial() throws ContextInitialException{
		try{
			this.beanDefinitionsSorted.clear();
			this.beanDefinitionsSorted.addAll(this.beanFactoryProcessor.getBeanDefinitionsSorted());
			for(BeanDefinition beanDefinition : this.beanDefinitionsSorted){
				beanDefinition.initial();
			}
		}catch(BeanDefinitionReferenceCycleException e){
			throw new ContextInitialException(e.getMessage());
		}catch(BeanDefinitionSortException e){
			throw new ContextInitialException(e.getMessage());
		}
	}

	@Override
	public void destory() throws ContextDestoryException{
		for(BeanDefinition beanDefinition : this.beanDefinitionsSorted){
			beanDefinition.destory();
		}
		this.beanDefinitionsSorted.clear();
		this.clearBeanDefinitions();
	}

	@Override
	public void create() throws ContextCreateException{
		for(BeanDefinition beanDefinition : this.beanDefinitionsSorted){
			beanDefinition.create();
		}
	}

	@Override
	public void inject() throws ContextInjectException{
		for(BeanDefinition beanDefinition : this.beanDefinitionsSorted){
			beanDefinition.inject();
		}
	}

	@Override
	protected BeanFactory getBeanFactory(){
		return this;
	}

	@Override
	public PluginManager getPluginManager(){
		return this.pluginManager;
	}
	
	@Override
	public ConfigManager getConfigManager(){
		return this.configManager;
	}

	protected abstract BeanFactoryProcessor createBeanFactoryProcessor();

	protected abstract PluginManager createPluginManager();

	protected abstract ConfigManager createConfigManager();
	
}