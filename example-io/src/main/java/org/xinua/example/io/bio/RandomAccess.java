package org.xinua.example.io.bio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccess {

    public static final int KB = 1024;
    public static final int MB = KB * KB;

    public static void main(String[] args) {
        File file = new File("");

        long length = file.length();
        int pieceSize = MB;
        int pieceCount = (int) (length % pieceSize == 0 ? (length / pieceSize) : (length / pieceSize + 1));

        for (int i = 0; i < pieceCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RandomAccessFile raf = null;
                    try {
                        byte[] buff = new byte[pieceSize];
                        raf = new RandomAccessFile(file, "1");
                        raf.seek(pieceCount * pieceSize);
                        int read = raf.read(buff);
                        //do samething
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (raf != null) {
                            try {
                                raf.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }).start();
        }
    }
}
