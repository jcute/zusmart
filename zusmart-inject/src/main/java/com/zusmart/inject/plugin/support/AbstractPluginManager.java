package com.zusmart.inject.plugin.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.zusmart.basic.util.AnnotationUtils;
import com.zusmart.inject.annotation.Order;
import com.zusmart.inject.exception.ContextCreateException;
import com.zusmart.inject.exception.ContextDestoryException;
import com.zusmart.inject.exception.ContextInitialException;
import com.zusmart.inject.exception.ContextInjectException;
import com.zusmart.inject.plugin.Plugin;
import com.zusmart.inject.plugin.PluginManager;
import com.zusmart.inject.scanner.PackageScanner;

public abstract class AbstractPluginManager implements PluginManager, Comparator<Plugin> {

	private List<Plugin> plugins = new ArrayList<Plugin>();

	@Override
	public void addPlugin(Plugin plugin) {
		if (null == plugin) {
			throw new IllegalArgumentException("Plugin must not be null");
		}
		if (this.plugins.contains(plugin)) {
			return;
		}
		this.plugins.add(plugin);
		Collections.sort(this.plugins, this);
	}

	@Override
	public List<Plugin> getAllPlugins() {
		return Collections.unmodifiableList(this.plugins);
	}

	@Override
	public void onBeforeScanning(PackageScanner packageScanner) throws ContextInitialException {
		for (Plugin plugin : this.getAllPlugins()) {
			plugin.onBeforeScanning(packageScanner);
		}
	}

	@Override
	public void onBeforeInitial() throws ContextInitialException {
		for (Plugin plugin : this.getAllPlugins()) {
			plugin.onBeforeInitial();
		}
	}

	@Override
	public void onBeforeCreate() throws ContextCreateException {
		for (Plugin plugin : this.getAllPlugins()) {
			plugin.onBeforeCreate();
		}
	}

	@Override
	public void onBeforeInject() throws ContextInjectException {
		for (Plugin plugin : this.getAllPlugins()) {
			plugin.onBeforeInject();
		}
	}

	@Override
	public void onAfterInject() throws ContextInjectException {
		for (Plugin plugin : this.getAllPlugins()) {
			plugin.onAfterInject();
		}
	}

	@Override
	public void onBeforeDestory() throws ContextDestoryException {
		for (Plugin plugin : this.getAllPlugins()) {
			plugin.onBeforeDestory();
		}
	}

	@Override
	public int compare(Plugin o1, Plugin o2) {
		Class<?> c1 = o1.getClass();
		Class<?> c2 = o2.getClass();
		int p1 = 0;
		int p2 = 0;
		if (AnnotationUtils.hasAnnotation(c1, Order.class)) {
			p1 = AnnotationUtils.getAnnotation(c1, Order.class).value();
		}
		if (AnnotationUtils.hasAnnotation(c2, Order.class)) {
			p2 = AnnotationUtils.getAnnotation(c2, Order.class).value();
		}
		return p1 - p2;
	}

}