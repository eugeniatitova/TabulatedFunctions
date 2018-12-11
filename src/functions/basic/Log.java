package functions.basic;

import functions.Function;
import gui.FunctionLoader;

public class Log implements Function {
    private double base;
    public Log (){
        this.base= FunctionLoader.getLogBase();
    }
    @Override public double getLeftDomainBorder() {
        return 0;
    }
    @Override public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    @Override public double getFunctionValue(double x) {
        return Math.log(x)/ Math.log(base);
    }
    public Log(double base){
        this.base=base;
    }
}
