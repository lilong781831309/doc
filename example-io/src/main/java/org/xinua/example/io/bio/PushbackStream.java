package org.xinua.example.io.bio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class PushbackStream {

    public static void pushbackInputStream() {
        String data = "This an example of PushbackInputStream";
        ByteArrayInputStream byteArrayInputStream = null;
        PushbackInputStream pushbackInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(data.getBytes());
            pushbackInputStream = new PushbackInputStream(byteArrayInputStream);

            //Read first character from stream
            int i = pushbackInputStream.read();
            System.out.println((char) i);

            //Push back first character to stream
            pushbackInputStream.unread(i);

            //Now Read full bytes
            byte b[] = new byte[data.getBytes().length];
            pushbackInputStream.read(b);
            System.out.println(new String(b));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pushbackInputStream != null) {
                    pushbackInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
