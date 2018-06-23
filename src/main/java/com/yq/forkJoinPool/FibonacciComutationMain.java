
package com.yq.forkJoinPool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ForkJoinPool;

/**
 * Simple to Introduction
 * className: FibonacciComutationMain
 *
 * @author EricYang
 * @version 2018/6/23 16:12
 */

public class FibonacciComutationMain {
    public static final Log log = LogFactory.getLog(FibonacciComutationMain.class);

    public static void main(String args[]){
        //斐波那契数列 计算第20个斐波那契数列
        // 以递归的方法定义：F(1)=1，F(2)=1, F(n)=F(n-1)+F(n-2)（n>=2，n∈N*）
        int number = 20;

        int poolSize = Runtime.getRuntime().availableProcessors();
        //ForkJoinPool基本上默认就是Runtime.getRuntime().availableProcessors()个县城
        ForkJoinPool pool = new ForkJoinPool(poolSize);

        long beforeTime = System.currentTimeMillis();
        log.info("Parallelism  => "+ pool.getParallelism());
        Integer result = (Integer) pool.invoke(new FibonacciComputation(number));

        log.info("Total Time in MilliSecond Taken ->  "+ (System.currentTimeMillis() - beforeTime));

        log.info(number +"the element of Fibonacci Number = "+result);

    }
}
