package org.dikhim.jclicker.util;

import javafx.util.StringConverter;

public class Converters {
    public static StringConverter<Number> getStringToNumberConvertor() {
        return new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                try {
                    return String.valueOf(object);
                } catch (Exception e) {
                    System.out.println(object);
                    return "";
                }
            }


            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (Exception e) {
                    System.out.println(string);
                    return 0;
                }
            }
        };
    }
}
