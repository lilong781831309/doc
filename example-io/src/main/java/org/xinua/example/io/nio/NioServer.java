package org.xinua.example.io.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {

    private int port;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        new NioServer(8888).start();
    }

    public NioServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", port));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            //自上次调用select()方法后有多少通道变成就绪状态,因为有一个通道变成就绪状态，返回了1，若再次调用select()方法，如果另一个通道就绪了，它会再次返回1。
            //如果对第一个就绪的channel没有做任何操作，现在就有两个就绪的通道，但在每次select()方法调用之间，只有一个通道就绪了。
            //其他线程调用 selector.wakeup() 方法会立即唤醒,可能返回0
            int n = selector.select();
            if (n == 0) {
                continue;
            }

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                //Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除。下次该通道变成就绪时，Selector会再次将其放入已选择键集中
                it.remove();

                if (sk.isAcceptable()) {
                    accepteHandler(selector, sk);
                } else if (sk.isReadable()) {
                    readHandler(selector, sk);
                } else if (sk.isWritable()) {
                    writeHandler(sk);
                }
            }
        }
    }

    private void accepteHandler(Selector selector, SelectionKey sk) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) sk.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void readHandler(Selector selector, SelectionKey sk) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SocketChannel socketChannel = (SocketChannel) sk.channel();
        int n;
        buffer.clear();
        //read返回0有3种情况
        //1、某一时刻socketChannel中当前没有数据可以读
        //2、bytebuffer的remaining等于0(执行clear,不存在这种情况)
        //3、客户端的数据发送完毕了
        while ((n = socketChannel.read(buffer)) > 0) {
            buffer.flip();
            baos.write(buffer.array(), 0, n);
            buffer.clear();
        }
        if (baos.size() > 0) {
            System.out.println(new String(baos.toByteArray()));
            broadcast(selector, sk, baos.toByteArray());
        }
    }

    private void broadcast(Selector selector, SelectionKey sk, byte[] bytes) throws IOException {
        for (SelectionKey key : selector.keys()) {
            if (key.channel() instanceof SocketChannel && key.channel() != sk.channel()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                int len = socketChannel.write(buffer);
                //write()方法无法保证能写多少字节到SocketChannel。所以重复调用write()直到Buffer没有要写的字节为止
                while (buffer.hasRemaining() && len > 0) {
                    socketChannel.write(buffer);
                }
                //如果缓存区满了,则注册写入事件
                if (buffer.hasRemaining() && len == 0) {
                    sk.attach(buffer);
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                }
            }
        }
    }

    private void writeHandler(SelectionKey sk) throws IOException {
        SocketChannel socketChannel = (SocketChannel) sk.channel();
        ByteBuffer buffer = (ByteBuffer) sk.attachment();
        if (buffer != null) {
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
        }
        //取消注册写入事件
        sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
    }
}
