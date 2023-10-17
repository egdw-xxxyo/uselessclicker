package org.dikhim.clickauto.jsengine.objects;

public class LinearMethod implements AnimationMethod {
    @Override
    public double transform(double param) {
        if(param< 0 || param > 1) throw new IllegalArgumentException("Param should be between 0 and 1");
        return param;
    }
}
