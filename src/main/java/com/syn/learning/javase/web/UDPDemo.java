package com.syn.learning.javase.web;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/1/15 16:52
 **/
public class UDPDemo {

}

class UDPClient{
    public static void main(String[] args) throws IOException{
        DatagramSocket client=new DatagramSocket(9998);
        byte[] data="你好啊".getBytes();
        InetAddress address = InetAddress.getByName("127.0.0.1");
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, 9999);
        client.send(datagramPacket);
        client.close();
    }
}

class UDPServer{
    public static void main(String[] args) throws IOException {
        DatagramSocket server=new DatagramSocket(9999);
        byte[] container=new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(container, container.length);
        server.receive(datagramPacket);
        byte[] data = datagramPacket.getData();
        System.out.println(new String(data, StandardCharsets.UTF_8));
        server.close();
    }
}
