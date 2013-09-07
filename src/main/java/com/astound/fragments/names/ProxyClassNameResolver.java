package com.astound.fragments.names;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

public class ProxyClassNameResolver extends DefaultClassNameResolver {

	@Override public String resolveName(Class aClass) {
		return Enhancer.isEnhanced(aClass) || Proxy.isProxyClass(aClass)
				? super.resolveName(aClass.getSuperclass())
				: super.resolveName(aClass);
	}
}
