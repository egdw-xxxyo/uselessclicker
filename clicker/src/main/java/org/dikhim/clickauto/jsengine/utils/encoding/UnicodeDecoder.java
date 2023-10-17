package org.dikhim.clickauto.jsengine.utils.encoding;

import java.io.UnsupportedEncodingException;

import static org.dikhim.clickauto.jsengine.utils.encoding.UnicodeEncoder.SHIFT;


public class UnicodeDecoder {


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

    public static byte[] decode(String data) {
        byte[] out = new byte[0];
        try {
            out = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return out;
    }
}
