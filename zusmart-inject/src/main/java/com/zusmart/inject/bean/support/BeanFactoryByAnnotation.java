package com.zusmart.inject.bean.support;

import com.zusmart.inject.bean.BeanFactoryProcessor;
import com.zusmart.inject.config.ConfigManager;
import com.zusmart.inject.config.support.ConfigManagerByDefault;
import com.zusmart.inject.context.ApplicationContext;
import com.zusmart.inject.plugin.PluginManager;
import com.zusmart.inject.plugin.support.PluginManagerByAnnotation;

public class BeanFactoryByAnnotation extends AbstractBeanFactory{

	public BeanFactoryByAnnotation(ApplicationContext applicationContext){
		super(applicationContext);
	}

	@Override
	protected PluginManager createPluginManager(){
		return new PluginManagerByAnnotation();
	}
	
	@Override
	protected ConfigManager createConfigManager(){
		return new ConfigManagerByDefault();
	}
	
	@Override
	protected BeanFactoryProcessor createBeanFactoryProcessor(){
		return new BeanFactoryProcessorByAnnotation(this);
	}
	
}