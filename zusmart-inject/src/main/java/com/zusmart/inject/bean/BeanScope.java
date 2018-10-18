package com.zusmart.inject.bean;

public enum BeanScope {

	Singleton("singleton"), // 单例模式,指定对象在整个生命周期中只有一个实例对象
	Prototype("prototype");// 多例模式,指定对象在整个生命周期中根据调用或引用的次数创建

	private String value;

	private BeanScope(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}