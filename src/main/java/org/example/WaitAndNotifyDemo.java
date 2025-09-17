package org.example;

public class WaitAndNotifyDemo {
    public static void runDemo(){
        Object o = new Object();
        Object q = new Object();

        new Thread(()->{
            synchronized (o){
                try {
                    System.out.println("waiting");
                    o.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("notified");
            }
        }).start();

        new Thread(()->{
            synchronized (o){
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                o.notify();
            }
        }).start();

    }
}
