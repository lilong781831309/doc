package org.xinua.example.io.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TryWithResource {

    public void test() {
        try (
                FileInputStream fis = new FileInputStream("d:/1.txt");
                InputStreamReader isr = new InputStreamReader(fis, "GBK");
                BufferedReader br = new BufferedReader(isr);

                FileOutputStream fos = new FileOutputStream("1.txt");
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);
        ) {
            String str;
            while ((str = br.readLine()) != null) {
                bw.write(str);
                bw.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
