package org.dikhim.jclicker.controllers.utils.recording;

import java.util.function.Consumer;

public abstract class StringRecorder extends SimpleRecorder {
    public StringRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    protected void putString(String string) {
        getOnRecorded().accept(string);
    }
}
