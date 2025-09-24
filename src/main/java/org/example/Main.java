package org.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/***
 * Threads cannot be used again because they have lifecycle
 */
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //WaitAndNotifyDemo.runDemo();
        WaitAndNotifyDemo.runDemo();
        WaitAndNotifyDemo.runDemo();
        WaitAndNotifyDemo.runDemo();
        WaitAndNotifyDemo.runDemo();
    }

}
