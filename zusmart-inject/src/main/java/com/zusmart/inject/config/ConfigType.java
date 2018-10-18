package com.zusmart.inject.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.zusmart.basic.util.GenericUtils;
import com.zusmart.inject.resource.Resource;

public enum ConfigType {

	// Properties文件
	Properties(new ConfigConverter<Properties>() {
		@Override
		public java.util.Properties convert(Resource resource) {
			Properties properties = new Properties();
			InputStream inputStream = null;
			try {
				inputStream = resource.getFileInputStream();
				if (null != inputStream) {
					properties.load(inputStream);
				}
			} catch (IOException e) {
			} finally {
				if (null != inputStream) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return properties;
		}
	});

	private ConfigConverter<?> configConverter;

	ConfigType(ConfigConverter<?> configConverter) {
		this.configConverter = configConverter;
	}

	public <T> T convert(Resource resource) {
		return GenericUtils.parseType(this.configConverter.convert(resource));
	}

}