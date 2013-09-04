package com.astound.fragments.locators.extractors;

import com.google.common.base.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;

import java.lang.reflect.Field;

public class FromFieldName implements ByExtractor {

    @Override public Optional<By> extract(Field field) {
        return Optional.<By>of(new ByIdOrName(field.getName()));
    }
}
