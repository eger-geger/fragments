package com.astound.fragments.events;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublishFormatter {

    private final static Pattern ARGUMENT_PATTERN = Pattern.compile(patternInCurlyBrackets("(a|arg|argument)(\\d)"));

    private final static Pattern RETURN_PATTERN = Pattern.compile(patternInCurlyBrackets("(r|ret|return)"));

    private final static Pattern NAME_PATTERN = Pattern.compile(patternInCurlyBrackets("(c|context)"));

    private final static Pattern METHOD_NAME_PATTERN = Pattern.compile(patternInCurlyBrackets("(m|method)"));

    private final StringBuffer stringBuffer;

    public PublishFormatter(Publish publish) {
        stringBuffer = new StringBuffer(publish.format());
    }

    public void formatArguments(Object[] arguments) {
        Matcher matcher = ARGUMENT_PATTERN.matcher(stringBuffer.toString());

        resetBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, formatArgument(matcher, arguments));
        }
        matcher.appendTail(stringBuffer);
    }

    private static String formatArgument(Matcher matcher, Object[] arguments) {
        int index = Integer.parseInt(matcher.group(2));

        return toString(index < arguments.length ? arguments[index] : "[argument not available]");
    }

    public void formatReturnValue(Object returnValue) {
        Matcher matcher = RETURN_PATTERN.matcher(stringBuffer.toString());

        resetBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, toString(returnValue));
        }
        matcher.appendTail(stringBuffer);

    }

    public void formatMethod(Method method) {
        Matcher matcher = METHOD_NAME_PATTERN.matcher(stringBuffer.toString());

        resetBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, method.getName());
        }
        matcher.appendTail(stringBuffer);
    }

    public void formatContextName(String contextName) {
        Matcher matcher = NAME_PATTERN.matcher(stringBuffer.toString());

        resetBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, contextName);
        }
        matcher.appendTail(stringBuffer);
    }

    public String getFormatted() {
        return stringBuffer.toString();
    }

    private void resetBuffer() {
        stringBuffer.delete(0, stringBuffer.length());
    }

    private static String patternInCurlyBrackets(String content) {
        return "\\{" + content + "\\}";
    }

    private static String toString(Object object) {
        if (object == null) {
            return "null";
        }

        if (object instanceof String) {
            return (String) object;
        }

        if (object.getClass().isArray()) {
            return ArrayUtils.toString(object);
        }

        return object.toString();
    }
}
