package org.dikhim.jclicker.controllers.utils.recording;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class SimpleRecorder implements Recorder {
    
    private Consumer onRecorded;
    private EventManager eventManager;
    private BooleanProperty recording;
    private List<EventListener> listeners = new ArrayList<>();

    public SimpleRecorder(Consumer onRecorded) {
        this.onRecorded = onRecorded;
        eventManager = Dependency.getEventManager();
        recording = new SimpleBooleanProperty(false);
    }

    protected void addListener(String name, EventListener listener) {
        listeners.add(listener);
        eventManager.addListener(name, listener);
    }

    protected void removeListener(String name) {
        eventManager.removeListener(name);
    }
    protected void removeListener(EventListener listener) {
        eventManager.removeListener(listener);
    }

    public boolean isRecording() {
        return recording.get();
    }

    public BooleanProperty recordingProperty() {
        return recording;
    }

    @Override
    public void start() {
        recording.set(true);
        onStart();
    }

    @Override
    public void stop() {
        recording.set(false);
        onStop();
    }
    
    protected abstract void onStart();

    protected  void onStop(){
        listeners.forEach(this::removeListener);
    }

    protected Consumer getOnRecorded() {
        return onRecorded;
    }
}
