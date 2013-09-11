package com.astound.fragments.locators.parsers;

import com.astound.fragments.locators.TransformableBy;
import org.openqa.selenium.support.How;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringLocatorParser implements LocatorParser<String> {

    private final static Pattern CUSTOM_LOCATOR_PATTERN = Pattern
            .compile("(css|xpath|id|name|tag_name|id_or_name|class_name|link_text|partial_link_text)\\s*::\\s*(.+)", Pattern.CASE_INSENSITIVE);

    @Override public TransformableBy parse(String string) {
        Matcher matcher = CUSTOM_LOCATOR_PATTERN.matcher(string);

        if (matcher.matches()) {
            return new TransformableBy(howValueOf(matcher), locatorValueOf(matcher));
        }

        throw new IllegalArgumentException(String.format("Incorrect syntax [%s].", string));
    }

    private static String locatorValueOf(Matcher matcher) {
        return matcher.group(2).trim();
    }

    private static How howValueOf(Matcher matcher) {
        return Enum.valueOf(How.class, matcher.group(1).trim().toUpperCase());
    }
}
