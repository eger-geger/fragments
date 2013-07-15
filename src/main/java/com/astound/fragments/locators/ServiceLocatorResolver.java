package com.astound.fragments.locators;

import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ServiceLoader;


public class ServiceLocatorResolver implements ILocatorResolver {

    private static ILocatorResolver instance;

    private final Collection<ILocatorResolver> resolvers;

    private ServiceLocatorResolver() {
        Iterator<ILocatorResolver> resolversIterator = ServiceLoader
                .load(ILocatorResolver.class).iterator();

        resolvers = new LinkedList<>();

        while (resolversIterator.hasNext()) {
            resolvers.add(resolversIterator.next());
        }
    }

    public boolean isApplicable(Field field) {
        for (ILocatorResolver resolver : resolvers) {
            if (resolver.isApplicable(field)) {
                return true;
            }
        }
        return false;
    }

    public By resolve(Field field) {
        for (ILocatorResolver resolver : resolvers) {
            if (resolver.isApplicable(field)) {
                return resolver.resolve(field);
            }
        }
        throw new IllegalArgumentException("Resolver is not applicable.");
    }

    public static ILocatorResolver getInstance() {
        if (instance == null) {
            instance = new ServiceLocatorResolver();
        }
        return instance;
    }
}
