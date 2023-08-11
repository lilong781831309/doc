package org.xinua.example.io.bio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataStream {

    public static void dataInputStream(File file) {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(file));
            System.out.println("readBoolean: " + dis.readBoolean());
            System.out.println("readByte: " + dis.readByte());
            System.out.println("readChar: " + dis.readChar());
            System.out.println("readShort: " + dis.readShort());
            System.out.println("readInt: " + dis.readInt());
            System.out.println("readLong: " + dis.readLong());
            System.out.println("readFloat: " + dis.readFloat());
            System.out.println("readDouble: " + dis.readDouble());
            System.out.println("readUTF: " + dis.readUTF());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void dataOutputStream(File file) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(file));
            dos.writeBoolean(true);
            dos.writeByte(1);
            dos.writeChar('a');
            dos.writeShort(2);
            dos.writeInt(3);
            dos.writeLong(4);
            dos.writeFloat(5.123F);
            dos.writeDouble(6.01234);
            dos.writeUTF("test");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
