package threads;

import functions.Functions;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    Task task;
    Semaphore semaphore;
    public Integrator(Task task, Semaphore semaphore){
        this.task=task;
        this.semaphore=semaphore;
    }
    public void run() {
        try {
            semaphore.acquire();
            for (int i=0; i<task.getTasks(); i++){
                System.out.printf("Result %s, %s, %s, %s; \n", task.getLeftX(), task.getRightX(),
                        task.getStep(), Functions.integral(task.getFunction(), task.getLeftX(),
                                task.getRightX(), task.getStep()));
                sleep(15);
                semaphore.release();
                }
        }
        catch (InterruptedException e) {
                    e.printStackTrace();
                }
    }
}
