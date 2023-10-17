package org.dikhim.clickauto.jsengine.utils.encoding;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class Base64Alfabet {
    private static BidiMap<Integer, Character> alfabet = new DualHashBidiMap<>();

    static {
        char[] base64Chars = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
        };
        for (int i =0;i<base64Chars.length;i++) {
            alfabet.put(i, base64Chars[i]);
        }
    }

    public char getChar(int i) {
        return alfabet.get(i);
    }

    public int getNumber(char c){
        return alfabet.getKey(c);
    }

}
