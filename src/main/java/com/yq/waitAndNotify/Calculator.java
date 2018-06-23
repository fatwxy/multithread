package com.yq.waitAndNotify;

public class Calculator {
    private int total = 0;

    public void calculate() {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                total += i;
            }
            // notify all the objects, which waits for this object
            notifyAll();
        }

        //如果lock对象就调用notifyAll， 会有IllegalMonitorStateException
        //notifyAll();
    }

    public int getTotal() {
        return this.total;
    }
}
