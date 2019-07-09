package com.syn.learning.javase.multithread;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Base
 * @Description TODO
 * @Date 2019/5/10 15:34
 **/
public class Base {
    final static int capacity=50;
    static int numOfRemain=5000;
    static Queue<Integer> que=new LinkedList<>();
    static BlockingQueue<Integer> queue=new LinkedBlockingQueue<>(10);

    static ReentrantLock lock=new ReentrantLock();
    static Condition productCondition=lock.newCondition();
    static Condition consumeCondition=lock.newCondition();
    static volatile int num=0;

    public static void main(String[] args) throws InterruptedException {
//        new Thread(new Productor2(),"生产者 - 1 ").start();
//        new Thread(new Productor2(),"生产者 - 2 ").start();
//        new Thread(new Productor2(),"生产者 - 3 ").start();
//        new Thread(new Consumer2(),"消费者 - 1 ").start();
//        new Thread(new Consumer2(),"消费者 - 2 ").start();
        String lock = "1";
        String lock2 = "2";
        long s=System.currentTimeMillis();
        Thread t1= new Thread(new Runnable() {
            @Override
            public void run() {
                    Thread.yield();
                    for (int i = 0; i < 100; i++) {
                        System.out.println(Thread.currentThread().getName() + "   ===========  " + ++num);
                    }
                }
        }, "11111111111");

        Thread t2= new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("22222222222222222");
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long s=System.currentTimeMillis();
                    for (int i = 0; i < 100; i++) {

                        System.out.println(Thread.currentThread().getName() + "   |||||||||||| " + ++num);
                    }
                    System.out.println(Thread.currentThread().getName() +"--time:  "+(System.currentTimeMillis()-s)/1000);

            }
        }, "22222222222");

//        new Thread(new Productor3(),"生产者1").start();
//        new Thread(new Productor3(),"生产者2").start();
//        new Thread(new Consumer3(),"消费者1").start();
//        new Thread(new Consumer3(),"消费者2").start();
        for(int i=0;i<160;i++){
            Integer locki=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (locki){
                        long s=System.currentTimeMillis();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName()+"  "+(System.currentTimeMillis()-s)/1000);
                    }
                }
            },"name -- "+i).start();
        }

        System.out.println(Thread.currentThread().getName() +"--time:  "+(System.currentTimeMillis()-s)/1000);

        Thread.sleep(20);
    }
}

class Productor extends Base implements Runnable{
    @Override
    public void run() {
        while (true){
            synchronized (Base.class){
                //必须用while，因为可能被其他的producter唤醒，如果此处使用if，那么就会在队列已满的情况下继续生产
                while (que.size()==capacity){
                    Base.class.notifyAll();
                    System.out.println("队列已满，"+Thread.currentThread().getName()+"开始休息！！！");
                    try {
                        Base.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(--numOfRemain>=0) {
                    System.out.print(Thread.currentThread().getName() + "开始生产...  ");
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                    int val = (int) (Math.random() * 1000);
                    System.out.println("本次生产内容 -> " + val);
                    que.add(val);
                    Base.class.notifyAll();
                }else {
                    System.out.println(Thread.currentThread().getName()+"--------生产完毕---------");
                    break;
                }
            }
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}

class Consumer extends Base implements Runnable {
    @Override
    public void run() {
        while (true){
            synchronized (Base.class){
                //必须用while，因为可能被其他的consumer唤醒，如果此处使用if，那么就会在队列为空的情况下继续消费
                while (numOfRemain>0&&que.isEmpty()){
                    System.out.println("队列为空，"+Thread.currentThread().getName()+"开始休息");
                    Base.class.notifyAll();
                    try {
                        Base.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(numOfRemain<=0&&que.isEmpty()) {
                    System.out.println(Thread.currentThread().getName()+"----------消费者完毕------  ");
                    break;
                }
                System.out.print(Thread.currentThread().getName() +"开始消费...  ");
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println("本次消费内容 -> "+que.poll());
                Base.class.notifyAll();
            }
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}

class Productor2 extends Base implements Runnable {
    @Override
    public void run() {
        while (numOfRemain >= 0) {
            lock.lock();
            try {
                if (que.size() >= capacity) {
                    System.out.println("队列已满，生产者"+Thread.currentThread().getName()+"休息");
                    productCondition.await();
                }
                if (numOfRemain-- <= 0) {
                    System.out.println("==========生产者"+Thread.currentThread().getName()+"生产完毕========");
                } else {
                    int v = (int) (Math.random() * 100);
                    System.out.println("生产者"+Thread.currentThread().getName()+"生产一个：" + v);
                    que.add(v);
                }
                consumeCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

class Consumer2 extends Base implements Runnable {
    @Override
    public void run() {
        while (numOfRemain > 0||!que.isEmpty()) {
            lock.lock();
            try {
                if (que.isEmpty() && numOfRemain > 0) {
                    System.out.println("队列为空，消费者"+Thread.currentThread().getName()+"休息");
                    consumeCondition.await();
                }
                if (!que.isEmpty()) {
                    System.out.println("消费者"+Thread.currentThread().getName()+"消费一个：" + que.poll());
                }
                productCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        System.out.println("==========消费者"+Thread.currentThread().getName()+"完毕==========");
    }
}

class Productor3 extends Base implements Runnable{
    @Override
    public void run() {
        while(true){
            int a=(int)(Math.random()*100);
            System.out.println(Thread.currentThread().getName() + "开始生产...  "+a);
            try {
                queue.put(a);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

class Consumer3 extends Base implements Runnable {
    @Override
    public void run() {
        while(true){
            try {
                System.out.println(Thread.currentThread().getName() + "开始消费...  "+queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
