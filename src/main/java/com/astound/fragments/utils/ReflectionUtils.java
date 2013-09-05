package com.astound.fragments.utils;

import java.lang.reflect.Method;

public class ReflectionUtils {

	public static boolean isOverridden(Method method1, Method method2) {
		return method1.getName().equals(method2.getName())
				&& method1.getParameterTypes().equals(method2.getParameterTypes())
				&& method1.getDeclaringClass().equals(method2.getDeclaringClass());
	}

}
