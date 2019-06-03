package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.WindowManager;

import java.io.File;
import java.util.function.Consumer;

public class FilePathRecorder extends StringRecorder {
    public FilePathRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    protected void onStart() {
        File file = WindowManager.getInstance().openFile();
        if (file != null) {
            putString(file.getAbsolutePath());
        }
    }
}
