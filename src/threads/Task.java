package threads;

import functions.Function;

public class Task {

    private Function function;
    private Double leftX, rightX;
    private Double step;
    private int tasks;

    public Task(Function function, double leftX, double rightX, double step, int tasks){
        this.function=function;
        this.leftX=leftX;
        this.rightX= rightX;
        this.step=step;
        this.tasks = tasks;
    }

    public void setFunction(Function function){
        synchronized (this){
        this.function= function; }
    }
    public void setLeftX(double leftX ){
        synchronized (this) {
            this.leftX = leftX;
        }
    }
    public void setRightX(double rightX){
        synchronized (this) {
            this.rightX = rightX;
        }
    }
    public void setStep (double step){
        synchronized (this) {
            this.step = step;
        }
    }
    public int getTasks(){
        return tasks;
    }
    public Function getFunction(){
        return function;
    }
    public double getLeftX(){
        return leftX;
    }
    public double getRightX(){
        return rightX;
    }
    public double getStep (){
        return step;
    }

}
