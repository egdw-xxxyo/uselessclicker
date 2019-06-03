package org.dikhim.jclicker.jsengine.clickauto;

import org.dikhim.clickauto.jsengine.events.Event;
import org.dikhim.clickauto.jsengine.events.KeyboardEvent;
import org.dikhim.clickauto.jsengine.events.MouseButtonEvent;
import org.dikhim.clickauto.jsengine.events.MouseMoveEvent;
import org.dikhim.clickauto.jsengine.events.MouseWheelEvent;
import org.dikhim.jclicker.eventmanager.event.*;

import java.util.List;
import java.util.stream.Collectors;

public class EventsConverter {
    public static Event convertUselessEventToClickauto(org.dikhim.jclicker.eventmanager.event.Event e) {
        if (e instanceof MousePressEvent) {
            MousePressEvent event = (MousePressEvent) e;
            return new MouseButtonEvent(event.getButton(), null, "PRESS", event.getX(), event.getY(), event.getTime());
        } else if (e instanceof MouseReleaseEvent) {
            MouseReleaseEvent event = (MouseReleaseEvent) e;
            return new MouseButtonEvent(event.getButton(), null, "RELEASE", event.getX(), event.getY(), event.getTime());
        }else if (e instanceof org.dikhim.jclicker.eventmanager.event.MouseMoveEvent) {
            org.dikhim.jclicker.eventmanager.event.MouseMoveEvent event = (org.dikhim.jclicker.eventmanager.event.MouseMoveEvent) e;
            return new MouseMoveEvent(event.getX(), event.getY(), event.getTime());
        }else if (e instanceof MouseWheelUpEvent) {
            MouseWheelUpEvent event = (MouseWheelUpEvent) e;
            return new MouseWheelEvent("UP", event.getAmount(), event.getTime(), event.getX(), event.getY());
        }else if (e instanceof MouseWheelDownEvent ) {
            MouseWheelDownEvent  event = (MouseWheelDownEvent ) e;
            return new MouseWheelEvent("DOWN", event.getAmount(), event.getTime(), event.getX(), event.getY());
        }else if (e instanceof KeyPressEvent) {
            KeyPressEvent  event = (KeyPressEvent) e;
            return new KeyboardEvent(event.getKey(),null,"PRESS", event.getTime());
        }else if (e instanceof KeyReleaseEvent) {
            KeyReleaseEvent  event = (KeyReleaseEvent) e;
            return new KeyboardEvent(event.getKey(),null,"RELEASE", event.getTime());
        }
        return null;
    }

    public static List<Event> convertUselessEventToClickauto(List<org.dikhim.jclicker.eventmanager.event.Event> events) {
        return events.stream().map(EventsConverter::convertUselessEventToClickauto).collect(Collectors.toList());
    }
}
