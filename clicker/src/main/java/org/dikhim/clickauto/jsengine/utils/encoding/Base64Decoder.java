package org.dikhim.clickauto.jsengine.utils.encoding;

public class Base64Decoder {
    Base64Alfabet alfabet = new Base64Alfabet();
    public int decode(char[] params) {
        int result = 0;
        int multiplier = 1;
        int multiplierBase = 64;
        for(int i=params.length-1; i>=0 ;i--) {
            result += alfabet.getNumber(params[i]) * multiplier;
            multiplier *= multiplierBase;
        }
        return result - multiplier / 2;
    }
}
