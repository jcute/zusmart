package com.zusmart.inject.resource.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.zusmart.basic.util.ClassUtils;
import com.zusmart.basic.util.StringUtils;
import com.zusmart.inject.util.ResourceUtils;

public class ResourceByClassPath extends AbstractResource {

	private String path;
	private ClassLoader classLoader;

	public ResourceByClassPath(String path, ClassLoader classLoader) {
		if (StringUtils.isBlank(path)) {
			throw new IllegalArgumentException("Path must not be null");
		}
		this.path = ResourceUtils.cleanPath(path);
		this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
	}

	@Override
	public String getFileName() {
		return ResourceUtils.getFileName(this.path);
	}

	@Override
	public String getFilePath() {
		return this.path;
	}

	@Override
	public InputStream getFileInputStream() throws IOException {
		InputStream inputStream = null;
		if (null != this.classLoader) {
			inputStream = this.classLoader.getResourceAsStream(this.path);
		} else {
			inputStream = ClassLoader.getSystemResourceAsStream(this.path);
		}
		if (null == inputStream) {
			throw new FileNotFoundException(String.format("Resource cannot be opened because it does not exist [%s]", this.path));
		}
		return inputStream;
	}

}