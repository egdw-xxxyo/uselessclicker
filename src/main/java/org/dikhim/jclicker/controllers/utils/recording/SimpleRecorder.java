package org.dikhim.jclicker.controllers.utils.recording;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

import java.util.function.Consumer;

public abstract class SimpleRecorder implements Recorder {
    private Consumer<String> onRecorded;
    private EventManager eventManager;
    private BooleanProperty recording;

    public SimpleRecorder(Consumer<String> onRecorded) {
        this.onRecorded = onRecorded;
        eventManager = Dependency.getEventManager();
        recording = new SimpleBooleanProperty(false);
    }

    protected void addListener(String name, EventListener listener) {
        eventManager.addListener(name, listener);
    }

    protected void removeListener(String name) {
        eventManager.removeListener(name);
    }

    protected void putCode(String code) {
        onRecorded.accept(code);
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

    protected abstract void onStop();
}
