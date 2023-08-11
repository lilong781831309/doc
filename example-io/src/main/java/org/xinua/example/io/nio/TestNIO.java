package org.xinua.example.io.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestNIO {

    @Test
    public void test1() throws Exception {
        FileInputStream fis = new FileInputStream("D:\\in.txt");
        FileOutputStream fos = new FileOutputStream("D:\\out.txt");
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024);//非直接缓冲区
        //ByteBuffer buf = ByteBuffer.allocateDirect(1024);//直接缓冲区

        while (inChannel.read(buf) > 0) {
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }

        fis.close();
        fos.close();
        inChannel.close();
        outChannel.close();
    }

    @Test
    public void test2() throws Exception {
        RandomAccessFile ras = new RandomAccessFile("D:\\in.txt", "rw");
        RandomAccessFile ras2 = new RandomAccessFile("D:\\out.txt", "rw");

        FileChannel inChannel = ras.getChannel();
        FileChannel outChannel = ras2.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(1024);//非直接缓冲区
        //ByteBuffer buf = ByteBuffer.allocateDirect(1024);//直接缓冲区

        while (inChannel.read(buf) > 0) {
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }

        ras.close();
        ras2.close();

        inChannel.close();
        outChannel.close();
    }

    @Test
    public void test3() throws Exception {
        FileChannel inChannel = FileChannel.open(Paths.get("D:\\in.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:\\out.txt"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        inChannel.transferTo(0, inChannel.size(), outChannel);
        //outChannel.transferFrom(inChannel, 0, inChannel.size());

        inChannel.close();
        outChannel.close();
    }

    @Test
    public void test4() throws Exception {
        FileChannel inChannel = FileChannel.open(Paths.get("D:\\in.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:\\out.txt"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        long size = inChannel.size();

        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
        MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);

        byte[] buf = new byte[1024];
        int length;

        while (inMappedBuf.hasRemaining()) {
            length = inMappedBuf.remaining() > buf.length ? buf.length : inMappedBuf.remaining();
            inMappedBuf.get(buf, 0, length);
            outMappedBuf.put(buf, 0, length);
        }

        inChannel.close();
        outChannel.close();
    }

    @Test
    public void test5() throws Exception {
        FileChannel inChannel = FileChannel.open(Paths.get("D:\\in.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:\\out.txt"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);
        ByteBuffer[] bufs = {buf1, buf2};

        inChannel.read(bufs);

        for (ByteBuffer buffer : bufs) {
            buffer.flip();
        }

        outChannel.write(bufs);

        inChannel.close();
        outChannel.close();
    }

    @Test
    public void test6() throws Exception {
        RandomAccessFile ras = new RandomAccessFile("D:\\in.txt", "rw");
        RandomAccessFile ras2 = new RandomAccessFile("D:\\out.txt", "rw");
        FileChannel inChannel = ras.getChannel();
        FileChannel outChannel = ras2.getChannel();

        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);
        ByteBuffer[] bufs = {buf1, buf2};

        inChannel.read(bufs);

        for (ByteBuffer buffer : bufs) {
            buffer.flip();
        }

        outChannel.write(bufs);

        ras.close();
        ras2.close();
        inChannel.close();
        outChannel.close();
    }

    @Test
    public void test7() throws Exception {
        Charset gbk = Charset.forName("GBK");
        Charset utf8 = Charset.forName("UTF-8");

        CharBuffer charBuffer = CharBuffer.allocate(100);
        charBuffer.put("大家好");
        charBuffer.flip();

        //gbk编码
        ByteBuffer byteBuffer = gbk.newEncoder().encode(charBuffer);

        //gbk解码
        CharBuffer buff1 = gbk.newDecoder().decode(byteBuffer);
        System.out.println(buff1.toString());//大家好

        //重读
        byteBuffer.rewind();

        //utf8解码
        CharBuffer buffer = utf8.decode(byteBuffer);
        System.out.println(buffer.toString());//��Һ�
    }

    @Test
    public void client() throws Exception {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\a.txt"), StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (fileChannel.read(buffer) > 0) {
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }

        socketChannel.shutdownOutput();

        int len;
        while ((len = socketChannel.read(buffer)) > 0) {
            System.out.print(new String(buffer.array(), 0, len));
        }

        fileChannel.close();
        socketChannel.close();
    }

    @Test
    public void server() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8888));

        SocketChannel socketChannel = serverSocketChannel.accept();
        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\b.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }

        buffer.put("服务的接收成功".getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        socketChannel.shutdownOutput();

        fileChannel.close();
        socketChannel.close();
        serverSocketChannel.close();
    }

}
