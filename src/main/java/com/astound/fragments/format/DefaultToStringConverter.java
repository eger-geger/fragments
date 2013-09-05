package com.astound.fragments.format;

import java.util.Arrays;

public class DefaultToStringConverter implements ToStringConverter {

	private static final String NULL_STRING = "NULL";

	@Override public String apply(Object object) {
		if (object == null) {
			return NULL_STRING;
		}

		if (object.getClass().isArray()) {
			return Arrays.toString((Object[]) object);
		}

		return object.toString();
	}
}
