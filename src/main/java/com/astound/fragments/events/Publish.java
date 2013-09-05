package com.astound.fragments.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Publish {

    public PageEventType type() default PageEventType.PAGE_EVENT;

    public String format() default "executed {method}:{return}";

}
