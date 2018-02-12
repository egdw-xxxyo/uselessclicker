package org.dikhim.jclicker.actions.utils.encoding;

public class UnicodeEncoder {
    public static final int SHIFT = 13500;


    public String encode(int[] params) {
        char[] result = new char[params.length];
        for (int i = 0; i < params.length; i++) {
            result[i] = encode(params[i]);
        }

        return String.valueOf(result);
    }

    private char encode(int param) {
        return (char) (param + SHIFT);

    }
}
