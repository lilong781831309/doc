package org.xinua.example.io.bio;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

public class PrintStreamWriter {

    public static void printStream() throws IOException {
        PrintStream ps = new PrintStream(new FileOutputStream("C:\\ps.txt"));
        System.setOut(ps);
        System.out.println("abc");
        System.out.println("def");
    }

    public static void printWriter() throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\pw.txt"))), true);
        pw.println("abc");
        pw.println("def");
        pw.close();
    }
}
