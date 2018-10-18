package com.zusmart.inject.plugin.support;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import com.zusmart.inject.annotation.Pluggable;
import com.zusmart.inject.plugin.Plugin;

public class PluginManagerByAnnotation extends AbstractPluginManager{

	@Override
	public Map<Annotation,Class<? extends Plugin>> searchPlugins(Class<?> target){
		Map<Annotation,Class<? extends Plugin>> result = new HashMap<Annotation,Class<? extends Plugin>>();
		if(null == target){
			return result;
		}
		Annotation[] ans = target.getAnnotations();
		if(null == ans || ans.length == 0){
			return result;
		}
		for(int i = 0;i < ans.length;i++){
			Annotation an = ans[i];
			if(an.annotationType().getAnnotation(Pluggable.class) != null){
				Pluggable pluggable = an.annotationType().getAnnotation(Pluggable.class);
				if(null != pluggable){
					result.put(an,pluggable.value());
				}
			}
		}
		return result;
	}
	
}