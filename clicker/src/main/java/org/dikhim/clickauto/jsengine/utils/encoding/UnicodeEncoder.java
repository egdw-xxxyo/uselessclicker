package org.dikhim.clickauto.jsengine.utils.encoding;

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

    public static String encode(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i+=2) {
            char c;
            if (i == data.length - 1) {
                c = (char)(((data[i]&0x00FF)<<8));
            }else {
                c = (char)(((data[i]&0x00FF)<<8) + (data[i+1]&0x00FF));
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
