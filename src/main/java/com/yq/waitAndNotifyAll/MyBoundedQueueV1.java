package com.yq.waitAndNotifyAll;

import java.util.ArrayList;

/**
 * 该类用来说明为什么需要while(check condition) { wait()}
 */
public class MyBoundedQueueV1 {
    private final int MAX = 5;
    private final ArrayList<Integer> list = new ArrayList<Integer>();
    synchronized void put(int v) throws InterruptedException {
        if (list.size() == MAX) {
            wait();
        }
        list.add(v);
        notifyAll();
    }

    synchronized int get() throws InterruptedException {
        if (list.size() == 0) {
            wait();
        }

        int v = list.remove(0);
        notifyAll();
        return v;
    }

    synchronized int size() {
        return list.size();
    }
}
