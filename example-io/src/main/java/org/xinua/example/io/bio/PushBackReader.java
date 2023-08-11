package org.xinua.example.io.bio;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.PushbackReader;

public class PushBackReader {

    public static void pushbackReader() {
        String data = "示例 pushbackReade";
        CharArrayReader charArrayReader = null;
        PushbackReader pushbackReader = null;
        try {
            charArrayReader = new CharArrayReader(data.toCharArray());
            pushbackReader = new PushbackReader(charArrayReader);

            int i = pushbackReader.read();
            System.out.println((char) i);

            pushbackReader.unread(i);

            char[] chs = new char[data.toCharArray().length];
            pushbackReader.read(chs);
            System.out.println(new String(chs));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (charArrayReader != null) {
                charArrayReader.close();
            }
            try {
                if (pushbackReader != null) {
                    pushbackReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
