package com.astound.fragments.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StackTraceUtils {

    private final static String[] TRASH_PACKAGES = new String[]{
            "sun.reflect", "java.lang.reflect", "java.util.concurrent",
            "org.openqa.selenium",
            "net.thucidides.fragments.proxy", "net.thucidides.fragments.utils"
    };

    public static Throwable cleanStackTrace(Throwable throwable) {
        List<StackTraceElement> effectiveStackTrace =
                new ArrayList<>(Arrays.asList(throwable.getStackTrace()));

        for (StackTraceElement element : throwable.getStackTrace()) {
            for (int i = 0; i < TRASH_PACKAGES.length && effectiveStackTrace.contains(element); i++) {
                if (element.getClassName().startsWith(TRASH_PACKAGES[i])) {
                    effectiveStackTrace.remove(element);
                }
            }
        }

        throwable.setStackTrace(effectiveStackTrace.toArray(new StackTraceElement[0]));

        return throwable;
    }
}