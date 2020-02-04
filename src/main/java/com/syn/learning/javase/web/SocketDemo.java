package com.syn.learning.javase.web;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author shiyinong
 * @version 1.0
 * @Date 2019/5/21 10:48
 **/
public class SocketDemo {


}

class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(19999);
        while(true) {
            System.out.println("等待客户端通信。。。");
            Socket socket = serverSocket.accept();
            FileInputStream is = (FileInputStream) socket.getInputStream();
            int d;
            while((d=is.read())!=-1){
                System.out.print((char)d);
            }
            System.out.println("\n-------------");
        }
    }
}

class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 19999);
        FileOutputStream os = (FileOutputStream) socket.getOutputStream();
        for(int i=0;i<10;i++){
            os.write(i+'0');
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
