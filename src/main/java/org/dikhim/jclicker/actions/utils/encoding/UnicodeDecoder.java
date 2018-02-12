package org.dikhim.jclicker.actions.utils.encoding;

import static org.dikhim.jclicker.actions.utils.encoders.UnicodeActionEncoder.SHIFT;

public class UnicodeDecoder {

    /**
     * Decodes string to int array where each char converts to int representation
     * @param string that should be decoded
     * @return int array of decoded chars
     */
    public int[] decode(String string) {
        return decode(string.toCharArray());
    }

    private int[] decode(char[] params) {
        int[] result = new int[params.length];
        for (int i = 0; i < params.length; i++) {
            result[i] = decode(params[i]);
        }
        return result;
    }

    private int decode(char c) {
        return c - SHIFT;
    }
}
