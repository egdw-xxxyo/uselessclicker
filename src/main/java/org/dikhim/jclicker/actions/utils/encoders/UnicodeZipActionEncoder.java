package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.events.Event;
import org.dikhim.jclicker.actions.utils.encoding.Gzip;
import org.dikhim.jclicker.actions.utils.encoding.UnicodeEncoder;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class UnicodeZipActionEncoder extends UnicodeActionEncoder{

    @Override
    public String encode(List<Event> eventList) {
        String data = super.encode(eventList);
        try {
            byte[]  compressed = Gzip.compress(data);
            data = UnicodeEncoder.encode(compressed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
