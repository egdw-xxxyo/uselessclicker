package org.dikhim.jclicker.eventmanager.event;

public class KeyPressEvent implements KeyboardEvent {
    private final String key;
    private final long time;

    public KeyPressEvent(String key, long time) {
        this.key = key;
        this.time = time;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "KeyPressEvent{" +
                "key='" + key + '\'' +
                ", time=" + time +
                '}';
    }
}
