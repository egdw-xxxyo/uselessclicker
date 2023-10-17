package org.dikhim.clickauto.jsengine.objects;

public class AcceleratingMethod implements AnimationMethod {
    @Override
    public double transform(double param) {
        if (param < 0 || param > 1) throw new IllegalArgumentException("Param should be between 0 and 1");
        double result = -1 / (param - 1.09) / 10 - 0.09;

        if (result < 0.01) result = 0;
        if (result > 0.99) result = 1;
        return result;
    }
}
