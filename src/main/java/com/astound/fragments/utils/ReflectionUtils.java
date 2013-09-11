package com.astound.fragments.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtils {

    public static boolean isOverridden(Method method1, Method method2) {
        return method1.getName().equals(method2.getName())
                && method1.getParameterTypes().equals(method2.getParameterTypes())
                && method1.getDeclaringClass().equals(method2.getDeclaringClass());
    }

    public static void assignField(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Class getFieldGenericType(Field field) {
        Type type = field.getGenericType();

        if (type instanceof ParameterizedType) {
            Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];

            if (typeArgument instanceof Class) {
                return (Class) typeArgument;
            }

            if (typeArgument instanceof ParameterizedType) {
                return (Class) ((ParameterizedType) typeArgument).getRawType();
            }

            return null;
        }

        return field.getType();
    }

}
