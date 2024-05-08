package org.example;

/***
 *I am going to use this class for basic thread operations like
 * >Creating threads
 * >Daemon Thread
 * >Joins
 * >Priority to threads
 * >Assigning name and printing name of thread
 * >Thread Groups
 * >Thread Pools
 * >Thread Join
 * >Runnable
 * >Callable and Future Objects
 * >Executor Service
 */
//======================================================================================================================

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

/***
 * One way to create a thread is to extend thread class, not recommended since there are better options
 */
class MyThreadExampleOne extends Thread{

    /***
     *>> Thread name can either be set while initializing an object of this class or in the thread class
     * itself using the 'this' keyword
     * >> Other thread methods are also be used with 'this' keyword.
     * @param name name of the thread passed
     */
    public MyThreadExampleOne(String name){
        this.setName(name);
    }
    public void run(){
        System.out.println(Thread.currentThread().getName());
    }
}

class MyThreadExampleTwo implements Runnable{

    /***
     * >> Creating thread by using runnable interface
     * >> setName() method is not available for runnable interface.
     */
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

class MyCallableExample implements Callable<String>{

    @Override
    public String call() throws Exception {
        return "Callable example";
    }
}
public class ThreadBasics {

    /***
     * making the constructor private, I am going to use this class as a driver
     * no instance of this class is required
     */
    private ThreadBasics(){}

    /***
     * creates thread by extending thread class
     */
    public static void ThreadUsingThreadClass(){
        MyThreadExampleOne t = new MyThreadExampleOne("my thread");
        t.start();
        t = new MyThreadExampleOne(""); //entering a blank value
        t.setName("Setting name before calling, overriding constructor passed value");
        t.start();
    }

    /***
     * create thread implementing runnable class
     */
    public static void ThreadUsingRunnableInterface(){
        //overloaded constructor can assign name to thread
        Thread t = new Thread(new MyThreadExampleTwo(),"this was created using runnable");
        t.start();
    }

    /***
     * since runnable class is a functional interface,
     * thread can be created by providing a lambda expression
     * to thread class constructor.
     */
    public static void ThreadUsingLambdaExpression(){
        Thread t = new Thread(()-> System.out.println(Thread.currentThread().getName()),
                "thread created using lambda expression");
        t.start();
    }

    /***
     * 1) create a thread
     * 2) we'll have main thread and new created thread
     * 3) using join method, we'll have the main thread waiting till new thread finishes.
     */
    public static void ThreadJoinDemo() throws InterruptedException {

        Thread t = new Thread(() -> {
            try {
                System.out.println("Child thread sleeping for 10s");
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t.start();
        t.join();
        System.out.println("Main thread continued after join");
    }

    /***
     * thread priority is only a request/suggestion to compiler and does not guarantee that a higher
     * priority thread will be performed first
     */
    public static void ThreadPriorityDemo(){
        Thread t1 = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " has a priority of " + Thread.currentThread().getPriority());
        }, "Thread one");
        Thread t2 = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " has a priority of " + Thread.currentThread().getPriority());
        }, "Thread two");
        Thread t3 = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " has a priority of " + Thread.currentThread().getPriority());
        }, "Thread three");
        Thread t4 = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " has a priority of " + Thread.currentThread().getPriority());
        }, "Thread four");

        //thread priority is an integer with a value ranging from 1 to 10
        //one being lowest and 10 being highest.
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        //we can also pass an integer values rather than using
        //Thread class static int variables
        t3.setPriority(Thread.NORM_PRIORITY);
        t4.setPriority(7);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    /***
     * used for low priority task
     * doesn't stop the JVM from exiting
     * The program may terminate before the thread finishes
     */
    public static void DaemonThreadDemo(){
        Thread t = new Thread(() -> {
            try {
                sleep(10000);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"Daemon Thread");
        t.setDaemon(true);
        t.start();
    }


    /***
     *
     */
    public static void ExecutorServiceDemo(){
        ExecutorService exs = Executors.newSingleThreadExecutor();
        exs.execute(()->{
            System.out.println("this runnable was kicked off from executor service");
            try {
                sleep(3000); //will sleep for 3 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                System.out.println("Waited for 3 seconds and now in finally block");
            }
        });

        exs.shutdown(); //it is kinda important to do don't know why, google please
    }

    /***
     *
     */
    public static void ExecutorServiceDemoTwo(){
        ArrayList<Runnable> task = new ArrayList<>();
        ExecutorService exs = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++){
            final int j = i;
            task.add(new Thread( ()->{
                System.out.println("Executor Service list, task no: " + j + " Executed by " + Thread.currentThread().getName());
            }));
        }
        for (Runnable t : task){
            exs.execute(t);
        }
        exs.shutdown();
    }

    /***
     *
     */
    public static void callableDemo(){
        MyCallableExample callable = new MyCallableExample();
        ExecutorService exs = Executors.newSingleThreadExecutor();
        Future <String> future = exs.submit(callable);

        while (!future.isDone()) System.out.println("Waiting");
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            exs.close();
        }
    }

    /***
     * callable is a functional interface
     */
    public static void callableUsingLambda(){
        Callable<String> c = () -> "Hello world";
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> f = es.submit(c);
        while (!f.isDone()){
            System.out.println(f.state());
        }
        try {
            System.out.println(f.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            es.close();
        }
    }

    public static void invokeAnyAndInvokeAllExamples(){
        List<Callable<String>> listOne = new ArrayList<>();
        List<Callable<String>> listTwo = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            final int taskNo = i;
            listOne.add(
                    () -> "Task Number " + taskNo + " Executed by " + Thread.currentThread().getName()
            );

            listTwo.add(
                    () -> "Task Number " + taskNo + " Executed by " + Thread.currentThread().getName()
            );
        }

        //invokeAll only throws InterruptedException
        //the future get throws ExecutionException
        //since invokeAny seems to be handling future part itself. it throws both.
        ExecutorService exs = Executors.newFixedThreadPool(4);
        try {
            List<Future<String>> flist = exs.invokeAll(listOne);
            for (Future f : flist){
                while (!f.isDone())
                    System.out.println(f.state());
                System.out.println(f.get());
            }

            String s = exs.invokeAny(listTwo);
            System.out.println(s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }finally {
            exs.close();
        }




    }
}
