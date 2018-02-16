package org.dikhim.jclicker.actions.utils.encoding;

public class NaturalEncoder {
    public static final int PARAM_SIZE = 5;

    public String encode(int i) {
        if (i < -9999) i = 9999;
        if (i > 10000) i = 10000;

        return String.valueOf(i);
    }
}
