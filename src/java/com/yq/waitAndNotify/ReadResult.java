package com.yq.waitAndNotify;

import java.util.concurrent.TimeUnit;

public class ReadResult implements Runnable{

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        // �?�动5个线程，分别获�?�计算结果  
        for(int i = 0; i < 5; i++){
            Thread thread = new Thread(new ReadResult(calculator));
            thread.start();
        }
        //�?�动计算 , 如果我们在这里�?调用calculate， 那么主线程退出了，什么结果也没有。 因为我们调用calculate时�?有notifyAll
        calculator.calculate();

        System.out.println("end");
    }

    private Calculator cal = null;
    public ReadResult(Calculator cal) {
        this.cal = cal;
    }

    @Override
    public void run() {
        synchronized (cal) {
            try {
                System.out.println(Thread.currentThread() + "等待计算结果。。。");
                cal.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread() + "计算结果为：" + cal.getTotal());
        }
    }
}
