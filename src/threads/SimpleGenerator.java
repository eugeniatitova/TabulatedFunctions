package threads;

import functions.basic.Log;

import static java.lang.Thread.sleep;

public class SimpleGenerator implements Runnable {
    Task task;
    public SimpleGenerator(Task task){
        this.task=task;
    }
    @Override public void run() {
        for (int i=0; i<task.getTasks(); i++){
            synchronized (task){
                try {Double base  =  Math.random()*(10 - 1) + 1;
                    task.setFunction(new Log(base));
                    task.setLeftX(Math.random()*100);
                    task.setRightX(Math.random()*(200 - 100) + 100);
                    task.setStep(Math.random());
                    System.out.printf("Source %s, %s, %s; \n", task.getLeftX(), task.getRightX(),task.getStep());
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
