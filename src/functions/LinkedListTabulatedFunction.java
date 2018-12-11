package functions;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction<T> implements TabulatedFunction, Function, Cloneable, Serializable, Iterable {

    class FunctionNode implements Serializable {
    FunctionPoint element;
    FunctionNode next;
    FunctionNode prev;

    FunctionNode(){
        this.element=new FunctionPoint();
        this.next=this.prev=null;
    }
    FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) {
        this.element = new FunctionPoint(point);
        this.prev=prev;
        this.next=next;
    }
}
    private int size;
    private FunctionNode head;

    public LinkedListTabulatedFunction (double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException {
        head=new FunctionNode();
        size=0;
        head.next=head.prev=head;
        if (leftX>=rightX || pointsCount<2)
            throw new IllegalArgumentException();
        double step= (rightX-leftX)/(pointsCount-1);
        for (int i=0; i<pointsCount; ++i)
            this.addNodeToTail(new FunctionPoint(leftX+i*step, 0));
    }
    public LinkedListTabulatedFunction (double leftX, double rightX, double[] values)
            throws IllegalArgumentException {
        head=new FunctionNode();
        size=0;
        head.next=head.prev=head;
        if (leftX>=rightX || values.length<2)
            throw new IllegalArgumentException();
        double step= (rightX-leftX)/(values.length-1);
        for (int i=0; i<values.length; i++)
            this.addNodeToTail(new FunctionPoint(leftX+i*step, values[i]));
    }
    public LinkedListTabulatedFunction (FunctionPoint[] points) throws IllegalArgumentException {
        head=new FunctionNode();
        head.next=head.prev=head;
        boolean sort=true;
        for (int i=1; i<points.length; i++){
            if (points[i-1].getx()>=points[i].getx())
                sort=false;
        }
        if (sort==false)
            throw new IllegalArgumentException();
        for (int i=0; i<points.length; i++)
            this.addNodeToTail(points[i]);
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory{

        @Override public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }
        @Override public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }
        @Override public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }
    }
    FunctionNode getNodeByIndex(int index){
        FunctionNode node = head.next;
        for (int i=0; i<index; i++)
            node=node.next;
        return node;
    }
    FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode node = new FunctionNode(point, head.prev, head);
        head.prev.next=head.prev=node;
        size++;
        return node;
    }
    FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        FunctionNode iterator = head;
        for (int i=0; i<=index; i++)
            iterator=iterator.next;
        FunctionNode node = new FunctionNode(point, iterator.prev ,iterator);
        iterator.prev.next=iterator.prev=node;
        size++;
        return node;
    }
    FunctionNode deleteNodeByIndex(int index) {
        FunctionNode iterator = head;
        for (int i=0; i<=index; i++) {
            iterator=iterator.next;
        }
        iterator.prev.next=iterator.next;
        iterator.next.prev=iterator.prev;
        size--;
        return iterator;
    }
    @Override public double getLeftDomainBorder(){ return head.next.element.getx(); }
    @Override public double getRightDomainBorder(){
        return head.prev.element.getx();
    }
    @Override public double getFunctionValue (double x){
        for (FunctionNode i = head.next; i!=head; i=i.next)
            if (i.element.getx()==x) return i.element.gety();
        if (this.getLeftDomainBorder()<=x && x<=this.getRightDomainBorder()){
            return ((x-head.next.element.getx())*(head.prev.element.gety()
                    -head.next.element.gety()) /(head.prev.element.getx()
                    -head.next.element.getx())+head.next.element.gety());}
        else return Double.NaN;
    }


    @Override public int getPointsCount(){
        return size;
    }
    @Override public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        return this.getNodeByIndex(index).element;
    }
    @Override public void setPoint (int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        FunctionNode iterator=head.next;
        for(int i=0; i<index; i++)
            iterator=iterator.next;
        if (point.getx()<=iterator.prev.element.getx() || iterator.next.element.getx()<=point.getx())
            throw new InappropriateFunctionPointException();
        iterator.element.setx(point.getx());
        iterator.element.sety(point.gety());
        return;
    }
    @Override public double getPointX (int index) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        return this.getNodeByIndex(index).element.getx();
    }
    @Override public void setPointX (int index, double x)
            throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        FunctionNode iterator=head;
        for(int i=0; i<index; i++)
            iterator=iterator.next;
        if (x<=iterator.prev.element.getx()|| iterator.next.element.getx()<=x)
            throw new InappropriateFunctionPointException();
        iterator.element.setx(x);
        return;
    }
    @Override public double getPointY (int index) throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        return this.getNodeByIndex(index).element.gety();
    }
    @Override public void setPointY(int index, double y)throws FunctionPointIndexOutOfBoundsException {
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        this.getNodeByIndex(index).element.sety(y);
        return;
    }
    @Override public void deletePoint (int index)throws IllegalStateException,
            FunctionPointIndexOutOfBoundsException {
        if (size<3)
            throw new IllegalStateException();
        if (index<0 || size<=index)
            throw new FunctionPointIndexOutOfBoundsException();
        this.deleteNodeByIndex(index);
        return;
    }
    @Override public void addPoint (FunctionPoint point)throws InappropriateFunctionPointException {
        if (point.getx() > this.getRightDomainBorder()) {
            this.addNodeToTail(point);
            return;
        }
        int pos = 0;
        FunctionNode iterator=head.next;
        while (point.getx() >= iterator.element.getx())
            if (point.getx() == iterator.element.getx())
                throw new InappropriateFunctionPointException();
            else {
                pos++;
                iterator=iterator.next;
            }
        this.addNodeByIndex(pos, point);
        return;
    }
    public String toString(){
        StringBuilder tmp =new StringBuilder("{");
        for ( FunctionNode i=head.next; i!=head.prev;i=i.next){
            tmp.append(i.element.toString());
            tmp.append(", ");
        }
        tmp.append(getNodeByIndex(getPointsCount()-1).element.toString());
        tmp.append("}");
        return tmp.toString();
    }
    public boolean equals(Object o){
        if(o instanceof TabulatedFunction){
            if (((TabulatedFunction) o).getPointsCount()!=size) return false;
            boolean flag=true;
            int i=0;
            for ( FunctionNode it=head.next; it!=head;it=it.next, i++){
               if (!it.element.equals(((TabulatedFunction) o).getPoint(i)))
                    flag=false;
            }
            return flag?true:false;
        }
        return false;
    }
    public int hashCode(){
        int hash = size;
        for( FunctionNode i=head.next; i!=head;i=i.next)
           hash^=i.element.hashCode();
        return hash;
    }
    @Override public Object clone() throws CloneNotSupportedException {
        Object clone = (LinkedListTabulatedFunction) super.clone();
        try {
            FileOutputStream fos = new FileOutputStream(String.valueOf(hashCode()));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            FileInputStream fis = new FileInputStream(String.valueOf(hashCode()));
            ObjectInputStream oin = new ObjectInputStream(fis);
            clone = oin.readObject();
            oin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clone;
    }
    @Override public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>(){

            private FunctionNode curr=head;

            @Override public boolean hasNext() {
                return curr!=head.prev;
            }
            @Override public T next() {
                if (!this.hasNext()) throw new NoSuchElementException();
                curr = curr.next;
                T value = (T) curr.element;

                return value;
            }
            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }
}

