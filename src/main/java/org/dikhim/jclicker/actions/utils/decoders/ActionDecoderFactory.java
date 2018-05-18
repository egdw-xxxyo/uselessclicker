package org.dikhim.jclicker.actions.utils.decoders;

public class ActionDecoderFactory {
    public static ActionDecoder get(String encoding) {
        if ("unicode".equalsIgnoreCase(encoding)) {
            return new UnicodeActionDecoder();
        } else if ("base64".equalsIgnoreCase(encoding)) {
            return new Base64ActionDecoder();
        } 
        return new UnicodeActionDecoder();
    }
}
