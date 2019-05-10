package com.syn.mq;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName Consumer
 * @Description TODO
 * @Date 2019/5/10 15:45
 **/
public class Consumer extends Base implements Runnable {
    @Override
    public void run() {
        while (true){
            synchronized (Base.class){
                while (numOfRemain>0&&que.isEmpty()){
                    System.out.println("队列为空，消费者开始休息");
                    Base.class.notifyAll();
                    try {
                        Base.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(numOfRemain==0&&que.isEmpty()) {
                    System.out.print("----------消费者完毕------  ");
                    break;
                }
                System.out.print("消费者开始消费...  ");
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
