package org.dikhim.clickauto.jsengine.utils.decoders;


import static org.dikhim.clickauto.jsengine.utils.encoders.AbstractActionEncoder.*;

public abstract class AbstractActionDecoder implements ActionDecoder {

    protected boolean validateKey(String key) {
        return !key.isEmpty();
    }

    protected boolean validateDelaySeconds(int value) {
        return value >= 0 && value <= MAX_DELAY_SECONDS;
    }

    protected boolean validateDelayMilliseconds(int value) {
        return value >= 0 && value <= MAX_DELAY_MILLISECONDS;
    }

    protected boolean validateCoordinate(int value) {
        return value >= 0 && value <= MAX_COORDINATE;
    }
    protected boolean validateDifferenceCoordinate(int value) {
        return value >= MIN_D_COORDINATE && value <= MAX_D_COORDINATE;
    }

    protected boolean validateWheelAmount(int value) {
        return value >= 0 && value < MAX_WHEEL_AMOUNT;
    }
}
