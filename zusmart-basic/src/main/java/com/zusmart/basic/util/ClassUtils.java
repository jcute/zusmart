package com.zusmart.basic.util;


/**
 * Class处理工具类
 */
public abstract class ClassUtils {

	/**
	 * 获取默认的ClassLoader对象
	 * 
	 * @return 返回ClassLoader
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader classLoader = null;
		try {
			classLoader = Thread.currentThread().getContextClassLoader();
		} catch (Exception e) {
		}
		if (null == classLoader) {
			classLoader = ClassUtils.class.getClassLoader();
			if (null == classLoader) {
				try {
					classLoader = ClassLoader.getSystemClassLoader();
				} catch (Exception e) {
				}
			}
		}
		return classLoader;
	}

	/**
	 * 获取指定字符串的class对象
	 * 
	 * @param packageName
	 * @param classLoader
	 * @return
	 */
	public static Class<?> forName(String packageName, ClassLoader classLoader) {
		ClassLoader targetClassLoader = classLoader;
		if (null == targetClassLoader) {
			targetClassLoader = getDefaultClassLoader();
		}
		try {
			return Class.forName(packageName, true, targetClassLoader);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static Class<?> forName(String packageName) {
		return forName(packageName, null);
	}

}