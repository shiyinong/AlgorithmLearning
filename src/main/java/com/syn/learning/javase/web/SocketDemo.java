package com.syn.learning.javase.web;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName SocketDemo
 * @Description TODO
 * @Date 2019/5/21 10:48
 **/
public class SocketDemo {


}

class Server{

    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(19999);
        System.out.println("等待客户端通信。。。");
        ExecutorService es=new ThreadPoolExecutor(2,2,1, TimeUnit.SECONDS,new LinkedBlockingDeque<>(1));
        while(true) {
            final Socket socket = serverSocket.accept();
            es.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String info = br.readLine();
                        System.out.println("我是服务端，客户端说：" + info);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        bw.write(info + "   --  " + info.length());
                        bw.flush();

                        br.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }
}

class Client{

    public static void main(String[] args) throws IOException{
        Socket socket= new Socket("localhost", 19999);
        System.out.println("客户端输入：");
        BufferedReader br2=new BufferedReader(new InputStreamReader(System.in));
        String str=br2.readLine();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(str);
        bw.flush();
        socket.shutdownOutput();

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String info=br.readLine();
        System.out.print("我是客户端，服务端回复："+info);

        br.close();
        socket.close();
        bw.close();
        br2.close();
    }
}
