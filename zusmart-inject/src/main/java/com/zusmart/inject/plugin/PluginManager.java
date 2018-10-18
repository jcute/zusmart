package com.zusmart.inject.plugin;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.zusmart.inject.exception.ContextCreateException;
import com.zusmart.inject.exception.ContextDestoryException;
import com.zusmart.inject.exception.ContextInitialException;
import com.zusmart.inject.exception.ContextInjectException;
import com.zusmart.inject.scanner.PackageScanner;

/**
 * 插件管理器
 * 
 * @author tangbin
 *
 */
public interface PluginManager{

	/**
	 * 注册插件
	 * 
	 * @param plugin
	 */
	public void addPlugin(Plugin plugin);

	/**
	 * 返回所有插件
	 * 
	 * @return
	 */
	public List<Plugin> getAllPlugins();

	/**
	 * 查询指定class的插件信息
	 * 
	 * @param target
	 * @return
	 */
	public Map<Annotation,Class<? extends Plugin>> searchPlugins(Class<?> target);

	public void onBeforeScanning(PackageScanner packageScanner) throws ContextInitialException;

	public void onBeforeInitial() throws ContextInitialException;

	public void onBeforeCreate() throws ContextCreateException;

	public void onBeforeInject() throws ContextInjectException;

	public void onAfterInject() throws ContextInjectException;

	public void onBeforeDestory() throws ContextDestoryException;

}