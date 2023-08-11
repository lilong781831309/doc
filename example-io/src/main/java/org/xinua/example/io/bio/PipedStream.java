package org.xinua.example.io.bio;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipedStream {

    public static void main(String[] args) throws IOException {
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream();
        pis.connect(pos);
        new Thread(new Reciver(pis)).start();
        new Thread(new Sender(pos)).start();
    }

    static class Sender implements Runnable {
        PipedOutputStream pos;

        public Sender(PipedOutputStream pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(500);
                    pos.write(("Message " + i + "\r\n").getBytes());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (pos != null) {
                    try {
                        pos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    static class Reciver implements Runnable {
        PipedInputStream pis;

        public Reciver(PipedInputStream pis) {
            this.pis = pis;
        }

        @Override
        public void run() {
            try {
                byte[] buff = new byte[64];
                int read;
                while ((read = pis.read(buff)) != -1) {
                    System.out.print(new String(buff, 0, read));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (pis != null) {
                    try {
                        pis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
