package org.dikhim.jclicker.actions.utils.encoding;

public class NaturalDecoder {
    private final int PARAM_SIZE = NaturalEncoder.PARAM_SIZE;

    public int decode(String param) {
        return Integer.parseInt(param);
    }
}
