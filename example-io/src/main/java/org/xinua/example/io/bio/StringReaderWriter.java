package org.xinua.example.io.bio;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class StringReaderWriter {

    //必须使用一个Reader来作为参数传递时，但你的数据源又仅仅是一个String类型数据
    StringReader sr = new StringReader("just a test~");


    //必须使用一个Writer来作为参数传递时
    public static void stringWriter() {
        StringWriter sw = new StringWriter();
        sw.write(1);
        sw.write("str");
        fn(sw);
    }

    public static void fn(Writer writer) {

    }

    // 将堆栈跟踪转换为 String
    public static void stringWriter2() {
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String s = sw.toString(); //我们现在可以将堆栈跟踪作为字符串
        }
    }
}
