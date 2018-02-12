package org.dikhim.jclicker.actions.utils.encoding;

public class Base64Encoder {
    Base64Alfabet alfabet = new Base64Alfabet();

    private static final int MASK_1_SIXTET = 63; 
    private static final int MASK_2_SIXTET = 4032; 
    private static final int MASK_3_SIXTET = 258048; 
    private static final int MASK_4_SIXTET = 16515072; 
    private static final int MASK_5_SIXTET = 1056964608; 
    /**
     * Encode number from 0 to 63
     *
     * @param param
     * @return
     */
    public char encodeTo1Char(int param) {
        return alfabet.getChar(param);
    }

    /**
     * Encode number from 0 to 4095
     *
     * @param param
     * @return
     */
    public char[] encodeTo2Chars(int param) {
        char[] result = new char[2];
        result[0] = alfabet.getChar(param & MASK_2_SIXTET);
        result[1] = alfabet.getChar(param & MASK_1_SIXTET);
        return result;
    }

    /**
     * Encode number from 0 to 262143
     *
     * @param param
     * @return
     */
    public char[] encodeTo3Chars(int param) {
        char[] result = new char[3];
        result[0] = alfabet.getChar(param & MASK_3_SIXTET);
        result[1] = alfabet.getChar(param & MASK_2_SIXTET);
        result[2] = alfabet.getChar(param & MASK_1_SIXTET);
        return result;
    }
}
