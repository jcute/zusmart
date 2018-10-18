package com.zusmart.inject.config.support;

public class ConfigBySystemProperties extends ConfigByProperties {

	public ConfigBySystemProperties() {
		super(System.getProperties());
	}

}