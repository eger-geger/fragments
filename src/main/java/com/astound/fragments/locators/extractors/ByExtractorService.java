package com.astound.fragments.locators.extractors;

import com.astound.fragments.locators.parsers.FinByParser;
import com.astound.fragments.locators.parsers.LocatorParser;
import com.google.common.collect.Iterables;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ServiceLoader;

/**
 * Aggregates {@link ByExtractor} instances loaded via services and default extractors. Default Extractors: {@link FromFindAll}, {@link FromFindBys},
 * {@link FromFindBy}, {@link FromFieldName} Used by {@link com.astound.fragments.locators.FragmentLocator}
 */
public final class ByExtractorService extends IteratingByExtractor {

    private static ByExtractor instance;

    private ByExtractorService() {
        super(Iterables.concat(ServiceLoader.load(ByExtractor.class), defaultExtractors()));
    }

    private static Iterable<ByExtractor> defaultExtractors() {
        final LocatorParser<FindBy> findByParser = FinByParser.getInstance();

        Deque<ByExtractor> extractors = new ArrayDeque<>();
        extractors.addLast(new FromFindAll(findByParser));
        extractors.addLast(new FromFindBys(findByParser));
        extractors.addLast(new FromFindBy(findByParser));
        extractors.addLast(new FromFieldName());

        return extractors;
    }

    public static ByExtractor getInstance() {
        if (instance == null) {
            instance = new ByExtractorService();
        }
        return instance;
    }
}
