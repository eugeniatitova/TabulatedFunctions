package functions.basic;

public class Sin extends TrigonometricFunction {
    public Sin(){}

    public double getLeftDomainBorder() {
        return super.getLeftDomainBorder();
    }
    public double getRightDomainBorder() {
        return super.getRightDomainBorder();
    }
    public double getFunctionValue(double x) {
        return Math.sin(x);
    }
}
