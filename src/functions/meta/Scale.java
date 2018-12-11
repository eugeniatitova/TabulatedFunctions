package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function f;
    private double sx, sy;
    public Scale(Function f, double x, double y){
        this.f=f;
        this.sx=x;
        this.sy=y;
    }
    @Override
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder()*sx;
    }

    @Override
    public double getRightDomainBorder() {
        return f.getRightDomainBorder()*sx;
    }

    @Override
    public double getFunctionValue(double x) {
        return f.getFunctionValue(x)*sy;
    }
}
