package com.yq.atomicInteger;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ericyang
 * @version 1.0
 */
public class AtomicCounterTest {

    public static void main(String[] args) throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final AtomicCounter counter = new AtomicCounter();

        // create 1000 threads
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int x = 0; x < 1000; x++) {
           threads.add(new Thread(new CounterRunnable(counter)));
        }

        // start all of the threads
        Iterator<Thread> itr = threads.iterator();
        while (itr.hasNext()) {
            Thread mt = (Thread) itr.next();
           mt.start();
        }

        // wait for all the threads to finish
        Iterator<Thread> itr2 = threads.iterator();
        while (itr2.hasNext()) {
           Thread mt = (Thread) itr2.next();
           mt.join();
        }

        //该代码由于多线程执行i++,AtomicInteger可以保证院子操作， 每次运行的结果都是相同的
        //在我的机上16G内存，4核8线程cpu上运行多次，每次结果计数结果都是相同的1000000， 时间可能不同
        final long endTime = System.currentTimeMillis();
        final long durationTime = endTime - startTime;
        System.out.println("Count: " + counter.getCount() + "， durationTime：" + durationTime);
    }

}
