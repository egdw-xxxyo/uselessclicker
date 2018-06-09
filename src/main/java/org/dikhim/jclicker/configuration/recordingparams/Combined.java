package org.dikhim.jclicker.configuration.recordingparams;

import org.dikhim.jclicker.configuration.values.BooleanValue;
import org.dikhim.jclicker.configuration.values.IntegerValue;
import org.dikhim.jclicker.configuration.values.StringValue;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class Combined {
    private String name;

    private Preferences preferences;

    private StringValue controlKey;
    private StringValue encodingType;
    private IntegerValue fixedRate;
    private IntegerValue minDistance;
    private IntegerValue stopDetectionTime;
    private BooleanValue includeDelays;
    private BooleanValue includeKeyboard;
    private BooleanValue includeMouseButtons;
    private BooleanValue includeMouseWheel;
    private BooleanValue absolute;
    private BooleanValue relative;
    private BooleanValue fixedRateOn;
    private BooleanValue minDistanceOn;
    private BooleanValue stopDetectionOn;

    Combined(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        controlKey = new StringValue("controlKey", jsonObject.getString("controlKey"));
        encodingType = new StringValue("encodingType", jsonObject.getString("encodingType"));
        fixedRate = new IntegerValue("fixedRate", jsonObject.getInt("fixedRate"));
        minDistance = new IntegerValue("minDistance", jsonObject.getInt("minDistance"));
        stopDetectionTime = new IntegerValue("stopDetectionTime", jsonObject.getInt("stopDetectionTime"));
        includeDelays = new BooleanValue("includeDelays", jsonObject.getBoolean("includeDelays"));
        includeKeyboard = new BooleanValue("includeKeyboard", jsonObject.getBoolean("includeKeyboard"));
        includeMouseButtons = new BooleanValue("includeMouseButtons", jsonObject.getBoolean("includeMouseButtons"));
        includeMouseWheel = new BooleanValue("includeMouseWheel", jsonObject.getBoolean("includeMouseWheel"));
        absolute = new BooleanValue("absolute", jsonObject.getBoolean("absolute"));
        relative = new BooleanValue("relative", jsonObject.getBoolean("relative"));
        fixedRateOn = new BooleanValue("fixedRateOn", jsonObject.getBoolean("fixedRateOn"));
        minDistanceOn = new BooleanValue("minDistanceOn", jsonObject.getBoolean("minDistanceOn"));
        stopDetectionOn = new BooleanValue("stopDetectionOn", jsonObject.getBoolean("stopDetectionOn"));
    }


    public void setDefault() {
        controlKey.setDefault();
        encodingType.setDefault();
        fixedRate.setDefault();
        minDistance.setDefault();
        stopDetectionTime.setDefault();
        includeDelays.setDefault();
        includeKeyboard.setDefault();
        includeMouseButtons.setDefault();
        includeMouseWheel.setDefault();
        absolute.setDefault();
        relative.setDefault();
        fixedRateOn.setDefault();
        minDistanceOn.setDefault();
        stopDetectionOn.setDefault();
    }

    public void save() {
        controlKey.save(preferences);
        encodingType.save(preferences);
        fixedRate.save(preferences);
        minDistance.save(preferences);
        stopDetectionTime.save(preferences);
        includeDelays.save(preferences);
        includeKeyboard.save(preferences);
        includeMouseButtons.save(preferences);
        includeMouseWheel.save(preferences);
        absolute.save(preferences);
        relative.save(preferences);
        fixedRateOn.save(preferences);
        minDistanceOn.save(preferences);
        stopDetectionOn.save(preferences);
    }

    public void loadOrSetDefault() {
        controlKey.loadOrSetDefault(preferences);
        encodingType.loadOrSetDefault(preferences);
        fixedRate.loadOrSetDefault(preferences);
        minDistance.loadOrSetDefault(preferences);
        stopDetectionTime.loadOrSetDefault(preferences);
        includeDelays.loadOrSetDefault(preferences);
        includeKeyboard.loadOrSetDefault(preferences);
        includeMouseButtons.loadOrSetDefault(preferences);
        includeMouseWheel.loadOrSetDefault(preferences);
        absolute.loadOrSetDefault(preferences);
        relative.loadOrSetDefault(preferences);
        fixedRateOn.loadOrSetDefault(preferences);
        minDistanceOn.loadOrSetDefault(preferences);
        stopDetectionOn.loadOrSetDefault(preferences);
    }

    //

    public String getName() {
        return name;
    }

    public StringValue getControlKeyValue() {
        return controlKey;
    }

    public String getControlKey() {
        return controlKey.get();
    }

    public StringValue getEncodingTypeValue() {
        return encodingType;
    }

    public String getEncodingType() {
        return encodingType.get();
    }

    public IntegerValue getFixedRateValue() {
        return fixedRate;
    }

    public int getFixedRate() {
        return fixedRate.get();
    }

    public IntegerValue getMinDistanceValue() {
        return minDistance;
    }

    public int getMinDistance() {
        return minDistance.get();
    }

    public IntegerValue getStopDetectionTimeValue() {
        return stopDetectionTime;
    }

    public int getStopDetectionTime() {
        return stopDetectionTime.get();
    }

    public BooleanValue getIncludeDelaysValue() {
        return includeDelays;
    }

    public boolean isDelaysIncluded() {
        return includeDelays.get();
    }

    public BooleanValue getIncludeKeyboardValue() {
        return includeKeyboard;
    }

    public boolean isKeysIncluded() {
        return includeKeyboard.get();
    }

    public BooleanValue getIncludeMouseButtonsValue() {
        return includeMouseButtons;
    }

    public boolean isMouseButtonsIncluded() {
        return includeMouseButtons.get();
    }

    public BooleanValue getIncludeMouseWheelValue() {
        return includeMouseWheel;
    }

    public boolean isMouseWheelIncluded() {
        return includeMouseWheel.get();
    }

    public BooleanValue getAbsoluteValue() {
        return absolute;
    }

    public boolean isAbsolute() {
        return absolute.get();
    }

    public BooleanValue getRelative() {
        return relative;
    }

    public boolean isRelative() {
        return relative.get();
    }

    public BooleanValue getFixedRateOnValue() {
        return fixedRateOn;
    }

    public boolean isFixedRateOn() {
        return fixedRateOn.get();
    }

    public BooleanValue getMinDistanceOnValue() {
        return minDistanceOn;
    }

    public boolean isMinDistanceOn() {
        return minDistanceOn.get();
    }

    public BooleanValue getStopDetectionOnValue() {
        return stopDetectionOn;
    }

    public boolean isStopDetectionOn() {
        return includeKeyboard.get();
    }

}
