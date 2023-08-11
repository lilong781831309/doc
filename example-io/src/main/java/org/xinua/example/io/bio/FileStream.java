package org.xinua.example.io.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileStream {

    public static String fileInputStream(File file) {
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;
        byte[] buf = new byte[1024];
        int n = -1;
        try {
            fis = new FileInputStream(file);
            while ((n = fis.read(buf)) > 0) {
                sb.append(new String(buf, 0, n));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static void fileOutputStream(File file) {
        FileOutputStream fos = null;
        try {
            String str = "test fileOutputStream";
            fos = new FileOutputStream(file);
            fos.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
