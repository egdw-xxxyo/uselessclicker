package org.dikhim.clickauto.util;

public class MathUtil {
    public static double roundTo1(double number) {
        return Math.round(number * 10.0) / 10.0;
    }
    public static double roundTo5(double number) {
        return Math.round(number * 100.0) / 100.0;
    }
}
