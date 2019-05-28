package org.dikhim.jclicker.eventmanager.event;

public interface MouseButtonEvent extends MouseEvent {
    /**
     * Returns the name of a mouse button that triggered an event
     *
     * @return name of the mouse button
     */
    String getButton();
}
