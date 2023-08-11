package org.xinua.example.io.bio;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CharArrayReaderWriter {

    public static void charArrayReader() {
        // 使用文件名称创建流对象
        CharArrayReader car = null;
        try {
            char[] chs = {'你', '好', ',', 'h', 'e', 'l', 'l', 'o'};
            car = new CharArrayReader(chs);
            int val = 0;
            System.out.println("====char value===");
            while ((val = car.read()) != -1) {
                System.out.println((char) val);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (car != null) {
                car.close();
            }
        }
    }

    public static void charArrayWriter() {
        CharArrayWriter caw = new CharArrayWriter();
        caw.write('a');
        caw.write('b');
        caw.write('c');
        caw.write('d');
        caw.write('e');
        char[] chs = caw.toCharArray();

        System.out.println("====char value===");
        for (int i = 0; i < chs.length; i++) {
            System.out.println((char) chs[i]);
        }
    }
}
