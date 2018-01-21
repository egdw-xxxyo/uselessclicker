package org.dikhim.jclicker.configuration.recordingparams;

import org.dikhim.jclicker.configuration.values.BooleanValue;
import org.dikhim.jclicker.configuration.values.IntegerValue;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class Combined {
    private String name;

    private Preferences preferences;

    private IntegerValue fixedRate;
    private IntegerValue minDistance;
    private IntegerValue stopDetectionTime;
    private BooleanValue includeDelays;
    private BooleanValue includeKeyboard;
    private BooleanValue includeMouseButtons;
    private BooleanValue includeMouseWheel;
    private BooleanValue includeMouseMovement;
    private BooleanValue absolute;
    private BooleanValue fixedRateOn;
    private BooleanValue minDistanceOn;
    private BooleanValue stopDetectionOn;

    public Combined(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        fixedRate = new IntegerValue("fixedRate", jsonObject.getInt("fixedRate"));
        minDistance = new IntegerValue("minDistance", jsonObject.getInt("minDistance"));
        stopDetectionTime = new IntegerValue("stopDetectionTime", jsonObject.getInt("stopDetectionTime"));
        includeDelays = new BooleanValue("includeDelays", jsonObject.getBoolean("includeDelays"));
        includeKeyboard = new BooleanValue("includeKeyboard", jsonObject.getBoolean("includeKeyboard"));
        includeMouseButtons = new BooleanValue("includeMouseButtons", jsonObject.getBoolean("includeMouseButtons"));
        includeMouseWheel = new BooleanValue("includeMouseWheel", jsonObject.getBoolean("includeMouseWheel"));
        includeMouseMovement = new BooleanValue("includeMouseMovement", jsonObject.getBoolean("includeMouseMovement"));
        absolute = new BooleanValue("absolute", jsonObject.getBoolean("absolute"));
        fixedRateOn = new BooleanValue("fixedRateOn", jsonObject.getBoolean("fixedRateOn"));
        minDistanceOn = new BooleanValue("minDistanceOn", jsonObject.getBoolean("minDistanceOn"));
        stopDetectionOn = new BooleanValue("stopDetectionOn", jsonObject.getBoolean("stopDetectionOn"));
    }


    public void setDefault() {
        fixedRate.setDefault();
        minDistance.setDefault();
        stopDetectionTime.setDefault();
        includeDelays.setDefault();
        includeKeyboard.setDefault();
        includeMouseButtons.setDefault();
        includeMouseWheel.setDefault();
        includeMouseMovement.setDefault();
        absolute.setDefault();
        fixedRateOn.setDefault();
        minDistanceOn.setDefault();
        stopDetectionOn.setDefault();
    }

    public void save() {
        fixedRate.save(preferences);
        minDistance.save(preferences);
        stopDetectionTime.save(preferences);
        includeDelays.save(preferences);
        includeKeyboard.save(preferences);
        includeMouseButtons.save(preferences);
        includeMouseWheel.save(preferences);
        includeMouseMovement.save(preferences);
        absolute.save(preferences);
        fixedRateOn.save(preferences);
        minDistanceOn.save(preferences);
        stopDetectionOn.save(preferences);
    }

    public void loadOrSetDefault() {
        fixedRate.loadOrSetDefault(preferences);
        minDistance.loadOrSetDefault(preferences);
        stopDetectionTime.loadOrSetDefault(preferences);
        includeDelays.loadOrSetDefault(preferences);
        includeKeyboard.loadOrSetDefault(preferences);
        includeMouseButtons.loadOrSetDefault(preferences);
        includeMouseWheel.loadOrSetDefault(preferences);
        includeMouseMovement.loadOrSetDefault(preferences);
        absolute.loadOrSetDefault(preferences);
        fixedRateOn.loadOrSetDefault(preferences);
        minDistanceOn.loadOrSetDefault(preferences);
        stopDetectionOn.loadOrSetDefault(preferences);
    }

    //

    public String getName() {
        return name;
    }

    public IntegerValue getFixedRateValue() {
        return fixedRate;
    }

    public IntegerValue getMinDistanceValue() {
        return minDistance;
    }

    public IntegerValue getStopDetectionTimeValue() {
        return minDistance;
    }

    public BooleanValue getIncludeDelaysValue() {
        return includeDelays;
    }

    public BooleanValue getIncludeKeyboardValue() {
        return includeKeyboard;
    }

    public BooleanValue getIncludeMouseButtonsValue() {
        return includeMouseButtons;
    }

    public BooleanValue getIncludeMouseWheelValue() {
        return includeMouseWheel;
    }

    public BooleanValue getIncludeMouseMovementValue() {
        return includeMouseMovement;
    }

    public BooleanValue getAbsoluteValue() {
        return absolute;
    }

    public BooleanValue getFixedRateOnValue() {
        return fixedRateOn;
    }

    public BooleanValue getMinDistanceOnValue() {
        return minDistanceOn;
    }

    public BooleanValue getStopDetectionOnValue() {
        return stopDetectionOn;
    }

}
