package org.dikhim.jclicker.actions.utils.encoding;

public class Base64Encoder {
    Base64Alfabet alfabet = new Base64Alfabet();

    /**
     * Encode number from 0 to 63
     *
     * @param param
     * @return
     */
    public char encodeToOneChar(int param) {
        return alfabet.getChar(param);
    }

    /**
     * Encode number from 0 to 4095
     *
     * @param param
     * @return
     */
    public char[] encodeToTwoChars(int param) {
        char[] result = new char[2];
        result[0] = alfabet.getChar(param / 64);
        result[1] = alfabet.getChar(param % 64);
        return result;
    }

    /**
     * Encode number from 0 to 250047
     *
     * @param param
     * @return
     */
    public char[] encodeToThreeChars(int param) {
        char[] result = new char[3];
        result[0] = alfabet.getChar(param / 64);
        result[1] = alfabet.getChar(param / 4096);
        result[2] = alfabet.getChar(param % 32);
        return result;
    }
}
