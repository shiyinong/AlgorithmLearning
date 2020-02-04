package com.syn.learning.javase.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/1/2 17:43
 **/
public class NioTcp {

}

class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(19999));
        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("等待连接。。。");
        while (true) {
            if (selector.select(2000) > 0) {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    try {
                        if (key.isAcceptable()) {
                            System.out.println("成功连接上一个客户端");
                            SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer bb = ByteBuffer.allocate(1024);
                            client.read(bb);
                            bb.flip();
                            System.out.print("接受到了新消息：");
                            byte[] bs=new byte[bb.limit()];
                            bb.get(bs,0,bb.limit());
                            System.out.println(new String(bs)+"--------------------------------");
                            key.cancel();
                        }
                    } finally {
                        it.remove();
                    }
                }
            }else{
                System.out.println("没人连接。。。");
            }
        }
    }
}

class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 19999));
        socketChannel.configureBlocking(false);
        String s = new Scanner(System.in).nextLine();
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.put(s.getBytes());
        bb.flip();
        socketChannel.write(bb);
        socketChannel.shutdownOutput();
        socketChannel.close();
    }
}