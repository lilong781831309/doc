package org.xinua.example.io.bio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ByteArrayStream {

    public static void byteArrayInputStream() {
        byte[] buf = {1, 2, 3, 'a', 'b', 'c'};
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        int val = 0;
        System.out.println("====int value===");
        while ((val = bais.read()) != -1) {
            System.out.println(val);
        }
    }

    public static void byteArrayOutputStream() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(1);
        baos.write(2);
        baos.write(3);
        baos.write('a');
        baos.write('b');
        baos.write('c');
        byte[] buf = baos.toByteArray();
        System.out.println("====int value===");
        for (int i = 0; i < buf.length; i++) {
            System.out.println(buf[i]);
        }
    }
}
