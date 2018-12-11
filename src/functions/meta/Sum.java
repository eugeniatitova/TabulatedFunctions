package functions.meta;

import functions.Function;

public class Sum implements Function {
    Function f,g;
    public Sum (Function f, Function g){
        this.f=f;
        this.g=g;
    }

    @Override
    public double getLeftDomainBorder() {
        return Double.max(f.getLeftDomainBorder(), g.getLeftDomainBorder());
    }

    @Override
    public double getRightDomainBorder() {
        return Double.min(f.getRightDomainBorder(), g.getRightDomainBorder());
    }

    @Override
    public double getFunctionValue(double x) {
        return f.getFunctionValue(x)+g.getFunctionValue(x);
    }
}
