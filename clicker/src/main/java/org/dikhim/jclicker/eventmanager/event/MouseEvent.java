package org.dikhim.jclicker.eventmanager.event;

public interface MouseEvent extends Event {
    /**
     * Returns the Y coordinate of the event
     *
     * @return X an integer indicating horizontal position of the pointer
     */
    int getX();

    /**
     * Returns the Y coordinate of the event.
     *
     * @return Y an integer indicating vertical position of the pointer
     */
    int getY();
}
