package com.yq.waitAndNotifyAll;

import java.util.ArrayList;

/**
 * 该类用来说明为什么需要notifyAll, 而不是notify
 */
public class MyBoundedQueueV3Notify {
    private final int MAX = 5;
    private final ArrayList<Integer> list = new ArrayList<Integer>();
    synchronized void put(int v) throws InterruptedException {
        while(list.size() == MAX) {
            wait();
        }
        list.add(v);
        notify();
    }

    synchronized int get() throws InterruptedException {
        while(list.size() == 0) {
            wait();
        }

        int v = list.remove(0);
        notify();
        return v;
    }
}
