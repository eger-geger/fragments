package com.astound.fragments.names;

public class DefaultClassNameResolver implements ClassNameResolver {

	@Override public String resolveName(Class aClass) {
		return aClass.getSimpleName();
	}

}
