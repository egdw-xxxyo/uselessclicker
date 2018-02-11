package org.dikhim.jclicker.actions.utils.decoders;

import org.dikhim.jclicker.actions.utils.encoders.ActionEncoder;

public class ActionDecoderFactory {
    public static ActionDecoder get(String encoding) {
        if ("unicode".equalsIgnoreCase(encoding)) {
            return new UnicodeActionDecoder();
        }
        return null;
    }
}
