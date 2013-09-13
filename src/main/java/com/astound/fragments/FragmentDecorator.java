package com.astound.fragments;

import com.astound.fragments.elements.Fragment;
import com.astound.fragments.names.DefaultFieldNameResolver;
import com.astound.fragments.names.FieldNameResolver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Field;
import java.util.List;

import static com.astound.fragments.utils.ReflectionUtils.getFieldGenericType;

public class FragmentDecorator implements FieldDecorator {

    private final ElementLocatorFactory locatorFactory;

    private final FragmentFactory elementFactory;

    private final FieldNameResolver fieldNameResolver;

    public FragmentDecorator(ElementLocatorFactory locatorFactory, FragmentFactory elementFactory, FieldNameResolver fieldNameResolver) {
        this.locatorFactory = locatorFactory;
        this.elementFactory = elementFactory;
        this.fieldNameResolver = fieldNameResolver;
    }

    public FragmentDecorator(ElementLocatorFactory locatorFactory, FragmentFactory elementFactory) {
        this(locatorFactory, elementFactory, new DefaultFieldNameResolver());
    }

    @Override public Object decorate(ClassLoader classLoader, Field field) {
        Class fieldType = field.getType();
        String fieldName = fieldNameResolver.resolveName(field);

        ElementLocator locator = locatorFactory.createLocator(field);

        if (isFragmentCompatible(fieldType)) {
            Class<? extends Fragment> fClass = fragmentTypeOf(fieldType);

            return isFrame(field)
                    ? elementFactory.createFrame(fClass, locator, fieldName)
                    : elementFactory.createFragment(fClass, locator, fieldName);
        }

        if (List.class.isAssignableFrom(fieldType)) {
            Class listGenericType = getFieldGenericType(field);

            if (isFragmentCompatible(listGenericType)) {
                return elementFactory.createList(fragmentTypeOf(listGenericType), locator, fieldName);
            }
        }

        return null;
    }

    private static boolean isFragmentCompatible(Class aClass) {
        return Fragment.class.isAssignableFrom(aClass) || WebElement.class.isAssignableFrom(aClass);
    }

    private static boolean isFrame(Field field) {
        return field.isAnnotationPresent(Frame.class) || field.getType().isAnnotationPresent(Frame.class);
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

}
