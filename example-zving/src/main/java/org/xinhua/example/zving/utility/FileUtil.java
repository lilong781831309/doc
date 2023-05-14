package org.xinhua.example.zving.utility;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    /**
     * 将文件路径规则化，去掉其中多余的/和\，去掉可能造成文件信息泄漏的../
     */
    public static String normalizePath(String path) {
        path = path.replace('\\', '/');
        path = replacePath(path, "../", "/");
        path = replacePath(path, "./", "/");
        if (path.endsWith("..")) {
            path = path.substring(0, path.length() - 2);
        }
        path = path.replaceAll("/+", "/");
        return path;
    }

    /**
     * 重复替换文件路径
     *
     * @param path
     * @param subStr
     * @param reStr
     * @return
     */
    public static String replacePath(String path, String subStr, String reStr) {
        if (path.indexOf(subStr) == -1) {
            return path;
        }
        path = StringUtil.replaceEx(path, subStr, reStr);
        return replacePath(path, subStr, reStr);
    }

    public static File normalizeFile(File f) {
        String path = f.getAbsolutePath();
        path = normalizePath(path);
        return new File(path);
    }

    /**
     * 得到文件名中的扩展名，不带圆点。
     */
    public static String getExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index < 0) {
            return null;
        }
        String ext = fileName.substring(index + 1);
        return ext.toLowerCase();
    }

    /**
     * 以全局编码将指定内容写入指定文件
     *
     * @param fileName
     * @param content
     * @return
     */
    public static boolean writeText(String fileName, String content) {
        return writeText(fileName, content, false);
    }

    /**
     * 以全局编码将指定内容写入指定文件
     *
     * @param fileName
     * @param content
     * @param append
     * @return
     */
    public static boolean writeText(String fileName, String content, boolean append) {
        return writeText(fileName, content, append, StandardCharsets.UTF_8.name());
    }

    /**
     * 以指定编码将指定内容写入指定文件
     *
     * @param fileName
     * @param content
     * @param encoding
     * @return
     */
    public static boolean writeText(String fileName, String content, String encoding) {
        return writeText(fileName, content, false, encoding);
    }

    /**
     * 以指定编码将指定内容写入指定文件
     *
     * @param fileName
     * @param content
     * @param append
     * @param encoding
     * @return
     */
    public static boolean writeText(String fileName, String content, boolean append, String encoding) {
        return writeText(fileName, content, append, encoding, false);
    }

    /**
     * 以指定编码将指定内容写入指定文件，如果编码为UTF-8且bomFlag为true,则在文件头部加入3字节的BOM
     *
     * @param fileName
     * @param content
     * @param encoding
     * @param bomFlag
     * @return
     */
    public static boolean writeText(String fileName, String content, String encoding, boolean bomFlag) {
        return writeText(fileName, content, false, encoding, bomFlag);
    }

    /**
     * 以指定编码将指定内容写入指定文件，如果编码为UTF-8且bomFlag为true,则在文件头部加入3字节的BOM
     *
     * @param fileName
     * @param content
     * @param append
     * @param encoding
     * @param bomFlag
     * @return
     */
    public static boolean writeText(String fileName, String content, boolean append, String encoding, boolean bomFlag) {
        fileName = normalizePath(fileName);
        try {
            byte[] bs = content.getBytes(encoding);
            if (encoding.equalsIgnoreCase("UTF-8") && bomFlag) {
                bs = ArrayUtils.addAll(StringUtil.BOM, bs);
            }
            writeByte(fileName, bs, append);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 将二进制数组写入指定文件
     *
     * @param fileName
     * @param b
     * @return
     */
    public static boolean writeByte(String fileName, byte[] b) {
        return writeByte(fileName, b, false);
    }

    /**
     * 将二进制数组写入指定文件
     *
     * @param fileName
     * @param b
     * @param append
     * @return
     */
    public static boolean writeByte(String fileName, byte[] b, boolean append) {
        fileName = normalizePath(fileName);
        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(fileName, append));
            os.write(b);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将二进制数组写入指定文件
     *
     * @param f
     * @param b
     * @return
     */
    public static boolean writeByte(File f, byte[] b) {
        return writeByte(f, b, false);
    }

    /**
     * 将二进制数组写入指定文件
     *
     * @param f
     * @param b
     * @param append
     * @return
     */
    public static boolean writeByte(File f, byte[] b, boolean append) {
        f = normalizeFile(f);
        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(f, append));
            os.write(b);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 以全局编码读取指定文件中的文本
     *
     * @param f
     * @return
     */
    public static String readText(File f) {
        return readText(f, StandardCharsets.UTF_8.name());
    }

    /**
     * 以指定编码读取指定文件中的文本
     *
     * @param f
     * @param encoding
     * @return
     */
    public static String readText(File f, String encoding) {
        f = normalizeFile(f);
        InputStream is = null;
        try {
            is = new FileInputStream(f);
            return readText(is, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 以全局编码读取指定文件中的文本
     *
     * @param fileName
     * @return
     */
    public static String readText(String fileName) {
        return readText(fileName, StandardCharsets.UTF_8.name());
    }

    /**
     * 以指定编码读取指定文件中的文本
     *
     * @param fileName
     * @param encoding
     * @return
     */
    public static String readText(String fileName, String encoding) {
        fileName = normalizePath(fileName);
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
            return readText(is, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 以全局编码读取流中的文本
     *
     * @param is
     * @return
     */
    public static String readText(InputStream is) {
        return readText(is, StandardCharsets.UTF_8.name());
    }

    /**
     * 以指定编码读取流中的文本
     *
     * @param is
     * @param encoding
     * @return
     */
    public static String readText(InputStream is, String encoding) {
        try {
            byte[] bs = readByte(is);
            if (encoding.equalsIgnoreCase("utf-8")) {// 如果是UTF8则要判断有没有BOM
                if (StringUtil.hexEncode(ArrayUtils.subarray(bs, 0, 3)).equals("efbbbf")) {// BOM标志
                    bs = ArrayUtils.subarray(bs, 3, bs.length);
                }
            }
            return new String(bs, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以二进制方式读取文件
     *
     * @param fileName
     * @return
     */
    public static byte[] readByte(String fileName) {
        fileName = normalizePath(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
            byte[] r = new byte[fis.available()];
            fis.read(r);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 以二进制方式读取文件
     *
     * @param f
     * @return
     */
    public static byte[] readByte(File f) {
        f = normalizeFile(f);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            byte[] r = readByte(fis, true);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取指定流，并转换为二进制数组
     *
     * @param is
     * @return
     */
    public static byte[] readByte(InputStream is) {
        return readByte(is, false);
    }

    /**
     * 读取指定流，并转换为二进制数组
     *
     * @param is
     * @param closeStream 读取完成后是否关闭流
     * @return
     */
    public static byte[] readByte(InputStream is, boolean closeStream) {
        try {
            byte[] buffer = new byte[8192];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            while (true) {
                int bytesRead = -1;
                try {
                    bytesRead = is.read(buffer);
                } catch (IOException e) {
                    throw new RuntimeException("File.readByte() failed");
                }
                if (bytesRead == -1) {
                    break;
                }
                try {
                    os.write(buffer, 0, bytesRead);
                } catch (Exception e) {
                    throw new RuntimeException("File.readByte() failed");
                }
            }
            return os.toByteArray();
        } finally {
            if (closeStream && is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 尝试获取文件输出流,如果文件或文件所在目录不存在则会尝试创建
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static FileOutputStream getOutputStream(File file) throws FileNotFoundException {
        return getOutputStream(file, false);
    }

    /**
     * 尝试获取文件输出流,如果文件或文件所在目录不存在则会尝试创建
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static FileOutputStream getOutputStream(File file, boolean append) throws FileNotFoundException {
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile.exists()) {
                if (!parentFile.isDirectory()) {
                    throw new FileNotFoundException("无法创建目录,已存在同名文件：" + parentFile.getAbsolutePath());
                }
            } else {
                parentFile.mkdirs();
            }
        }
        return new FileOutputStream(file, append);
    }

    /**
     * 尝试获取获取文件输出流
     *
     * @param outPath
     * @return
     * @throws FileNotFoundException
     */
    public static FileOutputStream getOutputStream(String outPath) throws FileNotFoundException {
        return getOutputStream(new File(outPath));
    }

    /**
     * 判断文件或文件夹是否存在
     */
    public static boolean exists(String path) {
        path = normalizePath(path);
        File dir = new File(path);
        return dir.exists();
    }

    /**
     * 将可序列化对象序列化并写入指定文件
     */
    public static void serialize(Serializable obj, String fileName) {// NO_UCD
        fileName = normalizePath(fileName);
        ObjectOutputStream s = null;
        try {
            FileOutputStream f = new FileOutputStream(fileName);
            s = new ObjectOutputStream(f);
            s.writeObject(obj);
            s.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将可序列化对象序列化并返回二进制数组
     */
    public static byte[] serialize(Serializable obj) {
        ObjectOutputStream s = null;
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            s = new ObjectOutputStream(b);
            s.writeObject(obj);
            s.flush();
            return b.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从指定文件中反序列化对象
     */
    public static Object unserialize(String fileName) {// NO_UCD
        fileName = normalizePath(fileName);
        ObjectInputStream s = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            s = new ObjectInputStream(in);
            Object o = s.readObject();
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从二进制数组中反序列化对象
     */
    public static Object unserialize(byte[] bs) {
        ObjectInputStream s = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bs);
            s = new ObjectInputStream(in);
            Object o = s.readObject();
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
