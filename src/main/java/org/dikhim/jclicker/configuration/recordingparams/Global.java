package org.dikhim.jclicker.configuration.recordingparams;

import org.dikhim.jclicker.configuration.values.IntegerValue;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class Global {
    private String name;

    private Preferences preferences;

    private IntegerValue fixedRate;
    private IntegerValue minDistance;
    private IntegerValue stopDetectionTime;

    public Global(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        fixedRate = new IntegerValue("fixedRate", jsonObject.getInt("fixedRate"));
        minDistance = new IntegerValue("minDistance", jsonObject.getInt("minDistance"));
        stopDetectionTime = new IntegerValue("stopDetectionTime", jsonObject.getInt("stopDetectionTime"));
    }


    public void setDefault() {
        fixedRate.setDefault();
        minDistance.setDefault();
        stopDetectionTime.setDefault();
    }

    public void save() {
        fixedRate.save(preferences);
        minDistance.save(preferences);
        stopDetectionTime.save(preferences);
    }

    public void loadOrSetDefault() {
        fixedRate.loadOrSetDefault(preferences);
        minDistance.loadOrSetDefault(preferences);
        stopDetectionTime.loadOrSetDefault(preferences);
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
}
