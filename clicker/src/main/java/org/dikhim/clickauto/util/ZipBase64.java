package org.dikhim.clickauto.util;

import java.io.IOException;
import java.util.Base64;

public class ZipBase64 {
    public static byte[] decode(String data) throws IOException {
        byte[]  compressedData = Base64.getDecoder().decode(data);
        return Gzip.decompressBytes(compressedData);
    }

    public static String encode(byte[] data) throws IOException {
        byte[] compressedData = Gzip.compressBytes(data);
        return Base64.getEncoder().encodeToString(compressedData);
    }
}
