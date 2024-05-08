package org.example;

class IssueWithSyncMethods{
    private int counterOne;
    private int counterTwo;

    public IssueWithSyncMethods(){
        counterOne = 0;
        counterTwo = 0;
    }

    /***
     * Since Synchronized work on same monitor(property of object and classes in case of when referenced from static method)
     * if a thread enters Synchronized method at increaseCounterOne, the thread at increase counter will wait
     * even though they are different unrelated methods. This is because synchronization works on monitor rather than
     * section of a code.
     */
    public synchronized void increaseCounterOne(){
        counterOne++;
    }

    public synchronized void increaseCounterTwo(){
        counterTwo++;
    }
}

public class synchronizationDemo {
    static int counter = 0;
    private synchronizationDemo(){}

    public static void whyWeNeedSyc() throws InterruptedException {
        //runnable to use to increase counter
        Runnable r = () -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            counter++;
        };

        //using 100 threads
        for (int i = 0; i < 100; i++){
            new Thread(r).start();
        }

        Thread.sleep(5000); //let the main thread sleep till others finishes
        // inconsistent counter final value
        System.out.println("without sync " + counter);

        //with sync
        Object o = new Object(); //created a random object to synchronize on
        counter = 0; //resetting counter value
        Runnable syncRunnable = () ->{
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //can use This keyword however since everything is static here.
            //For static methods, class name can also be used instead of object
            //example: synchronized(synchronizationDemo.class)
            //sync can be performed on any object, as they are the monitor
            //we can use synchronized keyword on method declaration to make it synchronized
            synchronized(o){
                counter++;
            }
        };
        for (int i = 0; i < 100; i++){
            new Thread(syncRunnable).start();
        }
        Thread.sleep(5000); //let the main thread sleep till others finishes
        System.out.println("with sync " + counter);
    }

}
