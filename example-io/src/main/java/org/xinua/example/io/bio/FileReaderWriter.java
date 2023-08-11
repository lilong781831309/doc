package org.xinua.example.io.bio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileReaderWriter {

    public static void fileReader(File file) {
        // 使用文件名称创建流对象
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            int c = 0;
            while ((c = fr.read()) != -1) {
                System.out.println((char) c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void fileWriter(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write('你');
            fw.write('好');
            fw.write("朋友们");
            fw.write(97);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
