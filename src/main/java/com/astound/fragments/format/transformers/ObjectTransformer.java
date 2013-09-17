package com.astound.fragments.format.transformers;

import com.astound.fragments.format.ToStringConverter;

import java.util.regex.Pattern;

/** Replaces all occurrences of provided pattern with string representation of provided value */
public class ObjectTransformer extends RegexpTransformer {

    private final Object object;

    public ObjectTransformer(Pattern regexp, Object object) {
        super(regexp);
        this.object = object;
    }

    @Override protected String transformMatch(Match match, ToStringConverter converter) {
        return converter.apply(object);
    }
}
