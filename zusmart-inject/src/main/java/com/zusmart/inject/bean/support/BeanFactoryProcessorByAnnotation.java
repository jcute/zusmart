package com.zusmart.inject.bean.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.basic.util.AnnotationUtils;
import com.zusmart.basic.util.StringUtils;
import com.zusmart.inject.annotation.Autowired;
import com.zusmart.inject.annotation.Component;
import com.zusmart.inject.annotation.ComponentScan;
import com.zusmart.inject.annotation.Configuration;
import com.zusmart.inject.annotation.Destory;
import com.zusmart.inject.annotation.ImportConfig;
import com.zusmart.inject.annotation.Initial;
import com.zusmart.inject.annotation.Interceptor;
import com.zusmart.inject.annotation.Order;
import com.zusmart.inject.annotation.Property;
import com.zusmart.inject.bean.BeanDefinition;
import com.zusmart.inject.bean.BeanFactory;
import com.zusmart.inject.bean.BeanScope;
import com.zusmart.inject.config.ConfigManager;
import com.zusmart.inject.context.ApplicationContext;
import com.zusmart.inject.exception.BeanDefinitionMultipleException;
import com.zusmart.inject.exception.BeanDefinitionNotFoundException;
import com.zusmart.inject.plugin.Plugin;
import com.zusmart.inject.plugin.PluginManager;
import com.zusmart.inject.resource.Resource;
import com.zusmart.inject.scanner.PackageScanner;
import com.zusmart.inject.scanner.PackageScannerFilter;
import com.zusmart.inject.scanner.support.DefaultPackageScanner;
import com.zusmart.inject.toolkit.DirectedGraph;
import com.zusmart.inject.toolkit.PathMatcher;
import com.zusmart.inject.toolkit.support.DefaultPathMatcher;
import com.zusmart.inject.util.ReflectionUtils;
import com.zusmart.inject.util.ResourceUtils;

public class BeanFactoryProcessorByAnnotation extends AbstractBeanFactoryProcessor implements Comparator<Method>, PackageScannerFilter {

	private static final Logger logger = LoggerFactory.getLogger(BeanFactoryProcessorByAnnotation.class);

	private Map<String, Set<String>> cachePatterns = new HashMap<String, Set<String>>();
	private PathMatcher pathMatcher = new DefaultPathMatcher(".");
	private ApplicationContext applicationContext;

	public BeanFactoryProcessorByAnnotation(BeanFactory beanFactory) {
		super(beanFactory);
		this.applicationContext = beanFactory.getApplicationContext();
	}

	/**
	 * 发现参数含有@Autowired的参数则从BeanFactory寻找对象
	 */
	@Override
	protected Object[] getArguments(Class<?>[] parameterTypes, Annotation[][] annotations) throws BeanDefinitionNotFoundException, BeanDefinitionMultipleException {
		if (null == parameterTypes || parameterTypes.length == 0) {
			return new Object[0];
		}
		BeanFactory beanFactory = this.getBeanFactory();
		ConfigManager configManager = beanFactory.getConfigManager();
		Annotation[] autowiredAnnotations = AnnotationUtils.getAnnotation(parameterTypes, annotations, Autowired.class);
		Annotation[] propertyAnnotations = AnnotationUtils.getAnnotation(parameterTypes, annotations, Property.class);
		Object[] result = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> parameterType = parameterTypes[i];
			Annotation autowiredAnnotation = autowiredAnnotations[i];
			Annotation propertyAnnotation = propertyAnnotations[i];
			if (null != autowiredAnnotation) {
				Autowired autowired = (Autowired) autowiredAnnotation;
				String beanName = autowired.value();
				Class<?> beanType = parameterTypes[i];
				result[i] = beanFactory.getBean(beanType, beanName);
			} else if (null != propertyAnnotation) {
				Property property = (Property) propertyAnnotation;
				result[i] = configManager.getConfigValue(property.value(), parameterType);
			} else {
				result[i] = null;
			}
		}
		return result;
	}

	/**
	 * 选举构造函数
	 * <ul>
	 * <li>1.寻找@Initial注解的构造函数</li>
	 * <li>2.寻找参数包含@Autowired注解的构造函数</li>
	 * <li>3.使用构造函数列表中的第0个位置</li>
	 * </ul>
	 */
	@Override
	protected Constructor<?> getConstructor(Constructor<?>[] constructors) {
		Constructor<?> constructor = null;
		for (int i = 0; i < constructors.length; i++) {
			Constructor<?> tempConstructor = constructors[i];
			if (AnnotationUtils.hasAnnotation(tempConstructor, Initial.class)) {
				constructor = tempConstructor;
				break;
			}
		}
		if (null == constructor) {
			for (int i = 0; i < constructors.length; i++) {
				Constructor<?> tempConstructor = constructors[i];
				if (AnnotationUtils.hasAnnotation(tempConstructor.getParameterAnnotations(), Autowired.class)) {
					constructor = tempConstructor;
					break;
				}
			}
		}
		if (null == constructor) {
			constructor = constructors[0];
		}
		return constructor;
	}

	@Override
	protected boolean isComponentClass(Class<?> beanType) {
		return AnnotationUtils.hasAnnotation(beanType, Component.class);
	}

	@Override
	public boolean isComponent(Method method) {
		if (null == method) {
			return false;
		}
		if (!AnnotationUtils.hasAnnotation(method, Component.class)) {
			return false;
		}
		if (method.getReturnType().equals(Void.TYPE)) {
			return false;
		}
		if (method.getReturnType().isPrimitive()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isConfiguration(Class<?> beanType) {
		return AnnotationUtils.hasAnnotation(beanType, Configuration.class);
	}

	@Override
	public void injectBeanInstance(Object beanInstance) {
		BeanFactory beanFactory = this.getBeanFactory();
		ConfigManager configManager = beanFactory.getConfigManager();

		Class<?> beanInstanceType = beanInstance.getClass();
		Field[] fields = ReflectionUtils.getDeclaredFields(beanInstanceType);
		if (null != fields && fields.length > 0) {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (AnnotationUtils.hasAnnotation(field, Autowired.class) && !field.getType().isPrimitive()) {
					Autowired autowired = AnnotationUtils.getAnnotation(field, Autowired.class);
					Class<?> beanType = field.getType();
					String beanName = autowired.value();
					try {
						ReflectionUtils.makeAccessible(field);
						field.set(beanInstance, beanFactory.getBean(beanType, beanName));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (AnnotationUtils.hasAnnotation(field, Property.class)) {
					Property property = AnnotationUtils.getAnnotation(field, Property.class);
					if (StringUtils.isNotBlank(property.value())) {
						try {
							Object fieldValue = configManager.getConfigValue(property.value(), field.getType());
							ReflectionUtils.makeAccessible(field);
							field.set(beanInstance, fieldValue);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		Method[] methods = ReflectionUtils.getDeclaredMethods(beanInstanceType);
		if (null != methods && methods.length > 0) {
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (AnnotationUtils.hasAnnotation(method, Autowired.class) || AnnotationUtils.hasAnnotation(method, Property.class)) {
					try {
						Object[] parameterDatas = this.getMethodArguments(method);
						ReflectionUtils.makeAccessible(method);
						ReflectionUtils.invokeMethod(method, beanInstance, parameterDatas);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected Method[] getInitialMethods(Object beanInstance) {
		Class<?> beanType = beanInstance.getClass();
		List<Method> result = new ArrayList<Method>();
		Method[] methods = ReflectionUtils.getDeclaredMethods(beanType);
		if (null == methods || methods.length == 0) {
			return new Method[0];
		}
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (!AnnotationUtils.hasAnnotation(method, Initial.class)) {
				continue;
			}
			result.add(method);
		}
		Collections.sort(result, this);
		return result.toArray(new Method[result.size()]);
	}

	@Override
	protected Method[] getDestoryMethods(Object beanInstance) {
		Class<?> beanType = beanInstance.getClass();
		List<Method> result = new ArrayList<Method>();
		Method[] methods = ReflectionUtils.getDeclaredMethods(beanType);
		if (null == methods || methods.length == 0) {
			return new Method[0];
		}
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (!AnnotationUtils.hasAnnotation(method, Destory.class)) {
				continue;
			}
			result.add(method);
		}
		Collections.sort(result, this);
		return result.toArray(new Method[result.size()]);
	}

	@Override
	public int compare(Method o1, Method o2) {
		int s1 = 0;
		int s2 = 0;
		if (AnnotationUtils.hasAnnotation(o1, Order.class)) {
			s1 = o1.getAnnotation(Order.class).value();
		}
		if (AnnotationUtils.hasAnnotation(o2, Order.class)) {
			s2 = o2.getAnnotation(Order.class).value();
		}
		return s1 - s2;
	}

	@Override
	protected Set<String> doSearchScanPattern(String root) {
		if (this.cachePatterns.containsKey(root)) {
			return this.cachePatterns.get(root);
		}
		Set<String> patterns = new LinkedHashSet<String>();
		this.doSearchScanPattern(root, patterns);
		if (null != patterns && patterns.size() > 0) {
			this.cachePatterns.put(root, patterns);
		}
		return patterns;
	}

	@Override
	protected BeanDefinition[] doSearchInterceptor(Class<?> beanType) {
		BeanFactory beanFactory = this.getBeanFactory();
		List<BeanDefinition> result = new ArrayList<BeanDefinition>();
		Map<String, BeanDefinition> mappings = beanFactory.getAllBeanDefinitions();
		for (Entry<String, BeanDefinition> entry : mappings.entrySet()) {
			BeanDefinition beanDefinition = entry.getValue();
			Class<?> interceptorBeanType = beanDefinition.getBeanType();
			if (!AnnotationUtils.hasAnnotation(interceptorBeanType, Interceptor.class)) {
				continue;
			}
			if (!Proxy.class.isAssignableFrom(interceptorBeanType)) {
				continue;
			}
			Interceptor interceptor = AnnotationUtils.getAnnotation(interceptorBeanType, Interceptor.class);
			if (!this.isInterceptTargetClass(interceptor, beanType, interceptorBeanType)) {
				continue;
			}
			result.add(beanDefinition);
		}
		if (null != result && result.size() > 0) {
			logger.debug("Found interceptos {} -> {}", beanType.getName(), result);
		}
		return result.toArray(new BeanDefinition[result.size()]);
	}

	@Override
	protected void processDirectedGraph(BeanDefinition beanDefinition, DirectedGraph<BeanDefinition> directedGraph) throws BeanDefinitionNotFoundException, BeanDefinitionMultipleException {
		BeanFactory beanFactory = this.getBeanFactory();
		Class<?> beanType = beanDefinition.getBeanType();
		if (AnnotationUtils.hasAnnotation(beanType, Configuration.class)) {
			Configuration configuration = AnnotationUtils.getAnnotation(beanType, Configuration.class);
			ImportConfig[] configs = configuration.configs();
			if (null != configs && configs.length > 0) {
				ConfigManager configManager = beanFactory.getConfigManager();
				for (int i = 0; i < configs.length; i++) {
					ImportConfig config = configs[i];
					Resource resource = ResourceUtils.getResource(config.value());
					configManager.addConfig(resource, config.type());
					logger.debug("Add config [{}]{}", config.type(), config.value());
				}
			}
		}

		if (beanDefinition instanceof BeanDefinitionByMethod) {
			BeanDefinitionByMethod beanDefinitionByMethod = (BeanDefinitionByMethod) beanDefinition;
			BeanDefinition parentBeanDefinition = beanDefinitionByMethod.getParentBeanDefinition();
			Method method = beanDefinitionByMethod.getBeanCreateMethod();
			directedGraph.addEdge(beanDefinition, parentBeanDefinition);// 添加父bean的依赖
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (null == parameterTypes || parameterTypes.length == 0) {
				directedGraph.addNode(beanDefinition);
				return;
			}
			// 添加方法注入依赖
			Annotation[] annotations = AnnotationUtils.getAnnotation(parameterTypes, method.getParameterAnnotations(), Autowired.class);
			for (int i = 0; i < parameterTypes.length; i++) {
				if (null == annotations[i]) {
					continue;
				}
				Class<?> parameterType = parameterTypes[i];
				Autowired autowired = (Autowired) annotations[i];
				String beanName = autowired.value();
				BeanDefinition dependencyBeanDefinition = beanFactory.getBeanDefinition(parameterType, beanName);
				directedGraph.addEdge(beanDefinition, dependencyBeanDefinition);
			}
		} else if (beanDefinition instanceof BeanDefinitionByInstance) {
			directedGraph.addNode(beanDefinition);
		} else if (beanDefinition instanceof BeanDefinitionByClass) {
			Constructor<?> beanConstructor = this.getBeanConstructor(beanDefinition.getBeanType());
			Class<?>[] parameterTypes = beanConstructor.getParameterTypes();
			if (null == parameterTypes || parameterTypes.length == 0) {
				directedGraph.addNode(beanDefinition);
				return;
			}
			// 添加构造函数依赖
			Annotation[] annotations = AnnotationUtils.getAnnotation(parameterTypes, beanConstructor.getParameterAnnotations(), Autowired.class);
			for (int i = 0; i < parameterTypes.length; i++) {
				if (null == annotations[i]) {
					continue;
				}
				Class<?> parameterType = parameterTypes[i];
				Autowired autowired = (Autowired) annotations[i];
				String beanName = autowired.value();
				BeanDefinition dependencyBeanDefinition = beanFactory.getBeanDefinition(parameterType, beanName);
				directedGraph.addEdge(beanDefinition, dependencyBeanDefinition);
			}
		}
	}

	@Override
	public BeanDefinition createBeanDefinition(Class<?> target) {
		String beanName = null;
		BeanScope beanScope = null;
		if (AnnotationUtils.hasAnnotation(target, Component.class)) {
			Component component = AnnotationUtils.getAnnotation(target, Component.class);
			beanName = component.value();
			beanScope = component.scope();
		}
		BeanFactory beanFactory = this.getBeanFactory();
		BeanDefinition beanDefinition = new BeanDefinitionByClass(beanFactory, target, beanName, beanScope);
		return beanDefinition;
	}

	@Override
	public boolean doFilter(Class<?> target) {
		return AnnotationUtils.hasAnnotation(target, Configuration.class) || AnnotationUtils.hasAnnotation(target, Component.class);
	}

	@Override
	public String resolveBeanName(Method method) {
		String beanName = method.getName();
		if (AnnotationUtils.hasAnnotation(method, Component.class)) {
			Component component = AnnotationUtils.getAnnotation(method, Component.class);
			if (StringUtils.isNotBlank(component.value())) {
				beanName = component.value();
			}
		}
		return beanName;
	}

	@Override
	public BeanScope resolveBeanScope(Method method) {
		BeanScope beanScope = BeanScope.Singleton;
		if (AnnotationUtils.hasAnnotation(method, Component.class)) {
			Component component = AnnotationUtils.getAnnotation(method, Component.class);
			if (StringUtils.isNotBlank(component.value())) {
				beanScope = component.scope();
			}
		}
		return beanScope;
	}

	/**
	 * 判断指定的拦截器注解,是否拦截指定的class
	 * 
	 * @param interceptor
	 * @param beanType
	 * @return true为拦截
	 */
	private boolean isInterceptTargetClass(Interceptor interceptor, Class<?> beanType, Class<?> interceptorType) {
		if (beanType.equals(interceptorType)) {// 剔除掉当前类,代理当前类的情况
			return false;
		}
		String[] packages = interceptor.packages();
		Class<? extends Annotation>[] annotations = interceptor.annotations();
		String className = beanType.getName();
		if (null != packages && packages.length > 0) {
			for (int i = 0; i < packages.length; i++) {
				String packagePattern = packages[i];
				if (this.pathMatcher.isPattern(packagePattern)) {
					if (this.pathMatcher.match(packagePattern, className)) {
						return true;
					}
				} else {
					if (packagePattern.equals(className)) {
						return true;
					}
				}
			}
		}
		if (null != annotations && annotations.length > 0) {
			for (int i = 0; i < annotations.length; i++) {
				Class<? extends Annotation> annotation = annotations[i];
				if (AnnotationUtils.hasAnnotation(beanType, annotation)) {
					return true;
				}
			}
		}
		return false;
	}

	private void doSearchScanPattern(String root, Set<String> patterns) {
		PackageScanner packageScanner = new DefaultPackageScanner();
		packageScanner.addPattern(root);
		packageScanner.addPackageScannerFilter(this);
		for (Class<?> clazz : packageScanner.scan()) {
			if (AnnotationUtils.hasAnnotation(clazz, ComponentScan.class)) {
				ComponentScan componentScan = AnnotationUtils.getAnnotation(clazz, ComponentScan.class);
				String[] tempPatterns = componentScan.value();
				if (null == tempPatterns || tempPatterns.length == 0) {
					continue;
				}
				for (int i = 0; i < tempPatterns.length; i++) {
					String tempPattern = tempPatterns[i];
					if (StringUtils.isBlank(tempPattern)) {
						continue;
					}
					if (patterns.contains(tempPattern)) {
						continue;
					}
					patterns.add(tempPattern);
					this.doSearchScanPattern(tempPattern, patterns);
				}
			}
			this.initPlugin(clazz);
		}
	}

	private void initPlugin(Class<?> clazz) {
		PluginManager pluginManager = this.getBeanFactory().getPluginManager();
		Map<Annotation, Class<? extends Plugin>> mapping = pluginManager.searchPlugins(clazz);
		if (null == mapping || mapping.size() == 0) {
			return;
		}
		for (Entry<Annotation, Class<? extends Plugin>> entry : mapping.entrySet()) {
			try {
				Class<? extends Plugin> plugin = entry.getValue();
				Constructor<?> constructor = plugin.getConstructor(new Class<?>[] { ApplicationContext.class, Annotation.class });
				Object instance = constructor.newInstance(new Object[] { this.applicationContext, entry.getKey() });
				pluginManager.addPlugin((Plugin) instance);
				logger.debug("Create plugin success @{} -> {}", entry.getKey().annotationType().getName(), entry.getValue().getName());
			} catch (Throwable t) {
				logger.warn("Create plugin failed @{} -> {}", entry.getKey().annotationType().getName(), entry.getValue().getName());
			}
		}
	}

}