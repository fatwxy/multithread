com.yq.waitAndNotifyAll
Q1，为什么必须使用while(check condition)而不是if就行
synchronized (obj) {
     while (check pass) {
        wait();
    }
    // do your business
}

执行TestBoundedQueueV1.java的结果
Exception in thread "pool-1-thread-11" Exception in thread "pool-1-thread-6" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0Exception in thread "pool-1-thread-3" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
java.lang.IndexOutOfBoundsException: Index: 0, Size: 0Exception in thread "pool-1-thread-4" 
    at java.util.ArrayList.rangeCheck(ArrayList.java:647)

    at java.util.ArrayList.rangeCheck(ArrayList.java:647)Exception in thread "pool-1-thread-8" 
java.lang.IndexOutOfBoundsException: Index: 0, Size: 0  at java.util.ArrayList.remove(ArrayList.java:486)   at java.util.ArrayList.remove(ArrayList.java:486)

    at com.yq.waitAndNotifyAll.MyBoundedQueueV1.get(MyBoundedQueueV1.java:21)   at com.yq.waitAndNotifyAll.MyBoundedQueueV1.get(MyBoundedQueueV1.java:21)java.lang.IndexOutOfBoundsException: Index: 0, Size: 0Exception in thread "pool-1-thread-7" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0


Exception in thread "pool-1-thread-5"   at com.yq.waitAndNotifyAll.TestBoundedQueueV1$2.run(TestBoundedQueueV1.java:37)

java.lang.IndexOutOfBoundsException: Index: 0, Size: 0  at java.util.ArrayList.rangeCheck(ArrayList.java:647)   at java.util.ArrayList.rangeCheck(ArrayList.java:647)

    at java.util.ArrayList.rangeCheck(ArrayList.java:647)Exception in thread "pool-1-thread-10"     at com.yq.waitAndNotifyAll.TestBoundedQueueV1$2.run(TestBoundedQueueV1.java:37) at java.util.ArrayList.remove(ArrayList.java:486)   at java.util.ArrayList.rangeCheck(ArrayList.java:647)
Exception in thread "pool-1-thread-2"   at java.util.ArrayList.rangeCheck(ArrayList.java:647)java.lang.IndexOutOfBoundsException: Index: 0, Size: 0

    at java.util.ArrayList.remove(ArrayList.java:486)

    at com.yq.waitAndNotifyAll.MyBoundedQueueV1.get(MyBoundedQueueV1.java:21)   at java.util.ArrayList.remove(ArrayList.java:486)   at java.util.ArrayList.rangeCheck(ArrayList.java:647)


    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1157)  at com.yq.waitAndNotifyAll.TestBoundedQueueV1$2.run(TestBoundedQueueV1.java:37)

    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1157)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1157)
java.lang.IndexOutOfBoundsException: Index: 0, Size: 0  at com.yq.waitAndNotifyAll.MyBoundedQueueV1.get(MyBoundedQueueV1.java:21)   at java.util.ArrayList.remove(ArrayList.java:486)


分析：
错误发生在        com.yq.waitAndNotifyAll.MyBoundedQueueV1.get(MyBoundedQueueV1.java:21)， 也就是 int v = list.remove(0);

假设有A, B两个线程执行get操作, 它们按照如的步骤执行:
1. A 拿到了锁， 也就是MyBoundedQueueV1.java:16
2. 此时size=0, 进入等待,并释放锁 
3. 接着B拿到了锁, MyBoundedQueueV1.java:16, 此时依然是size=0, 然后B进入等待,并释放锁
4. 这个时候有个线程C往里面加了个数据1, 那么notifyAll所有的等待的线程都被唤醒了.
5. AB 重新获取锁, 假设 A先拿到了. 然后它就走到MyBoundedQueueV1.java:21, 移除了一个数据, 没有问题.
6. A 移除数据后 通知别人list的大小有了变化, 于是调用了notifyAll, 这个时候就把B给唤醒了, 那么B接着往下走.
7. 就在这时B出问题了, 因为  此时的check条件已经不满足了 (size=0). B却以为还可以删除就尝试去删除, 结果就抛出异常了.

那么fix很简单, 在get的时候加上while就好了:

synchronized int get() throws InterruptedException {
      while (list.size() == 0) {
          wait();
      }
      int v = list.remove(0);
      notifyAll();
      return v;
  }

同样的, 我们可以尝试修改put的线程数 和 get的线程数来 发现如果put里面不是while的话 也是不行的:



Q2，什么时候用notifyAll或者notify
想要通知所有人的时候就用notifyAll, 只想通知一个人的时候就用notify.
但是我们知道使用notify是没法决定到底通知谁的(都是从等待集合里面选一个). 那这个还有什么存在的意义呢?
下面的几点是jvm告诉我们的:

 任何时候,被唤醒的线程是不可预知的. 比如有5个线程在等待同一个对象, 实际上我们无法知道哪个线程会被执行.
synchronized语义实现了有且只有一个线程可以执行同步块里面的代码.

如下的假设场景，就会导致死锁:

P – 生产者 调用put
C – 消费者 调用get

1. P1 放了一个数字1
2. P2 想来放,发现满了,在wait里面等了
3. P3 想来放,发现满了,在wait里面等了
4. C1想来拿, C2, C3 就在get里面等着
5. C1开始执行, 获取1, 然后调用notify 然后退出

    如果C1把C2唤醒了, 所以P2 (其他的都得等.)只能在put方法上等着. (等待获取synchoronized (this) 这个monitor)
    C2 检查while循环 发现此时队列是空的, 所以就在wait里面等着
    C3 也比P2先执行, 那么发现也是空的, 只能等着了.

6. 这时候我们发现P2 , C2, C3 都在等着锁. 最终P2 拿到了锁, 放一个1, notify,然后退出.
7. P2 这个时候唤醒了P3, P3发现队列是满的,没办法,只能等它变为空.
8. 这时候, 没有别的调用了, 那么现在这三个线程(P3, C2,C3)就全部变成suspend了.也就是死锁了.
