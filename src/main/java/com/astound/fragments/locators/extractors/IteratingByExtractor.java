package com.astound.fragments.locators.extractors;

import com.google.common.base.Optional;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.util.Iterator;

public class IteratingByExtractor implements ByExtractor {

    private final Iterable<ByExtractor> extractors;

    public IteratingByExtractor(Iterable<ByExtractor> extractors) {
        this.extractors = extractors;
    }

    @Override final public Optional<By> extract(Field field) {
        return doExtract(extractors.iterator(), field);
    }

    private static Optional<By> doExtract(Iterator<ByExtractor> iterator, Field field) {
        if (iterator.hasNext()) {
            return iterator.next().extract(field).or(doExtract(iterator, field));
        } else {
            return Optional.absent();
        }
    }
}
