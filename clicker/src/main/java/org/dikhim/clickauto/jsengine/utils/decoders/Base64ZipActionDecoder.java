package org.dikhim.clickauto.jsengine.utils.decoders;


import org.dikhim.clickauto.jsengine.actions.Action;
import org.dikhim.clickauto.util.Gzip;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class Base64ZipActionDecoder extends Base64ActionDecoder {

    @Override
    public List<Action> decode(String code) {
        String decompressed = "";
        try {
            decompressed = Gzip.decompressString(Base64.getDecoder().decode(code));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.decode(decompressed);
    }
}
