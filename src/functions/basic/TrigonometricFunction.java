package functions.basic;

import functions.Function;

public class TrigonometricFunction implements Function {
    TrigonometricFunction(){ }

    @Override public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }
    @Override public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    @Override public double getFunctionValue(double x) {
        return this.getFunctionValue(x);
    }

}
