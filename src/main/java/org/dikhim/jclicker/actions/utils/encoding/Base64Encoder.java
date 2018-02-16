package org.dikhim.jclicker.actions.utils.encoding;

import javax.persistence.criteria.CriteriaBuilder;

public class Base64Encoder {
    Base64Alfabet alfabet = new Base64Alfabet();

    private static final int SIXTET_MASK = 63;

    /**
     * Encode number from -32 to 31
     *
     * @param param
     * @return
     */
    public char[] encodeTo1Chars(int param) {
        char[] result = new char[1];
        result[0] = alfabet.getChar((param + 32) & SIXTET_MASK);
        return result;
    }

    /**
     * Encode number from -2048 to 2047
     *
     * @param param
     * @return
     */
    public char[] encodeTo2Chars(int param) {
        char[] result = new char[2];
        result[0] = alfabet.getChar((param + 2048) >> 6 & SIXTET_MASK);
        result[1] = alfabet.getChar((param + 2048) & SIXTET_MASK);
        return result;
    }

    /**
     * Encode number from -131072 to 131071
     *
     * @param param
     * @return
     */
    public char[] encodeTo3Chars(int param) {
        char[] result = new char[3];
        result[0] = alfabet.getChar((param + 131072) >> 12 & SIXTET_MASK);
        result[1] = alfabet.getChar((param + 131072) >> 6 & SIXTET_MASK);
        result[2] = alfabet.getChar((param + 131072) & SIXTET_MASK);
        return result;
    }

    /**
     * Encode number from 0 to 63
     *
     * @param param
     * @return
     */
    public char encodeTo1CharUnsigned(int param) {
        return alfabet.getChar(param & SIXTET_MASK);
    }

    /**
     * Encode number from 0 to 4095
     *
     * @param param
     * @return
     */
    public char[] encodeTo2CharsUnsigned(int param) {
        char[] result = new char[2];
        result[0] = alfabet.getChar(param >> 6 & SIXTET_MASK);
        result[1] = alfabet.getChar(param & SIXTET_MASK);
        return result;
    }

    /**
     * Encode number from 0 to 262143
     *
     * @param param
     * @return
     */
    public char[] encodeTo3CharsUnsigned(int param) {
        char[] result = new char[3];
        result[0] = alfabet.getChar(param >> 12 & SIXTET_MASK);
        result[1] = alfabet.getChar(param >> 6 & SIXTET_MASK);
        result[2] = alfabet.getChar(param & SIXTET_MASK);
        return result;
    }


    /**
     * Encode number to char sequence
     * @param param
     * @return
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
            result[i] = alfabet.getChar((param + difference) >> shift & SIXTET_MASK);
        }


        return result;
    }
}
