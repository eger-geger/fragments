package com.astound.fragments.names;

import java.lang.reflect.Field;

public class DefaultFieldNameResolver implements FieldNameResolver {

	@Override public String resolveName(Field field) {
		return field.getName();
	}
}
