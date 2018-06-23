package com.yq.atomicInteger;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ericyang
 * @version 1.0
 */
public class NonAtomicCounterTest {

    public static void main(String[] args) throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final NonAtomicCounter counter = new NonAtomicCounter();

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

        //该代码由于多线程执行i++,无法保证最终的结果一定是1000*1000（也就是1000000）， 每次运行可能得到不同的结果
        //在我的机上16G内存，4核8线程cpu上运行多次，每次结果都不同Count: 995337， durationTime：90， Count: 999719， durationTime：90
        final long endTime = System.currentTimeMillis();
        final long durationTime = endTime - startTime;
        System.out.println("Count: " + counter.getCount() + "， durationTime：" + durationTime);
    }

}
