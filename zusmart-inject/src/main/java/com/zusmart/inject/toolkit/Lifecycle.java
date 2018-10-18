package com.zusmart.inject.toolkit;

import com.zusmart.inject.exception.ContextCreateException;
import com.zusmart.inject.exception.ContextDestoryException;
import com.zusmart.inject.exception.ContextInitialException;
import com.zusmart.inject.exception.ContextInjectException;

/**
 * 框架生命周期
 * 
 * @author koko
 *
 */
public interface Lifecycle{

	/**
	 * 初始化
	 */
	public void initial() throws ContextInitialException;

	/**
	 * 销毁
	 */
	public void destory() throws ContextDestoryException;

	/**
	 * 创建
	 */
	public void create() throws ContextCreateException;

	/**
	 * 注入
	 */
	public void inject() throws ContextInjectException;

}