package org.dikhim.clickauto.jsengine.utils.encoders;


import org.dikhim.clickauto.jsengine.events.Event;

import java.util.List;

public interface ActionEncoder {
    ActionEncoder begin();

    ActionEncoder addKeys();

    ActionEncoder addMouseButtons();

    ActionEncoder addMouseWheel();

    ActionEncoder addDelays();

    ActionEncoder relative();

    ActionEncoder absolute();

    ActionEncoder fixedRate(int rate);

    ActionEncoder minDistance(int distance);

    ActionEncoder detectStopPoints(int stopPointDetectionThreshold);

    String encode(List<Event> eventList);
}
