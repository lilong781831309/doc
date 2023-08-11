package org.xinua.example.io.bio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StreamReaderWriter {

    public static void inputStreamReader() {
        InputStreamReader isr = null;
        InputStreamReader isr2 = null;
        try {
            // 定义文件路径,文件为gbk编码
            String FileName = "E:\\file_gbk.txt";

            // 创建流对象,默认UTF8编码
            isr = new InputStreamReader(new FileInputStream(FileName));
            // 创建流对象,指定GBK编码
            isr2 = new InputStreamReader(new FileInputStream(FileName), "GBK");
            // 定义变量,保存字符
            int read;
            // 使用默认编码字符流读取,乱码
            while ((read = isr.read()) != -1) {
                System.out.print((char) read); // ��Һ�
            }

            // 使用指定编码字符流读取,正常解析
            while ((read = isr2.read()) != -1) {
                System.out.print((char) read);// 大家好
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (isr2 != null) {
                    isr2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void outputStreamWriter() {
        OutputStreamWriter osw = null;
        OutputStreamWriter osw2 = null;
        try {
            // 定义文件路径
            String FileName = "E:\\out.txt";
            // 创建流对象,默认UTF8编码
            osw = new OutputStreamWriter(new FileOutputStream(FileName));
            // 写出数据
            osw.write("你好"); // 保存为6个字节
            osw.close();

            // 定义文件路径
            String FileName2 = "E:\\out2.txt";
            // 创建流对象,指定GBK编码
            osw2 = new OutputStreamWriter(new FileOutputStream(FileName2), "GBK");
            // 写出数据
            osw2.write("你好");// 保存为4个字节
            osw2.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
