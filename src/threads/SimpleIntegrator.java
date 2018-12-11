package threads;

import functions.Functions;

import static java.lang.Thread.sleep;

public class SimpleIntegrator implements Runnable {
    Task task;
    public SimpleIntegrator(Task task){
        this.task=task;
    }
    @Override public void run() {
            for (int i=0; i<task.getTasks(); i++){
                synchronized (this){
                    try {
                        System.out.printf("Result %s, %s, %s, %s; \n", task.getLeftX(), task.getRightX(),
                            task.getStep(), Functions.integral(task.getFunction(), task.getLeftX(),
                                    task.getRightX(), task.getStep()));
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
