package com.astound.fragments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark fragments which is {@code}<iframe></iframe>{@code}. Than {@link FrameHandler#perform(com.astound.fragments.elements.Fragment)} will
 * automatically switch context and have access to frame inner elements.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Frame {}
