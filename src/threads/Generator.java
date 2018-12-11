package threads;

import functions.basic.Log;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    Task task;
    Semaphore semaphore;
    public Generator(Task task, Semaphore semaphore){
        this.task=task;
        this.semaphore=semaphore;
    }
    public void run() {
        try {
            semaphore.acquire();
            for (int i=0; i<task.getTasks(); i++) {
                Double base = Math.random() * (10 - 1) + 1;
                task.setFunction(new Log(base));
                task.setLeftX(Math.random() * 100);
                task.setRightX(Math.random() * (200 - 100) + 100);
                task.setStep(Math.random());
                System.out.printf("Source %s, %s, %s; \n", task.getLeftX(), task.getRightX(), task.getStep());
                sleep(15);
                semaphore.release();

            }
        }
        catch (InterruptedException e) {
                    e.printStackTrace();
                }
    }
}
