package org.dikhim.clickauto.jsengine.utils.encoders;

import org.dikhim.clickauto.jsengine.events.Event;
import org.dikhim.clickauto.util.Gzip;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class Base64ZipActionEncoder extends Base64ActionEncoder{
    @Override
    public String encode(List<Event> eventList) {
        String data = super.encode(eventList);
        try {
            byte[]  compressed = Gzip.compressString(data);
            data = Base64.getEncoder().encodeToString(compressed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
