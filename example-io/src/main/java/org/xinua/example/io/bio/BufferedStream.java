package org.xinua.example.io.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BufferedStream {

    public static String bufferedInputStream(File file) {
        StringBuilder sb = new StringBuilder();
        BufferedInputStream bis = null;
        byte[] buf = new byte[1024];
        int n = -1;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            while ((n = bis.read(buf)) > 0) {
                sb.append(new String(buf, 0, n));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static void bufferedOutputStream(File file) {
        BufferedOutputStream bos = null;
        try {
            String str = "test bufferedOutputStream";
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
