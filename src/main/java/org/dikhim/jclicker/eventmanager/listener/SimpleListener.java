package org.dikhim.jclicker.eventmanager.listener;

public class SimpleListener implements EventListener {
    String id;

    public SimpleListener(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
