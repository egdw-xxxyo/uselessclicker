package org.dikhim.jclicker.configuration.storage;

import javafx.beans.property.*;

import java.util.HashMap;
import java.util.Map;

public class Storage  {
    private final Map<String, Object> storage = new HashMap<>();

    private final CombinedRecordingParams combinedRecordingParams = new CombinedRecordingParams();
    
    public void put(String key, Object object) {
        storage.put(key, object);
    }

    public Object get(String key) {
        return storage.get(key);
    }

    public StringProperty getStringProperty(String key) {
        return (StringProperty) get(key);
    }

    public IntegerProperty getImategerProperty(String key) {
        return (IntegerProperty) get(key);
    }

    public BooleanProperty getBooleanProperty(String key) {
        return (BooleanProperty) get(key);
    }
    
    public CombinedRecordingParams combinedRecordingParams() {
        return combinedRecordingParams;
    }
}
