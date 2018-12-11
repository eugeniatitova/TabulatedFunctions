import functions.*;
import functions.basic.Cos;
import functions.basic.Log;
import functions.basic.Sin;
import threads.*;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        nonThread();
        simpleThreads();
        sleep(1550);
        complicatedThreads();
        sleep(1550);
        TabulatedFunction<FunctionPoint> y = new LinkedListTabulatedFunction(0,9,10);
        for (FunctionPoint p : y) System.out.println(p);
        Function cos = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(),
                        new FunctionPoint(1, 1),
                        new FunctionPoint(2,2)
                }
        );
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);
    }

    public static void nonThread(){
        System.out.println("NonThread calculation:");
        Double base  =  Math.random()*(10 - 1) + 1;
        Double leftX = Math.random()*100;
        Double rightX = Math.random()*(200 - 100) + 100;
        Double step = Math.random();
        Function log= new Log(base);
        Task task_1 = new Task(log, leftX, rightX, step, 100);
        for (int i=0; i<task_1.getTasks(); i++){
            base  =  Math.random()*(10 - 1) + 1;
            leftX = Math.random()*100;
            rightX = Math.random()*(200 - 100) + 100;
            step = Math.random();
            log= new Log(base);
            System.out.printf("Source %s, %s, %s; \n", leftX, rightX,step);
            System.out.printf("Result %s, %s, %s, %s; \n", leftX, rightX, step, Functions.integral(log, leftX,rightX,step));
        }
    }
    public static void simpleThreads() throws InterruptedException {
        System.out.println("SimpleThreads calculation:");
        Task task_2 = new Task(new Log(Math.random()*(10-1)+1), Math.random()*100,
                Math.random()*(200 - 100) + 100, Math.random(), 100);
        SimpleGenerator simpleGenerator = new SimpleGenerator(task_2);
        SimpleIntegrator simpleIntegrator = new SimpleIntegrator(task_2);
        Thread thread_1 = new Thread(simpleGenerator);
        Thread thread_2 = new Thread(simpleIntegrator);
        thread_1.start();
        thread_2.start();
    }
    public static void complicatedThreads() throws InterruptedException {
        System.out.println("ComplicatedThreads calculation:");
        Task task_3 = new Task(new Log(Math.random()*(10-1)+1), Math.random()*100, Math.random()*(200 - 100) + 100, Math.random(), 100);
        Semaphore semaphore= new Semaphore(1);
        Generator generator = new Generator(task_3,semaphore);
        Integrator integrator = new Integrator(task_3,semaphore);
        Thread thread_1 = new Thread(generator);
        Thread thread_2 = new Thread(integrator);
        thread_1.start();
        thread_2.start();
        //thread_1.interrupt();
        //thread_2.interrupt();
    }

};

