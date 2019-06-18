package org.dikhim.jclicker.eventmanager.event;

public interface MouseWheelEvent extends MouseEvent {
    /**
     * Returns the number of units that should be scrolled per click of mouse wheel rotation.
     *
     * @return number of units to scroll
     */
    int getAmount();
}
