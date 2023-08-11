package org.xinua.example.io.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;

public class SequenceStream {

    public static void sequenceInputStream(File srcFile1, File srcFile2, File targetFile) {
        SequenceInputStream sis = null;
        FileOutputStream fos = null;

        try {
            InputStream is1 = new FileInputStream(srcFile1);
            InputStream is2 = new FileInputStream(srcFile2);
            fos = new FileOutputStream(targetFile);
            sis = new SequenceInputStream(is1, is2);

            int c = 0;
            while ((c = sis.read()) != -1) {
                fos.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sis != null) {
                    sis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
