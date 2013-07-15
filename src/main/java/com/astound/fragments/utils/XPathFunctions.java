package com.astound.fragments.utils;

import org.apache.commons.lang3.StringUtils;

public class XPathFunctions {

    public static String translate(String source, String target, String replacement) {
        StringBuilder xpathBuilder = new StringBuilder("translate(");

        xpathBuilder.append(StringUtils.join(new String[]{source, target, replacement}, ','));

        return xpathBuilder.append(')').toString();
    }

    public static String concat(String... inputs) {
        StringBuilder xpathBuilder = new StringBuilder("concat(");

        xpathBuilder.append(StringUtils.join(inputs, ','));

        return xpathBuilder.append(')').toString();
    }

    public static String normalizeSpace(String input) {
        return new StringBuilder("normalize-space(").append(input).append(')').toString();
    }

    public static String contains(String target, String input) {
        return new StringBuilder("contains(").append(target).append(',').append(input).append(')').toString();
    }

    public static String containstext(String target, String input) {
        StringBuilder uniquebuilder = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (Character.isLetter(c) && uniquebuilder.indexOf(String.valueOf(c).toLowerCase()) == -1) {
                uniquebuilder.append(String.valueOf(c).toLowerCase());
            }
        }

        String replace = '"' + uniquebuilder.toString().toUpperCase() + "'" + '"';

        String replcement = '"' + uniquebuilder.toString().toLowerCase() + '"';

        String translate = translate(target, replace, replcement);

        input = "'" + input.replace("'", "") + "'";

        return contains(translate, input.toLowerCase());
    }

}
