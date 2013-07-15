package com.astound.fragments;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionObjectBuilder<T> {

    private Map<Field, Object> fieldMap = new HashMap<>();

    public ReflectionObjectBuilder<T> set(Class<? super T> superClass, String name, Object value) throws NoSuchFieldException, SecurityException {
        fieldMap.put(superClass.getDeclaredField(name), value);
        return this;
    }

    public void assign(T object) throws IllegalAccessException {
        for (Field field : fieldMap.keySet()) {
            field.setAccessible(true);
            field.set(object, fieldMap.get(field));
        }
    }
}
