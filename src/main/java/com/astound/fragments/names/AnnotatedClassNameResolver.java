package com.astound.fragments.names;

import com.google.common.base.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AnnotatedClassNameResolver extends DefaultClassNameResolver {

	@Override public String resolveName(Class aClass) {
		return contextNameOf(aClass).or(super.resolveName(aClass));
	}

	private Optional<String> contextNameOf(Class aClass) {
		ContextName contextName = (ContextName) aClass.getAnnotation(ContextName.class);

		return contextName == null || isEmpty(contextName.value())
				? Optional.<String>absent()
				: Optional.of(contextName.value());
	}
}
