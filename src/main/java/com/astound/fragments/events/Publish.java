package com.astound.fragments.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Publish {

	public EventType type() default EventType.PAGE_EVENT;

	/**
	 * Formatting options:
	 * for return value: {r|ret|return}
	 * for arguments: {1}, {2}...
	 * for method name: {m|method}
	 */
	public String format() default "executed {method}:{return}";

}
