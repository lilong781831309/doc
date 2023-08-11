package org.xinua.example.io.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;

public class NioClient {

    private String nickname;

    public NioClient(String nickname) {
        this.nickname = nickname;
    }

    public void start() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        SelectionKey sk = socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));

        new Thread(new ClientHandle(selector)).start();

        Scanner scanner = new Scanner(System.in);
        Charset charset = Charset.forName("UTF-8");

        while (scanner.hasNext()) {
            String msg = scanner.nextLine();
            ByteBuffer buffer = charset.encode(nickname + ":" + msg);
            int len = socketChannel.write(buffer);
            while (buffer.hasRemaining() && len > 0) {
                socketChannel.write(buffer);
            }
            if (buffer.hasRemaining() && len == 0) {
                sk.attach(buffer);
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            }
        }
    }

    static class ClientHandle implements Runnable {
        private ByteBuffer buffer = ByteBuffer.allocate(1024);
        private Selector selector;

        public ClientHandle(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int n = selector.select();
                    if (n == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey sk = it.next();
                        it.remove();

                        if (sk.isConnectable()) {
                            connectHandler(selector, sk);
                        } else if (sk.isReadable()) {
                            readHandler(sk);
                        } else if (sk.isWritable()) {
                            writeHandler(sk);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void connectHandler(Selector selector, SelectionKey sk) throws IOException {
            SocketChannel socketChannel = (SocketChannel) sk.channel();
            if (!socketChannel.isConnected()) {
                socketChannel.finishConnect();
            }
            sk.interestOps(sk.interestOps() & ~SelectionKey.OP_CONNECT);
            socketChannel.register(selector, SelectionKey.OP_READ);
        }

        private void readHandler(SelectionKey sk) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SocketChannel socketChannel = (SocketChannel) sk.channel();
            int n;
            buffer.clear();
            while ((n = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                baos.write(buffer.array(), 0, n);
                buffer.clear();
            }
            if (baos.size() > 0) {
                System.out.println(baos.toString());
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
            sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }
}
