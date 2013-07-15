package com.astound.fragments;

import com.astound.fragments.annotation.Frame;
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

    public Object decorate(ClassLoader classLoader, Field field) {
        Class<?> fieldType = field.getType();

        String fieldName = field.getName();

        ElementLocator locator = locatorFactory.createLocator(field);

        if (field.isAnnotationPresent(Frame.class)) {
            if (Fragment.class.isAssignableFrom(fieldType)) {
                return elementFactory.createFrame((Class<Fragment>) fieldType, locator, fieldName);
            }

            if (WebElement.class.isAssignableFrom(fieldType)) {
                return elementFactory.createFrame(Fragment.class, locator, fieldName);
            }
        }

        if (Fragment.class.isAssignableFrom(fieldType)) {
            return elementFactory.createFragment((Class<Fragment>) fieldType, locator, fieldName);
        }

        if (WebElement.class.isAssignableFrom(fieldType)) {
            return elementFactory.createFragment(Fragment.class, locator, fieldName);
        }

        if (List.class.isAssignableFrom(fieldType)) {
            Class<?> genericType = getGenericType(field);

            if (Fragment.class.isAssignableFrom(genericType)) {
                return elementFactory.createList((Class<Fragment>) genericType, locator, fieldName);
            }

            if (WebElement.class.isAssignableFrom(genericType)) {
                return elementFactory.createList(Fragment.class, locator, fieldName);
            }
        }

        return null;
    }

    private static Class getGenericType(Field field) {
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
