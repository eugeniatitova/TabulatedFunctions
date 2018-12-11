package functions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction<T> implements TabulatedFunction, Function, Serializable, Cloneable, Iterable{

    private FunctionPoint[] data;
    private int size;

    public ArrayTabulatedFunction (double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        size = pointsCount;
        if (leftX>=rightX || size<2)
            throw new IllegalArgumentException();
        data = new FunctionPoint[(int)(size*1.5)];
        double step= (rightX-leftX)/(size-1);
        for (int i=0; i<size; ++i)
            data[i]=new FunctionPoint(leftX+i*step, 0);
    }
    public ArrayTabulatedFunction (double leftX, double rightX, double[] values) throws IllegalArgumentException {
        size = values.length;
        if (leftX>=rightX || size<2)
            throw new IllegalArgumentException();
        data = new FunctionPoint[(int)(size*1.5)];
        double step= (rightX-leftX)/(size-1);
        for (int i=0; i<size; i++)
            data[i]=new FunctionPoint(leftX+i*step, values[i]);
    }
    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        size=points.length;
        boolean sort=true;
        for (int i=1; i<size; i++){
            if (points[i-1].getx()>=points[i].getx())
                sort=false;
        }
        if (size<2 || sort==false)
            throw new IllegalArgumentException();
        data = new FunctionPoint[(int)(size*1.5)];
        for (int i=0; i<size; i++)
            data[i]=points[i];
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

        @Override public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }
        @Override public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }
        @Override public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    };

    @Override public double getLeftDomainBorder(){
        return data[0].getx();
    }
    @Override public double getRightDomainBorder(){
        return data[size-1].getx();
    }
    @Override public double getFunctionValue (double x){
        for (int i=0; i<size; i++)
            if (data[i].getx()==x) return data[i].gety();
        if (this.getLeftDomainBorder()<=x && x<=this.getRightDomainBorder()){
            return ((x-data[0].getx())*(data[size-1].gety()-data[0].gety())
                    /(data[size-1].getx()-data[0].getx())+data[0].gety());}
        else return Double.NaN;
    }



    @Override public int getPointsCount(){
        return size;
    }
    @Override public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        return data[index];
    }
    @Override public void setPoint (int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        if (point.getx()<=data[index-1].getx()|| data[index+1].getx()<=point.getx())
            throw new InappropriateFunctionPointException();
        data[index].setx(point.getx());
        data[index].sety(point.gety());
        return;
    }
    @Override public double getPointX (int index) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        return data[index].getx();
    }
    @Override public void setPointX (int index, double x)
            throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        if (x<=data[index-1].getx()|| data[index+1].getx()<=x)
            throw new InappropriateFunctionPointException();
        data[index].setx(x);
        return;
    }
    @Override public double getPointY (int index) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        return data[index].gety();
    }
    @Override public void setPointY(int index, double y)throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        data[index].sety(y);
        return;
    }
    @Override public void deletePoint (int index)throws IllegalStateException,
            FunctionPointIndexOutOfBoundsException {
        if (size<3) throw new IllegalStateException();
        if (index<0 || size<=index) throw new FunctionPointIndexOutOfBoundsException();
        if (index == 0) {
            for (int i = 0; i < size-1; i++)
                data[i] = data[i++];
        }
        if (index != size - 1) {
            for (int i = index; i < size; i++)
                data[i] = data[i+1];
        }
        size--;
        return;
    }
    @Override public void addPoint (FunctionPoint point)throws InappropriateFunctionPointException {
        if (data.length==size){
            FunctionPoint[] newdata = new FunctionPoint[(int)(size*1.5)];
            System.arraycopy(data, 0, newdata,0, size);
            data=newdata;
        }
        if (point.getx() > data[size - 1].getx()) {
            data[size] = new FunctionPoint(point);
            size++;
            return;
        }
        int pos = 0;
        while (point.getx() >= data[pos].getx())
            if (point.getx() == data[pos].getx())
                throw new InappropriateFunctionPointException();
            else pos++;
        for(int i=size; i>pos; i--)
            data[i]=data[i-1];
        data[pos]= new FunctionPoint(point);
        size++;
        return;
    }
    public String toString(){
        StringBuilder tmp =new StringBuilder("{");
        for (int i=0; i<getPointsCount()-1; i++){
            tmp.append(data[i].toString());
            tmp.append(", ");
        }
        tmp.append(data[getPointsCount()-1]);
        tmp.append("}");
        return tmp.toString();
    }
    public boolean equals(Object o){
        if(o instanceof TabulatedFunction){
            boolean flag=true;
            if (((TabulatedFunction) o).getPointsCount()!=size) return false;
            for (int i=0; i<size; i++){
                if (!data[i].equals(((TabulatedFunction) o).getPoint(i)))
                   flag=false;
                }
                return flag?true:false;
        }
        return false;
    }
    public int hashCode(){
        int hash = size;
        for(int i=0; i<size; i++)
            hash^=data[i].hashCode();
        return hash;
    }
    @Override public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] clonedata = new FunctionPoint[(int)(size*1.5)];
        for(int i=0; i<size; i++)
            clonedata[i]=(FunctionPoint)data[i].clone();
        ArrayTabulatedFunction clone = (ArrayTabulatedFunction) super.clone();
        clone.data=clonedata;
        return (Object) clone;
    }

    @Override public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {
            private FunctionPoint curr;
            private int index=0;

            @Override public boolean hasNext() {
                return index<size;
            }
            @Override public T next() {
                if (!this.hasNext()) throw new NoSuchElementException();
                curr = data[index++];
                T value = (T) curr;
                return value;
            }
            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }
};