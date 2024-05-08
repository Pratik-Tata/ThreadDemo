package org.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LocksDemo{

    /***
     * >>>Unlike synchronized keyword used on method, Locks
     * work on a section of a code.
     * >>>Locks achieve sync by allowing only one thread to acquire a lock
     * while the other thread waits till the thread that has the lock releases it.
     * >>>Unlike synchronized blocks that rely on monitor, locks are something a
     * thread acquire that allows it to execute the code (critical section) and
     * once it is finished, it releases it which allows other thread to acquire lock.
     * >>>Locks supports fairness, can set fairness property so that thread with most
     * wait time execute first, we cannot do that with synchronized blocks.
     * >>>A thread gets blocked if it can’t get an access to the synchronized block.
     * The Lock API provides tryLock() method. The thread acquires lock only if it’s available and
     * not held by any other thread. This reduces blocking time of thread waiting for the lock.
     * >>A thread that is in “waiting” state to acquire the access to synchronized block can’t be interrupted.
     * The Lock API provides a method lockInterruptibly() that can be used to interrupt the thread
     * when it’s waiting for the lock.
     * >>> read and write lock to maintain ACID
     */
    Lock lockForMethodA = new ReentrantLock();
    Lock lockForMethodB = new ReentrantLock();
    Lock lockGeneral = new ReentrantLock();
    int counterA = 0;
    int counterB = 0;

    int conditionalLockCounter = 0;


    public void MethodA(){
        lockForMethodA.tryLock();
            counterA++;
        lockForMethodA.unlock();
    }
    public void MethodB(){
        lockForMethodB.tryLock();
            counterB++;
        lockForMethodB.unlock();
    }

    public void lockCanWorkAcrossMethods(){
        lockAcrossMethodsLock();
        //my code here
        lockAcrossMethodsUnlock();
    }

    private void lockAcrossMethodsLock(){
        lockGeneral.tryLock();
    }

    private void lockAcrossMethodsUnlock(){
        lockGeneral.unlock();
    }

    private void workingWithConditions() throws InterruptedException {
        Condition lockCond = lockGeneral.newCondition();

        new Thread(()->{
            for (int i = 0; i < 100; i++){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        lockGeneral.lock();
        while (conditionalLockCounter < 100){
            lockCond.await();
        }
        System.out.println(conditionalLockCounter);
        lockGeneral.unlock();
    }


}

public class Locks {
    private Locks(){};

}
