package com.zusmart.inject.scanner;

public interface PackageScannerFilter {
	
	public boolean doFilter(Class<?> target);
	
}