package org.dikhim.clickauto.jsengine.utils.decoders;


import org.dikhim.clickauto.jsengine.actions.Action;
import org.dikhim.clickauto.jsengine.utils.encoding.UnicodeDecoder;
import org.dikhim.clickauto.util.Gzip;

import java.io.IOException;
import java.util.List;

public class UnicodeZipActionDecoder extends UnicodeActionDecoder {
    @Override
    public List<Action> decode(String code) {
        String decompressed = "";
        try {
            decompressed = Gzip.decompressString(UnicodeDecoder.decode(code));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.decode(decompressed);
    }
}
