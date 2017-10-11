package com.yq.waitAndNotifyAll;

import java.util.ArrayList;

public class MyBoundedQueueV2 {
    private final int MAX = 5;
    private final ArrayList<Integer> list = new ArrayList<>();
    synchronized void put(int v) throws InterruptedException {
        while(list.size() == MAX) {
            wait();
        }
        list.add(v);
        notifyAll();
    }

    synchronized int get() throws InterruptedException {
        while(list.size() == 0) {
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
