package com.astound.fragments.locators.parsers;

import com.astound.fragments.locators.FindByString;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.How;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindByStringParser {

    private final static Pattern CUSTOM_LOCATOR_PATTERN = Pattern
            .compile("(css|xpath|id|name|tag_name|id_or_name|class_name|link_text|partial_link_text)\\s*::\\s*(.+)", Pattern.CASE_INSENSITIVE);


    public By parse(FindByString findByString) {
        Matcher matcher = CUSTOM_LOCATOR_PATTERN.matcher(findByString.value());

        if (matcher.matches()) {
            return buildBy(howValueOf(matcher), locatorValueOf(matcher));
        }

        throw new IllegalArgumentException(String.format("Incorrect syntax [%s].", findByString.value()));
    }

    private static String locatorValueOf(Matcher matcher) {
        return matcher.group(2).trim();
    }

    private static How howValueOf(Matcher matcher) {
        return Enum.valueOf(How.class, matcher.group(1).trim().toUpperCase());
    }

    private static By buildBy(How how, String locator) {
        switch (how) {
            case CLASS_NAME:
                return By.className(locator);
            case CSS:
                return By.cssSelector(locator);
            case ID:
                return By.id(locator);
            case ID_OR_NAME:
                return new ByIdOrName(locator);
            case LINK_TEXT:
                return By.linkText(locator);
            case NAME:
                return By.name(locator);
            case PARTIAL_LINK_TEXT:
                return By.partialLinkText(locator);
            case TAG_NAME:
                return By.tagName(locator);
            case XPATH:
                return By.xpath(locator);
            default:
                throw new IllegalArgumentException("Cannot determine how to locate element " + how);
        }
    }
}
