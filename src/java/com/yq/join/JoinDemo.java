/*
* Licensed - Apache 2.0.
*/
package com.yq.join;

/**
 * @author ericyang
 */
public class JoinDemo {

    public static void main(String[] args) {
        final Thread threadA = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    System.out.println("ThreadA start");
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("ThreadA end");
                }
            });
        //threadB 将在threadA完成后才完成
        final Thread threadB = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    System.out.println("ThreadB start");
                    try {
                        threadA.join();
                        Thread.sleep(3500);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("ThreadB end");
                }
            });

        //threadC 将在threadB完成后才完成
        Thread threadC = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    System.out.println("ThreadC start");
                    try {
                        threadB.join();
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("ThreadC end");
                }
        });

        //threadD应该在threadA完成后， threadB完成前，结束， 因为threadA。 sleep 2000，
        // threadB sleep 3500,并且 将在threadA完成后才完成. 而threadD只需要sleep 3000
        // 但是需要注意sleep不是精确的sleep时间，线程调度也有随机性
        //The precision is not guaranteed - the Thread may sleep more or less than requested.
        Thread threadD = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    System.out.println("ThreadD start");
                    try {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("ThreadD end");
                }
        });

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
    }

}
