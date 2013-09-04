package com.astound.fragments;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class FragmentDecorator implements FieldDecorator {

    private final ElementLocatorFactory locatorFactory;

    private final FragmentFactory elementFactory;

    public FragmentDecorator(ElementLocatorFactory locatorFactory, FragmentFactory elementFactory) {
        this.locatorFactory = locatorFactory;
        this.elementFactory = elementFactory;
    }

    @Override public Object decorate(ClassLoader classLoader, Field field) {
        Class fieldType = field.getType();
        String fieldName = field.getName();

        ElementLocator locator = locatorFactory.createLocator(field);

        if (isFragmentCompatible(fieldType)) {
            return elementFactory.createFragment(fragmentTypeOf(fieldType), locator, fieldName);
        }

        if (List.class.isAssignableFrom(fieldType)) {
            Class listGenericType = fieldGenericType(field);

            if (isFragmentCompatible(listGenericType)) {
                return elementFactory.createList(fragmentTypeOf(listGenericType), locator, fieldName);
            }
        }

        return null;
    }

    private static boolean isFragmentCompatible(Class aClass) {
        return Fragment.class.isAssignableFrom(aClass) || WebElement.class.isAssignableFrom(aClass);
    }

    private static Class<? extends Fragment> fragmentTypeOf(Class aClass) {
        if (Fragment.class.isAssignableFrom(aClass)) {
            return aClass;
        } else if (WebElement.class.isAssignableFrom(aClass)) {
            return Fragment.class;
        } else {
            throw new IllegalArgumentException(String.format("Cannot determine type of fragment for [%s]", aClass));
        }
    }

    private static Class fieldGenericType(Field field) {
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
