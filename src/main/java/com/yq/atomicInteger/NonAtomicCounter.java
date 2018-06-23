package com.yq.atomicInteger;


/**
 * @author ericyang
 * @version 1.0
 * 该类用来说明不使用AtomicInteger可能会出错
 */
public class NonAtomicCounter implements Counter{
    private int count = 0;

    public void incrementCount() {
       count++;
    }

    public int getCount() {
      return count;
    }

}
