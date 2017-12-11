package com.yq.atomicInteger;

/**
 * @author ericyang
 * @version 1.0
 */
public class CounterRunnable implements Runnable {

    private Counter counter;
    CounterRunnable(Counter counter){
        this.counter = counter;
     }

    @Override
    public void run() {
       for (int x = 0; x < 1000; x++) {
          counter.incrementCount();
       }
    }
}
