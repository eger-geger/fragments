package com.astound.fragments.locators.parsers;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.Annotations;

public class FinByParser extends Annotations implements LocatorParser<FindBy> {

    private static FinByParser instance;

    private FinByParser() {
        super(null);
    }

    @Override public By parse(FindBy findBy) {
        return super.buildByFromFindBy(findBy);
    }

    public static FinByParser getInstance() {
        if (instance == null) {
            instance = new FinByParser();
        }

        return instance;
    }
}
