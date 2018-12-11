package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function f, g;
    public Composition(Function f, Function g){
        this.f=f;
        this.g=g;
    }
    @Override
    public double getLeftDomainBorder() {
        return g.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return g.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return f.getFunctionValue(g.getFunctionValue(x));
    }
}
