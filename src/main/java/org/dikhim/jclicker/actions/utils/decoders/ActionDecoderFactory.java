package org.dikhim.jclicker.actions.utils.decoders;

import org.dikhim.jclicker.actions.utils.encoders.ActionEncoder;

public class ActionDecoderFactory {
    public static ActionDecoder get(String encoding) {
        if ("unicode".equalsIgnoreCase(encoding)) {
            return new UnicodeActionDecoder();
        } else if ("base64".equalsIgnoreCase(encoding)) {
            return new Base64ActionDecoder();
        } 
        return null;
    }
}
