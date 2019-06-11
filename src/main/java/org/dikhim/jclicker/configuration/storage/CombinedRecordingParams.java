package org.dikhim.jclicker.configuration.storage;

import javafx.beans.property.*;

public class CombinedRecordingParams {
    private final StringProperty encodingType = new SimpleStringProperty("base64-zip");
    private final IntegerProperty fixedRate = new SimpleIntegerProperty(60);
    private final IntegerProperty minDistance = new SimpleIntegerProperty(10);
    private final IntegerProperty stopDetectionTime = new SimpleIntegerProperty(70);
    private final BooleanProperty includeKeyboard = new SimpleBooleanProperty(true);
    private final BooleanProperty includeMouseButtons = new SimpleBooleanProperty(true);
    private final BooleanProperty includeMouseWheel = new SimpleBooleanProperty(true);
    private final BooleanProperty includeDelays = new SimpleBooleanProperty(true);
    private final BooleanProperty absolute = new SimpleBooleanProperty(true);
    private final BooleanProperty relative = new SimpleBooleanProperty(false);
    private final BooleanProperty fixedRateOn = new SimpleBooleanProperty(false);
    private final BooleanProperty minDistanceOn = new SimpleBooleanProperty(false);
    private final BooleanProperty stopDetectionOn = new SimpleBooleanProperty(false);

    public String getEncodingType() {
        return encodingType.get();
    }

    public StringProperty encodingTypeProperty() {
        return encodingType;
    }

    public int getFixedRate() {
        return fixedRate.get();
    }

    public IntegerProperty fixedRateProperty() {
        return fixedRate;
    }

    public int getMinDistance() {
        return minDistance.get();
    }

    public IntegerProperty minDistanceProperty() {
        return minDistance;
    }

    public int getStopDetectionTime() {
        return stopDetectionTime.get();
    }

    public IntegerProperty stopDetectionTimeProperty() {
        return stopDetectionTime;
    }

    public boolean isIncludeKeyboard() {
        return includeKeyboard.get();
    }

    public BooleanProperty includeKeyboardProperty() {
        return includeKeyboard;
    }

    public boolean isIncludeMouseButtons() {
        return includeMouseButtons.get();
    }

    public BooleanProperty includeMouseButtonsProperty() {
        return includeMouseButtons;
    }

    public boolean isIncludeMouseWheel() {
        return includeMouseWheel.get();
    }

    public BooleanProperty includeMouseWheelProperty() {
        return includeMouseWheel;
    }

    public boolean isIncludeDelays() {
        return includeDelays.get();
    }

    public BooleanProperty includeDelaysProperty() {
        return includeDelays;
    }

    public boolean isAbsolute() {
        return absolute.get();
    }

    public BooleanProperty absoluteProperty() {
        return absolute;
    }

    public boolean isRelative() {
        return relative.get();
    }

    public BooleanProperty relativeProperty() {
        return relative;
    }

    public boolean isFixedRateOn() {
        return fixedRateOn.get();
    }

    public BooleanProperty fixedRateOnProperty() {
        return fixedRateOn;
    }

    public boolean isMinDistanceOn() {
        return minDistanceOn.get();
    }

    public BooleanProperty minDistanceOnProperty() {
        return minDistanceOn;
    }

    public boolean isStopDetectionOn() {
        return stopDetectionOn.get();
    }

    public BooleanProperty stopDetectionOnProperty() {
        return stopDetectionOn;
    }
}
