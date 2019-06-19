package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.events.Event;
import org.dikhim.jclicker.util.Gzip;

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
