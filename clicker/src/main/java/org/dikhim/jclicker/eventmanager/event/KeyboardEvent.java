package org.dikhim.jclicker.eventmanager.event;

public interface KeyboardEvent extends Event {
    /**
     * Returns the name of the key that triggered an event
     *
     * @return name of the key
     */
    String getKey();
}
