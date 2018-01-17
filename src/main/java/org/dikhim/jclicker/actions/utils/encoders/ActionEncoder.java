package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.events.Event;

import java.util.List;

public interface ActionEncoder {
    ActionEncoder begin();
    ActionEncoder addKeys();
    ActionEncoder addMouseButtons();
    ActionEncoder addMouseWheel();
    ActionEncoder addMouse();
    ActionEncoder addDelays();
    ActionEncoder relative();
    ActionEncoder absolute();
    ActionEncoder fixedRate(int rate);

    String encode(List<Event> eventList);
}