package org.dikhim.jclicker.util;

public class StackTraceUtil {
    public static void trace(StackTraceElement e[]) {
        boolean doNext = false;
        for (StackTraceElement s : e) {
            if (doNext) {
                return;
            }
            doNext = s.getMethodName().equals("getStackTrace");
        }
    }
}
