package org.dikhim.jclicker.eventmanager.event;

public class KeyReleaseEvent implements  KeyboardEvent {
    private String key;
    private long time;
    
    public KeyReleaseEvent(String key, long time) {
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
        return "KeyReleaseEvent{" +
                "key='" + key + '\'' +
                ", time=" + time +
                '}';
    }
}
