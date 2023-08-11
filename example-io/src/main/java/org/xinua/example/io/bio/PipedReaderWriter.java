package org.xinua.example.io.bio;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class PipedReaderWriter {

    public static void main(String[] args) throws IOException {
        PipedReader pr = new PipedReader();
        PipedWriter pw = new PipedWriter();
        pr.connect(pw);
        new Thread(new Reciver(pr)).start();
        new Thread(new Sender(pw)).start();
    }

    static class Sender implements Runnable {
        PipedWriter pw;

        public Sender(PipedWriter pw) {
            this.pw = pw;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(500);
                    pw.write("Message " + i + "\r\n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (pw != null) {
                    try {
                        pw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    static class Reciver implements Runnable {
        PipedReader pr;

        public Reciver(PipedReader pr) {
            this.pr = pr;
        }

        @Override
        public void run() {
            try {
                char[] buff = new char[1024];
                int read;
                while ((read = pr.read(buff)) != -1) {
                    System.out.print(new String(buff, 0, read));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (pr != null) {
                    try {
                        pr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
