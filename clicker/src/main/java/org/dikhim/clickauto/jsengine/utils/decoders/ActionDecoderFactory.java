package org.dikhim.clickauto.jsengine.utils.decoders;

import java.util.ArrayList;
import java.util.List;

public class ActionDecoderFactory {
    private static List<String> listOfEncodings = new ArrayList<>();

    static {
        listOfEncodings.add("unicode");
        listOfEncodings.add("base64");
        listOfEncodings.add("base64-zip");
    }
    public static ActionDecoder get(String encoding) {
        if ("unicode".equalsIgnoreCase(encoding)) {
            return new UnicodeActionDecoder();
        } else if ("base64".equalsIgnoreCase(encoding)) {
            return new Base64ActionDecoder();
        } else if ("base64-zip".equalsIgnoreCase(encoding)) {
            return new Base64ZipActionDecoder();
        } 
        return new UnicodeActionDecoder();
    }
    public static List<String> getListOfEncodings() {
        return listOfEncodings;
    }
}
