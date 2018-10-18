package com.zusmart.inject.bean;

import com.zusmart.inject.config.ConfigManager;
import com.zusmart.inject.context.ApplicationContext;
import com.zusmart.inject.plugin.PluginManager;
import com.zusmart.inject.toolkit.Lifecycle;

/**
 * Bean管理工厂
 * 
 * @author koko
 * 
 */
public interface BeanFactory extends Lifecycle, BeanFactoryManager {

	/**
	 * 返回Bean处理器
	 * 
	 * @return
	 */
	public BeanFactoryProcessor getBeanFactoryProcessor();

	public PluginManager getPluginManager();

	public ConfigManager getConfigManager();

	public ApplicationContext getApplicationContext();

}