package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.events.Event;

import java.util.List;

public interface ActionEncoder {
    ActionEncoder begin();
    ActionEncoder addKeys();
    ActionEncoder addMouseMovement();
    ActionEncoder addMouseButtons();
    ActionEncoder addMouseWheel();
    ActionEncoder addMouse();
    ActionEncoder addDelays();
    ActionEncoder relative();
    ActionEncoder absolute();
    ActionEncoder fixRate(int rate);

    String encode(List<Event> eventList);
}
