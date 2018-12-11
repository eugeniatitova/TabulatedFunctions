package functions.meta;

import functions.Function;

public class Mult implements Function {
    private Function f, g;
    public Mult(Function f, Function g){
        this.f=f;
        this.g=g;
    }
    @Override
    public double getLeftDomainBorder() {
        return Double.max(f.getLeftDomainBorder(), g.getLeftDomainBorder());
    }

    @Override
    public double getRightDomainBorder() {
        return Double.min(f.getLeftDomainBorder(), g.getLeftDomainBorder());
    }

    @Override
    public double getFunctionValue(double x) {
        return f.getFunctionValue(x)*g.getFunctionValue(x);
    }
}
