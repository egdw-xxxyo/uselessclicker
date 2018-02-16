package org.dikhim.jclicker.actions.utils.encoders;

public class ActionEncoderFactory {
    public static ActionEncoder get(String encoding) {
        if ("unicode".equalsIgnoreCase(encoding)) {
            return new UnicodeActionEncoder();
        } else if ("base64".equalsIgnoreCase(encoding)) {
            return new Base64ActionEncoder();
        }
        return null;
    }
}
