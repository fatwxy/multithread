package com.yq.waitAndNotifyAll;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestBoundedQueueV2 {

    public static void main(String[] args) {
        final MyBoundedQueueV2 queue = new MyBoundedQueueV2();
        ExecutorService exeSvc = Executors.newFixedThreadPool(11);
        for (int i = 0; i < 1; i++) {
            exeSvc.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println("put");
                            queue.put(1);
                            Thread.sleep(20);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            });
        }


        for (int i = 0; i < 10; i++) {
            exeSvc.execute(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println("get");
                            queue.get();
                            Thread.sleep(10);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            });
        }

        exeSvc.shutdown();
        try {
            exeSvc.awaitTermination(1, TimeUnit.DAYS);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("normal exit");
    }

}
