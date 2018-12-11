package functions;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.Serializable;

public class FunctionPoint implements Serializable, Cloneable{
    private double x,y;
    public FunctionPoint(double x, double y){
        this.x=x;
        this.y=y;
    }
    public FunctionPoint (FunctionPoint point){
        this.x=point.getx();
        this.y=point.gety();
    }
    public FunctionPoint(){
        this.x=0;
        this.y=0;
    }
    public DoubleProperty abscissaProperty() {return new SimpleDoubleProperty(x);}
    public Double getAbscissa(){ return x; }
    public DoubleProperty ordinateProperty() {return new SimpleDoubleProperty(y);}
    public Double getOrdinate(){ return y; }

    public double getx(){
        return x;
    }
    public double gety(){
        return y;
    }
    public void set (double x, double y){
        this.x=x;
        this.y=y;
    }
    public void setx(double x){ this.x=x; }
    public void sety(double y){ this.y=y; }
    public String toString(){
        StringBuilder tmp = new StringBuilder("(");
        tmp.append(x);
        tmp.append("; ");
        tmp.append(y);
        tmp.append(")");
        return tmp.toString();
    }
    public boolean equals(Object o){
        if(o instanceof FunctionPoint)
            return (((FunctionPoint) o).getx()==this.x&&((FunctionPoint) o).gety()==this.y)?true:false;
        return false;
    }
    public int hashCode(){
        long tmp=Double.doubleToLongBits(x);
        int hash=((int)((tmp&0xfffffffL)^(tmp>>32)));
        tmp=Double.doubleToLongBits(y);
        hash^=((int)((tmp&0xffffffffL)^(tmp>>32)));
        return hash;
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
};
