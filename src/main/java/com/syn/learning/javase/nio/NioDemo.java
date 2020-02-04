package com.syn.learning.javase.nio;

import org.junit.Test;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2020/1/2 10:29
 **/
public class NioDemo {

    /**
     * 使用传统的bio方式复制一个文件
     */
    @Test
    public void test1() throws IOException {
        FileInputStream fis = new FileInputStream("./data/nio/s.zip");
        FileOutputStream fos = new FileOutputStream("./data/nio/s2.zip");
        int len;
        byte[] bs = new byte[1024*1024];
        while ((len = fis.read(bs)) != -1) {
            fos.write(bs, 0, len);
        }
    }
    /**
     * 使用传统的bio方式复制一个文件
     */
    @Test
    public void test2() throws IOException {
        BufferedInputStream bis=new BufferedInputStream(new FileInputStream("./data/nio/e.pdf"));
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream("./data/nio/e2.pdf"));
        int len;
        byte[] bs = new byte[1024];
        while((len=bis.read(bs))!=-1){
            bos.write(bs,0,len);
        }
    }

    /**
     * 使用bio方式序列化一个对象到硬盘上
     * @throws IOException
     */
    @Test
    public void test3() throws IOException, ClassNotFoundException {
//        ObjectOutputStream ois=new ObjectOutputStream(new FileOutputStream("./data/nio/obj"));
//        Obj obj = new Obj();
//        obj.name="a";
//        obj.id=0;
//        ois.writeObject(obj);
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream("./data/nio/obj"));
        Obj o = ((Obj) ois.readObject());
    }


    @Test
    public void test4() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("./data/nio/s.zip"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("./data/nio/s2.zip"), StandardOpenOption.READ,
                StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        MappedByteBuffer inMap = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMap = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
        byte[] bs=new byte[inMap.limit()];
        inMap.get(bs);
        outMap.put(bs);
    }

}

class Obj implements Serializable{
    public String name;
    public Integer id;

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
        s.writeUTF("123");
        s.writeInt(100);
    }
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        System.out.println("==============");
        this.name=s.readUTF();
        this.id=s.readInt();
    }
}
