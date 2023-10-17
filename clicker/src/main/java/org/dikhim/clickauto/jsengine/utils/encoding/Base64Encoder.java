package org.dikhim.clickauto.jsengine.utils.encoding;


public class Base64Encoder {
    private Base64Alfabet alphabet = new Base64Alfabet();

    private static final int SEXTET_MASK = 63;

    /**
     * Encode number from -32 to 31
     *
     */
    public char[] encodeTo1Chars(int param) {
        char[] result = new char[1];
        result[0] = alphabet.getChar((param + 32) & SEXTET_MASK);
        return result;
    }

    /**
     * Encode number from -2048 to 2047
     *
     */
    public char[] encodeTo2Chars(int param) {
        char[] result = new char[2];
        result[0] = alphabet.getChar((param + 2048) >> 6 & SEXTET_MASK);
        result[1] = alphabet.getChar((param + 2048) & SEXTET_MASK);
        return result;
    }

    /**
     * Encode number from -131072 to 131071
     *
     */
    public char[] encodeTo3Chars(int param) {
        char[] result = new char[3];
        result[0] = alphabet.getChar((param + 131072) >> 12 & SEXTET_MASK);
        result[1] = alphabet.getChar((param + 131072) >> 6 & SEXTET_MASK);
        result[2] = alphabet.getChar((param + 131072) & SEXTET_MASK);
        return result;
    }

    /**
     * Encode number from 0 to 63
     *
     */
    public char encodeTo1CharUnsigned(int param) {
        return alphabet.getChar(param & SEXTET_MASK);
    }

    /**
     * Encode number from 0 to 4095
     *
     */
    public char[] encodeTo2CharsUnsigned(int param) {
        char[] result = new char[2];
        result[0] = alphabet.getChar(param >> 6 & SEXTET_MASK);
        result[1] = alphabet.getChar(param & SEXTET_MASK);
        return result;
    }

    /**
     * Encode number from 0 to 262143
     *
     */
    public char[] encodeTo3CharsUnsigned(int param) {
        char[] result = new char[3];
        result[0] = alphabet.getChar(param >> 12 & SEXTET_MASK);
        result[1] = alphabet.getChar(param >> 6 & SEXTET_MASK);
        result[2] = alphabet.getChar(param & SEXTET_MASK);
        return result;
    }


    /**
     * Encode number to char sequence
     */
    public char[] encode(int param) {
        int charsCount = 0;
        int difference = 0;
        if (param >= -32 && param <= 31) {
            charsCount = 1;
            difference = 32;
        } else if (param >= -2048 && param <= 2047) {
            charsCount = 2;
            difference = 2048;
        } else if (param >= -131072 && param <= 131071) {
            charsCount = 3;
            difference = 131072;
        }

        char[] result = new char[charsCount];
        int shift = 0;

        for (int i = result.length-1; i >= 0; i--) {
            result[i] = alphabet.getChar((param + difference) >> shift & SEXTET_MASK);
        }


        return result;
    }
}
