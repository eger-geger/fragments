package com.astound.fragments.locators.extractors;

import com.google.common.base.Optional;
import org.openqa.selenium.By;

import java.lang.reflect.Field;

public interface ByExtractor {

    public Optional<By> extract(Field field);

}
