package com.astound.fragments.format.transformers;

import com.astound.fragments.format.ToStringConverter;

import java.util.regex.Pattern;

/** Replaces {@code}{index}{@code} with string representation of provided array element */
public class ArrayTransformer extends RegexpTransformer {

    private static final int ORDER_SHIFT = 1;

    private final static Pattern ARGUMENT_PATTERN = Pattern.compile("\\{(\\d)\\}");

    private final Object[] objects;

    public ArrayTransformer(Object[] objects) {
        super(ARGUMENT_PATTERN);
        this.objects = objects;
    }

    @Override protected String transformMatch(Match match, ToStringConverter converter) {
        int position = Integer.parseInt(match.group(1));

        if (position > 0 && position <= objects.length) {
            return converter.apply(objects[position - ORDER_SHIFT]);
        } else {
            return nothingForIndex(position - ORDER_SHIFT);
        }
    }

    private static String nothingForIndex(int index) {
        return String.format("nothing on [%s] index", index);
    }
}
