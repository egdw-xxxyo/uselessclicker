package org.dikhim.clickauto.jsengine.utils.encoders;


import org.dikhim.clickauto.jsengine.events.Event;
import org.dikhim.clickauto.jsengine.utils.encoding.UnicodeEncoder;
import org.dikhim.clickauto.util.Gzip;

import java.io.IOException;
import java.util.List;

public class UnicodeZipActionEncoder extends UnicodeActionEncoder{

    @Override
    public String encode(List<Event> eventList) {
        String data = super.encode(eventList);
        try {
            byte[]  compressed = Gzip.compressString(data);
            data = UnicodeEncoder.encode(compressed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
