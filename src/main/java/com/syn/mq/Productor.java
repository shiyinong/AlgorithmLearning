package com.syn.mq;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Productor
 * @Description TODO
 * @Date 2019/5/10 15:35
 **/
public class Productor extends Base implements Runnable{
    @Override
    public void run() {
        while (true){
            synchronized (Base.class){
                while (que.size()==capacity){
                    Base.class.notifyAll();
                    System.out.println("队列已满，生产者开始休息！！！");
                    try {
                        Base.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("生产者开始生产...  ");
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                int val=(int)(Math.random()*1000);
                System.out.println("本次生产内容 -> " +val);

                que.add(val);
                Base.class.notifyAll();
                if(--numOfRemain==0) {
                    System.out.println("--------生产完毕---------");
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
