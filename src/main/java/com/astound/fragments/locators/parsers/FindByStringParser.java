package com.astound.fragments.locators.parsers;

import com.astound.fragments.locators.FindByString;
import org.openqa.selenium.By;

public class FindByStringParser implements LocatorParser<FindByString> {

	private final LocatorParser<String> stringLocatorParser;

	public FindByStringParser(LocatorParser<String> stringLocatorParser) {
		this.stringLocatorParser = stringLocatorParser;
	}

	public FindByStringParser() {
		this(new StringLocatorParser());
	}

	@Override public By parse(FindByString findByString) {
		return stringLocatorParser.parse(findByString.value());
	}
}
