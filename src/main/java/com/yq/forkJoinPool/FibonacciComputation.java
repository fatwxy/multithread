package com.yq.forkJoinPool;

import java.util.concurrent.RecursiveTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple to Introduction
 * className: FibonacciComputation
 *
 * @author EricYang
 * @version 2018/6/23 16:11
 */

public class FibonacciComputation extends RecursiveTask<Integer> {
    public static final Log log = LogFactory.getLog(FibonacciComputation.class);
    private final int number;

    public FibonacciComputation(int number) {
        this.number = number;
    }

    @Override
    public Integer compute() {

        //如果当前要计算的小于等于1， 直接返回
        if (number <= 1) {
            return number;
        }

        //如果要计算的斐波那契数列大于1， 我们就分成n-1和n-2， 等n-1和n-2计算完毕后将这两者的结果合并
        FibonacciComputation f1 = new FibonacciComputation(number - 1);
        f1.fork();

        log.info("Current Thread Name" + Thread.currentThread().getName());
        FibonacciComputation f2 = new FibonacciComputation(number - 2);

        return f2.compute() + (Integer)f1.join();
    }

}