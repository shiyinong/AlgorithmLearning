package com.syn.learning.javase.multithread;

import java.util.concurrent.*;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName ThreadPoolDemo
 * @Description TODO
 * @Date 2019/5/16 13:42
 **/
public class ThreadPoolDemo {

    public static void main(String[] args){
        BlockingQueue bq=new LinkedBlockingQueue(2);
        ExecutorService es= new ThreadPoolExecutor(4,8,10, TimeUnit.SECONDS,bq);
        for(int i=0;i<19;i++){
            es.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized (ThreadPoolDemo.class) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        test();
                    }
                }
            });
        }
        es.shutdown();
    }

    static void test(){
        System.out.println(Thread.currentThread().getName());
    }

}
