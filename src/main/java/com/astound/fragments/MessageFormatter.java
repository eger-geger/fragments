package com.astound.fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageFormatter {

    private final static Pattern ARGUMENT_PATTERN = Pattern.compile("\\{\\d\\}");

    private MessageFormatter() {}

    public static String format(String pattern, Object[] arguments) {
        Matcher matcher = ARGUMENT_PATTERN.matcher(pattern);

        StringBuffer messageBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(messageBuffer, formatArgument(matcher, arguments));
        }
        matcher.appendTail(messageBuffer);

        return messageBuffer.toString();
    }

    private static String formatArgument(Matcher matcher, Object[] arguments) {
        int index = Integer.parseInt(matcher.group(1));

        Object argument = index < arguments.length ? arguments[index] : String.format("{%s}", index);

        return argument == null ? "null" : argument.toString();
    }
}
