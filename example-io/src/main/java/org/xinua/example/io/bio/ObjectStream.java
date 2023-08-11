package org.xinua.example.io.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectStream {

    public static void objectInputStream(File file) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            System.out.println("readBoolean: " + ois.readBoolean());
            System.out.println("readByte: " + ois.readByte());
            System.out.println("readChar: " + ois.readChar());
            System.out.println("readShort: " + ois.readShort());
            System.out.println("readInt: " + ois.readInt());
            System.out.println("readLong: " + ois.readLong());
            System.out.println("readFloat: " + ois.readFloat());
            System.out.println("readDouble: " + ois.readDouble());
            System.out.println("readUTF: " + ois.readUTF());
            System.out.println("readObject: " + ((Person) ois.readObject()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void objectOutputStream(File file) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeBoolean(true);
            oos.writeByte(1);
            oos.writeChar('a');
            oos.writeShort(2);
            oos.writeInt(3);
            oos.writeLong(4);
            oos.writeFloat(5.123F);
            oos.writeDouble(6.01234);
            oos.writeUTF("test");
            oos.writeObject(new Person("张三", 20));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Person implements Serializable {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

}
