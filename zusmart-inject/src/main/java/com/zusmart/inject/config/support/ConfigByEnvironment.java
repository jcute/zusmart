package com.zusmart.inject.config.support;

import java.util.HashMap;
import java.util.Map;

public class ConfigByEnvironment extends AbstractConfig{

	private Map<String,String> configs = new HashMap<String,String>();

	public ConfigByEnvironment(){
		Map<String,String> temp = System.getenv();
		if(null != temp && temp.size() > 0){
			this.configs.putAll(temp);
		}
	}
	
	@Override
	public boolean containsName(String name){
		return this.configs.containsKey(name);
	}

	@Override
	protected String getValue(String name){
		return this.configs.get(name);
	}
	
}