package org.xinhua.example.zving.utility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {// NO_UCD

    /**
     * 系统换行符
     */
    public static final String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    /**
     * UTF-8的三个字节的BOM
     */
    public static final byte[] BOM = new byte[]{(byte) 239, (byte) 187, (byte) 191};

    /**
     * 十六进制字符
     */
    public static final char HexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * html标签正则表达式
     */
    public static Pattern PHtml = Pattern.compile("<[^>]+>", Pattern.DOTALL);

    /**
     * html注释正则表达式
     */
    public static Pattern PComment = Pattern.compile("<\\!\\-\\-.*?\\-\\->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); // 定义注释的正则表达式

    public static Pattern PInclude = Pattern.compile("<\\!\\-\\-#include .*?\\-\\->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); // 定义注释的正则表达式
    /**
     * script标签正则表达式
     */
    public static Pattern PScript = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); // 定义script的正则表达式

    /**
     * style标签正则表达式
     */
    public static Pattern PStyle = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); // 定义style的正则表达式

    /**
     * 将字符串进行md5摘要，然后输出成十六进制形式
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md = md5.digest(src.getBytes());
            return hexEncode(md);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串进行sh1摘要，然后输出成十六进制形式
     */
    public static String sha1Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA1");
            byte[] md = md5.digest(src.getBytes());
            return hexEncode(md);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将二进制数组转换成十六进制表示
     */
    public static String hexEncode(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = HexDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = HexDigits[0x0F & data[i]];
        }
        return new String(out);
    }

    /**
     * 将字符串转换成十六进制表示
     */
    public static byte[] hexDecode(String str) {
        char[] data = str.toCharArray();
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("StringUtil.hexEncode:Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("StringUtil.hexDecode:Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * 将字节数组转换成二制形式字符串
     */
    public static String byteToBin(byte[] bs) {
        char[] cs = new char[bs.length * 9];
        for (int i = 0; i < bs.length; i++) {
            byte b = bs[i];
            int j = i * 9;
            cs[j] = (b >>> 7 & 1) == 1 ? '1' : '0';
            cs[j + 1] = (b >>> 6 & 1) == 1 ? '1' : '0';
            cs[j + 2] = (b >>> 5 & 1) == 1 ? '1' : '0';
            cs[j + 3] = (b >>> 4 & 1) == 1 ? '1' : '0';
            cs[j + 4] = (b >>> 3 & 1) == 1 ? '1' : '0';
            cs[j + 5] = (b >>> 2 & 1) == 1 ? '1' : '0';
            cs[j + 6] = (b >>> 1 & 1) == 1 ? '1' : '0';
            cs[j + 7] = (b & 1) == 1 ? '1' : '0';
            cs[j + 8] = ',';
        }
        return new String(cs);
    }

    /**
     * 转换字节数组为十六进制字串
     */

    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte element : b) {
            resultSb.append(byteToHexString(element));
            resultSb.append(" ");
        }
        return resultSb.toString();
    }

    /**
     * 字节转换为十六进制字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HexDigits[d1] + "" + HexDigits[d2];
    }

    /**
     * 判断指定的二进制数组是否是一个UTF-8字符串
     */
    public static boolean isUTF8(byte[] bs) {
        if (bs.length > 3 && bs[0] == BOM[0] && bs[1] == BOM[1] && bs[2] == BOM[2]) {
            return true;
        }
        int encodingBytesCount = 0;
        for (byte element : bs) {
            byte c = element;
            if (encodingBytesCount == 0) {
                if ((c & 0x80) == 0) {// ASCII字符范围0x00-0x7F
                    continue;
                }
                if ((c & 0xC0) == 0xC0) {
                    encodingBytesCount = 1;
                    c <<= 2;
                    // 非ASCII第一字节用来存储长度
                    while ((c & 0x80) == 0x80) {
                        c <<= 1;
                        encodingBytesCount++;
                    }
                } else {
                    return false;// 不符合 UTF8规则
                }
            } else {
                // 后续字集必须以10开头
                if ((c & 0xC0) == 0x80) {
                    encodingBytesCount--;
                } else {
                    return false;// 不符合 UTF8规则
                }
            }
        }
        if (encodingBytesCount != 0) {
            return false;// 后续字节数不符合UTF8规则
        }
        return true;
    }

    /**
     * 将字符串转换成可以在JAVA表达式中直接使用的字符串，处理一些转义字符
     */
    public static String javaEncode(String txt) {
        FastStringBuilder sb = new FastStringBuilder();
        javaEncode(txt, sb);
        return sb.toStringAndClose();
    }

    /**
     * 将字符串转换成可以在JAVA表达式中直接使用的字符串，处理一些转义字符
     */
    public static void javaEncode(String txt, FastStringBuilder sb) {
        if (txt == null || txt.length() == 0) {
            return;
        }
        int last = 0;
        for (int i = 0; i < txt.length(); i++) {
            char c = txt.charAt(i);
            if (c == '\"' || c == '\\' || c == '\'') {
                if (last != i) {
                    sb.append(txt, last, i - last);
                }
                last = i + 1;
                sb.append('\\').append(c);
            } else if (c == '\n') {
                if (last != i) {
                    sb.append(txt, last, i - last);
                }
                last = i + 1;
                sb.append('\\').append('n');
            } else if (c == '\r') {
                if (last != i) {
                    sb.append(txt, last, i - last);
                }
                last = i + 1;
                sb.append('\\').append('r');
            } else if (c == '\t') {
                if (last != i) {
                    sb.append(txt, last, i - last);
                }
                last = i + 1;
                sb.append('\\').append('t');
            } else if (c == '\f') {
                if (last != i) {
                    sb.append(txt, last, i - last);
                }
                last = i + 1;
                sb.append('\\').append('f');
            } else if (c == '\b') {
                if (last != i) {
                    sb.append(txt, last, i - last);
                }
                last = i + 1;
                sb.append('\\').append('b');
            }
        }
        if (last == 0) {
            sb.append(txt);
        } else {
            sb.append(txt, last, txt.length() - last);
        }
    }

    /**
     * 将StringUtil.javaEncode()处理过的字符还原
     */
    public static String javaDecode(String txt) {
        if (txt == null || txt.length() == 0) {
            return txt;
        }
        FastStringBuilder sb = null;
        int lastIndex = 0;
        while (true) {
            int index = txt.indexOf('\\', lastIndex);
            if (index < 0) {
                break;
            }
            if (sb == null) {
                sb = new FastStringBuilder();
            }
            sb.append(txt.substring(lastIndex, index));
            if (index < txt.length() - 1) {
                char c = txt.charAt(index + 1);
                if (c == 'n') {
                    sb.append('\n');
                } else if (c == 'r') {
                    sb.append('\r');
                } else if (c == 't') {
                    sb.append('\t');
                } else if (c == 'f') {
                    sb.append('\f');
                } else if (c == '\'') {
                    sb.append('\'');
                } else if (c == '\"') {
                    sb.append('\"');
                } else if (c == '\\') {
                    sb.append('\\');
                }
                lastIndex = index + 2;
                continue;
            } else {
                sb.append(txt.substring(index, index + 1));
            }
            lastIndex = index + 1;
        }
        if (lastIndex == 0) {
            return txt;
        } else {
            sb.append(txt.substring(lastIndex));
        }
        return sb.toStringAndClose();
    }

    /**
     * 将一个字符串按照指下的分割字符串分割成数组。分割字符串不作正则表达式处理，<br>
     * String类的split方法要求以正则表达式分割字符串，有时较为不便，可以转为采用本方法。
     */
    public static String[] splitEx(String str, String spliter) {
        char escapeChar = '\\';
        if (spliter.equals("\\")) {
            escapeChar = '&';
        }
        return splitEx(str, spliter, escapeChar);
    }

    /**
     * 将一个字符串按照指下的分割字符串分割成数组。分割字符串不作正则表达式处理，<br>
     * String类的split方法要求以正则表达式分割字符串，有时较为不便，可以转为采用本方法。
     */
    public static String[] splitEx(String str, String spliter, char escapeChar) {
        if (str == null) {
            return new String[]{};
        }
        ArrayList<String> list = new ArrayList<String>();
        if (spliter == null || spliter.equals("") || str.length() < spliter.length()) {
            return new String[]{str};
        }
        int length = spliter.length();
        int lastIndex = 0;
        int lastStart = 0;
        while (true) {
            int i = str.indexOf(spliter, lastIndex);
            if (i >= 0) {
                if (i > 0 && str.charAt(i - 1) == escapeChar) {
                    lastIndex = i + 1;
                    continue;
                }
                list.add(str.substring(lastStart, i));
                lastStart = lastIndex = i + length;
            } else {
                if (lastStart <= str.length()) {
                    list.add(str.substring(lastStart));
                }
                break;
            }
        }
        String[] arr = new String[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * 将多个ID拼成的字符串转为long数组
     */
    public static long[] splitIDs(String ids) {
        return ObjectUtil.toLongArray(splitEx(ids, ","));
    }

    /**
     * 将多个ID拼成的字符串转为long数组
     */
    public static long[] splitIDs(String ids, String spliter) {
        return ObjectUtil.toLongArray(splitEx(ids, spliter));
    }

    /**
     * 将一个字符串中的指定片段全部替换，替换过程中不进行正则处理。<br>
     * 使用String类的replaceAll时要求片段以正则表达式形式给出，有时较为不便，可以转为采用本方法。<br>
     * 另:本方法和String类的replace(CharSequence target, CharSequence replacement)方法效果一样，但性能要快两倍。
     *
     * @param str
     * @param subStr
     * @param reStr
     * @return
     */
    public static String replaceEx(String str, String subStr, String reStr) {
        return replaceEx(str, subStr, reStr, false);
    }

    /**
     * 将一个字符串中的指定片段全部替换，替换过程中不进行正则处理。<br>
     * 使用String类的replaceAll时要求片段以正则表达式形式给出，有时较为不便，可以转为采用本方法。<br>
     * 另:本方法和String类的replace(CharSequence target, CharSequence replacement)方法效果一样，但性能要快两倍。
     */
    public static String replaceEx(String str, String subStr, String reStr, boolean ignoreCase) {
        if (str == null || str.length() == 0 || reStr == null) {
            return str;
        }
        if (subStr == null || subStr.length() == 0 || subStr.length() > str.length()) {
            return str;
        }
        StringBuilder sb = null;
        int lastIndex = 0;
        while (true) {
            int index = ignoreCase ? indexOfIgnoreCase(str, subStr, lastIndex) : str.indexOf(subStr, lastIndex);
            if (index < 0) {
                break;
            } else {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(str.substring(lastIndex, index));
                sb.append(reStr);
            }
            lastIndex = index + subStr.length();
        }
        if (lastIndex == 0) {
            return str;
        }
        sb.append(str.substring(lastIndex));
        return sb.toString();
    }

    /**
     * 从后面查找text中part出现的位置(忽略大小写)
     *
     * @param text
     * @param part
     * @return
     */
    public static int lastIndexOfIgnoreCase(String text, String part) {
        return lastIndexOfIgnoreCase(text, part, text.length());

    }

    /**
     * 从后面查找text中part出现的位置(忽略大小写)
     *
     * @param text
     * @param part
     * @param fromIndex
     * @return
     */
    public static int lastIndexOfIgnoreCase(String text, String part, int fromIndex) {
        int sourceCount = text.length();
        int targetCount = part.length();
        int rightIndex = sourceCount - targetCount;
        if (fromIndex < 0) {
            return -1;
        }
        if (fromIndex > rightIndex) {
            fromIndex = rightIndex;
        }
        /* Empty string always matches. */
        if (targetCount == 0) {
            return fromIndex;
        }

        int strLastIndex = targetCount - 1;
        char strLastChar = Character.toLowerCase(part.charAt(strLastIndex));
        int min = targetCount - 1;
        int i = min + fromIndex;

        startSearchForLastChar:
        while (true) {
            while (i >= min && Character.toLowerCase(text.charAt(i)) != strLastChar) {
                i--;
            }
            if (i < min) {
                return -1;
            }
            int j = i - 1;
            int start = j - (targetCount - 1);
            int k = strLastIndex - 1;

            while (j > start) {
                if (Character.toLowerCase(text.charAt(j--)) != Character.toLowerCase(part.charAt(k--))) {
                    i--;
                    continue startSearchForLastChar;
                }
            }
            return start + 1;
        }
    }

    /**
     * 忽略大小写的startWith
     *
     * @param text
     * @param prefix
     * @return
     */
    public static boolean startWithIgnoreCase(String text, String prefix) {
        if (text == prefix) {
            return true;
        } else {
            if (text == null) {
                return false;
            } else {
                if (prefix == null) {
                    return true;
                }
                if (text.length() < prefix.length()) {
                    return false;
                } else {
                    if (text.substring(0, prefix.length()).equalsIgnoreCase(prefix)) {
                        return true;
                    } else {
                        return false;
                    }
                }

            }
        }
    }

    /**
     * 忽略大小写的endWith
     *
     * @param text
     * @param suffix
     * @return
     */
    public static boolean endWithIgnoreCase(String text, String suffix) {
        if (text == suffix) {
            return true;
        } else {
            if (text == null) {
                return false;
            } else {
                if (suffix == null) {
                    return true;
                }
                if (text.length() < suffix.length()) {
                    return false;
                } else {
                    if (text.substring(text.length() - suffix.length()).equalsIgnoreCase(suffix)) {
                        return true;
                    } else {
                        return false;
                    }
                }

            }
        }
    }

    /**
     * 查找part字符串在text中的位置(不区分大小写)
     *
     * @param text
     * @param part
     * @return
     */
    public static int indexOfIgnoreCase(String text, String part) {
        return indexOfIgnoreCase(text, part, 0);
    }

    /**
     * 查找part字符串在text中的位置(不区分大小写)
     *
     * @param text
     * @param part
     * @param fromIndex
     * @return
     */
    public static int indexOfIgnoreCase(String text, String part, int fromIndex) {
        if (text == null) {
            return -1;
        }
        int plen = part.length();
        if (plen == 0) {
            return fromIndex;
        }
        int len = text.length();
        int max = len - plen;
        if (fromIndex > max) {
            return -1;
        }
        char first = Character.toLowerCase(part.charAt(0));
        for (int i = fromIndex; i <= max; i++) {
            /* 查找第一个匹配的字符. */
            if (Character.toLowerCase(text.charAt(i)) != first) {
                while (++i <= max && Character.toLowerCase(text.charAt(i)) != first)
                    ;
            }
            /* 找到第一个匹配字符，匹配后面的 */
            if (i <= max) {
                int j = i + 1;
                int end = j + plen - 1;
                for (int k = 1; j < end && Character.toLowerCase(text.charAt(j)) == Character.toLowerCase(part.charAt(k)); j++, k++)
                    ;// 比较后面的每一个字符,直到part的结尾或者不相等的字符

                if (j == end) {// 如果比较了每一个字符,说明全部相等,返回当前位置
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 不区分大小写的全部替换，替换时使用了正则表达式。
     */
    public static String replaceAllIgnoreCase(String source, String oldstring, String newstring) {
        Pattern p = Pattern.compile(oldstring, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
        return m.replaceAll(newstring);
    }

    /**
     * 以指定编码进行URL编码
     */
    public static String urlEncode(String str, String charset) {
        try {
            return URLEncoder.encode(str, charset);
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * 以指定编码进行URL解码
     */
    public static String urlDecode(String str, String charset) {
        try {
            return URLDecoder.decode(str, charset);
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * Javascript中escape的JAVA实现
     */
    public static String escape(String src) {
        char j;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
                sb.append(j);
            } else if (j < 256) {
                sb.append("%");
                if (j < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toString(j, 16));
            } else {
                sb.append("%u");
                sb.append(Integer.toString(j, 16));
            }
        }
        return sb.toString();
    }

    /**
     * Javascript中unescape的JAVA实现
     */
    public static String unescape(String src) {
        if (ObjectUtil.empty(src)) {
            return src;
        }
        StringBuilder sb = new StringBuilder();
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    sb.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    sb.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    sb.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    sb.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 在一字符串左边填充若干指定字符，使其长度达到指定长度
     */
    public static String leftPad(String srcString, char c, int length) {
        if (srcString == null) {
            srcString = "";
        }
        int tLen = srcString.length();
        int i, iMax;
        if (tLen >= length) {
            return srcString;
        }
        iMax = length - tLen;
        StringBuilder sb = new StringBuilder();
        for (i = 0; i < iMax; i++) {
            sb.append(c);
        }
        sb.append(srcString);
        return sb.toString();
    }

    /**
     * 将长度超过length的字符串截取length长度，若不足，则返回原串
     */
    public static String subString(String src, int length) {
        if (src == null) {
            return null;
        }
        int i = src.length();
        if (i > length) {
            return src.substring(0, length);
        } else {
            return src;
        }
    }

    /**
     * 将长度超过length的字符串截取length长度，若不足，则返回原串。<br>
     * 其中ASCII字符算1个长度单位，非ASCII字符算2个长度单位。
     */
    public static String subStringEx(String src, int length) {
        if (src == null) {
            return null;
        }
        int m = 0;
        try {
            byte[] b = src.getBytes("Unicode");
            boolean byteFlag = b[0] == -2;// 表明unicode字节顺中的高位在前
            for (int i = 2; i < b.length; i += 2) {
                if (b[byteFlag ? i : i + 1] == 0) {// ascii字符
                    m++;
                } else {
                    m += 2;
                }
                if (m > length) {
                    return src.substring(0, (i - 2) / 2);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("String.getBytes(\"Unicode\") failed");
        }
        return src;
    }

    /**
     * 获得字符串的长度，其中ASCII字符算1个长度单位,非ASCII字符算两个长度单位
     */
    public static int lengthEx(String src) {
        return lengthEx(src, false);
    }

    /**
     * 获得字符串的长度，其中ASCII字符算1个长度单位,如果UTF8Flag为ture,则非ASCII字符算3个字节，否则非ASCII字符算2个字节
     */
    public static int lengthEx(String src, boolean UTF8Flag) {
        if (ObjectUtil.empty(src)) {
            return 0;
        }
        int length = 0;
        try {
            byte[] b = src.getBytes("Unicode");
            boolean byteFlag = b[0] == -2;// 表明unicode字节顺中的高位在前
            for (int i = 2; i < b.length; i += 2) {
                if (b[byteFlag ? i : i + 1] == 0) {// ascii字符
                    length++;
                } else {
                    if (UTF8Flag) {
                        length += 3;
                    } else {
                        length += 2;
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("String.getBytes(\"Unicode\") failed");
        }
        return length;
    }

    /**
     * 在一字符串右边填充若干指定字符，使其长度达到指定长度
     */
    public static String rightPad(String srcString, char c, int length) {
        if (srcString == null) {
            srcString = "";
        }
        int tLen = srcString.length();
        int i, iMax;
        if (tLen >= length) {
            return srcString;
        }
        iMax = length - tLen;
        StringBuilder sb = new StringBuilder();
        sb.append(srcString);
        for (i = 0; i < iMax; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 清除字符串右端的空格
     */
    public static String rightTrim(String src) {
        if (ObjectUtil.empty(src)) {
            return src;
        }
        for (int i = src.length() - 1; i >= 0; i--) {
            if (!isSpace(src.charAt(i))) {
                return src.substring(0, i + 1);
            }
        }
        return src;
    }

    /**
     * 清除掉字符串左端的空格
     */
    public static String leftTrim(String src) {
        if (ObjectUtil.empty(src)) {
            return src;
        }
        for (int i = 0; i < src.length(); i++) {
            if (!isSpace(src.charAt(i))) {
                return src.substring(i);
            }
        }
        return src;
    }

    public static boolean isSpace(char c) {
        return c == ' ' || c == 160 || c == '\r' || c == '\n' || c == '\t' || c == '\b' || c == '\f';
    }

    /**
     * 历遍所有字符集，看哪种字符集下可以正确转化
     */
    public static void printStringWithAnyCharset(String str) {
        Map<String, Charset> map = Charset.availableCharsets();
        for (String key1 : map.keySet()) {
            for (String key2 : map.keySet()) {
                System.out.print("\t");
                try {
                    System.out.println("From " + key1 + " To " + key2 + ":" + new String(str.getBytes(key1.toString()), key2.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 半角转全角，转除英文字母之外的字符
     */
    public static String toSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] > 64 && c[i] < 91 || c[i] > 96 && c[i] < 123) {
                continue;
            }

            if (c[i] < 127) {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 半角转全角，转所有能转为全角的字符，包括英文字母
     */
    public static String toNSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }

            if (c[i] < 127) {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 全角转半角的函数 全角空格为12288，半角空格为32 <br>
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换
     *
     * @param cnStr
     *            String 字符串
     * @return String 转换成全拼后的字符串
     */
    public static String getChineseFullSpelling(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }
        return ChineseSpelling.getSpelling(cnStr);
    }

    /**
     * 返回姓名拼音，每个汉字拼音的首字母大写
     *
     * @param cnStr
     * @return
     */
    public static String getChinesePersonNameSpelling(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }
        return ChineseSpelling.getPersonNameSpelling(cnStr);
    }

    public static String getChineseCapitalizedSpelling(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }
        return ChineseSpelling.getCapitalizedSpelling(cnStr);
    }

    public static final Pattern PTitle = Pattern.compile("<title>(.+?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * 从一段html文本中提取出<title>标签内容
     */
    public static String getHtmlTitle(File f) {
        String html = FileUtil.readText(f);
        String title = getHtmlTitle(html);
        return title;
    }

    /**
     * 从一段html文本中提取出<title>标签内容
     */
    public static String getHtmlTitle(String html) {
        Matcher m = PTitle.matcher(html);
        if (m.find()) {
            return m.group(1).trim();
        }
        return null;
    }

    /**
     * 根据正则规则过滤文本
     *
     * @param text
     * @param patterns
     * @return
     */
    public static String clearByPatterns(String text, Pattern... patterns) {
        if (isEmpty(text)) {
            return text;
        }
        for (Pattern pattern : patterns) {
            Matcher m = pattern.matcher(text);
            text = m.replaceAll(""); // 过滤script标签
        }
        return text;

    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return new StringBuilder().append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    /**
     * 字符串是否为空，null或空字符串时返回true,其他情况返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 字符串是否不为空，null或空字符串时返回false,其他情况返回true
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }

    /**
     * 字符串是否为空，null或空字符或者为"null"字符串时返回true
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return isEmpty(str) || "null".equals(str);
    }

    /**
     * 字符串是否不为空，null,空字符串,或者"null" 字符串时返回false,其他情况返回true
     *
     * @param str
     * @return
     */
    public static boolean isNotNull(String str) {
        return isNotEmpty(str) && !"null".equals(str);
    }

    /**
     * 字符串为空时返回defaultString，否则返回原串
     */
    public static final String noNull(String string, String defaultString) {
        return isEmpty(string) ? defaultString : string;
    }

    /**
     * 字符串为空时返回defaultString，否则返回空字符串
     */
    public static final String noNull(String string) {
        return noNull(string, "");
    }

    /**
     * 将一个数组拼成一个字符串，数组项之间以逗号分隔
     */
    public static String join(Object arr) {
        return join(arr, ",");
    }

    /**
     * 将一个数组以指定的分隔符拼成一个字符串
     */
    public static String join(Object arr, String spliter) {
        if (arr == null) {
            return null;
        }
        if (!arr.getClass().isArray()) {
            return arr.toString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Array.getLength(arr); i++) {
            if (i != 0) {
                sb.append(spliter);
            }
            sb.append(Array.get(arr, i));
        }
        return sb.toString();
    }

    /**
     * 将一个数组拼成一个字符串，数组项之间以逗号分隔
     */
    public static String join(Object[] arr) {
        return join(arr, ",");
    }

    /**
     * 将一个二维数组拼成一个字符串，第二维以逗号分隔，第一维以换行分隔
     */
    public static String join(Object[][] arr) {
        return join(arr, "\n", ",");
    }

    /**
     * 将一个数组以指定的分隔符拼成一个字符串
     */
    public static String join(Object[] arr, String spliter) {
        if (arr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                sb.append(spliter);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 将一个二维数组拼成一个字符串，第二维以指定的spliter2参数分隔，第一维以换行spliter1分隔
     */
    public static String join(Object[][] arr, String spliter1, String spliter2) {
        if (arr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                sb.append(spliter2);
            }
            sb.append(join(arr[i], spliter2));
        }
        return sb.toString();
    }

    /**
     * 将一个List拼成一个字符串，数据项之间以逗号分隔
     */
    public static String join(Collection<?> list) {
        return join(list, ",");
    }

    /**
     * 将一个List拼成一个字符串，数据项之间以指定的参数spliter分隔
     */
    public static String join(Collection<?> c, String spliter) {
        if (c == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object name : c) {
            if (first) {
                first = false;
            } else {
                sb.append(spliter);
            }
            sb.append(name);
        }
        return sb.toString();
    }

    /**
     * 计算一个字符串中某一子串出现的次数
     */
    public static int count(String str, String findStr) {
        int lastIndex = 0;
        int length = findStr.length();
        int count = 0;
        int start = 0;
        while ((start = str.indexOf(findStr, lastIndex)) >= 0) {
            lastIndex = start + length;
            count++;
        }
        return count;
    }

    public static final Pattern PLetterOrDigit = Pattern.compile("^\\w*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static final Pattern PLetter = Pattern.compile("^[A-Za-z]*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static final Pattern PDigit = Pattern.compile("^\\d*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * 判断字符串是否全部由数字或字母组成
     */
    public static boolean isLetterOrDigit(String str) {
        return PLetterOrDigit.matcher(str).find();
    }

    /**
     * 判断字符串是否全部字母组成
     */
    public static boolean isLetter(String str) {
        return PLetter.matcher(str).find();
    }

    /**
     * 判断字符串是否全部由数字组成
     */
    public static boolean isDigit(String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        return PDigit.matcher(str).find();
    }

    private static Pattern PChinese = Pattern.compile("[^\u4e00-\u9fa5]+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * 判断字符串中是否含有中文字符
     */
    public static boolean containsChinese(String str) {
        if (!PChinese.matcher(str).matches()) {
            return true;
        }
        return false;
    }

    private static Pattern PID = Pattern.compile("[\\w\\s\\_\\.\\,]*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * 检查ID，防止SQL注入，主要是在删除时传入多个ID时使用
     */
    public static boolean checkID(String str) {
        if (StringUtil.isEmpty(str)) {
            return true;
        }
        if (PID.matcher(str).matches()) {
            return true;
        }
        return false;
    }

    /**
     * 得到URL的文件扩展名
     */
    public static String getURLExtName(String url) {
        if (isEmpty(url)) {
            return null;
        }
        int index1 = url.indexOf('?');
        if (index1 == -1) {
            index1 = url.length();
        }
        int index2 = url.lastIndexOf('.', index1);
        if (index2 == -1) {
            return null;
        }
        int index3 = url.indexOf('/', 8);
        if (index3 == -1) {
            return null;
        }
        String ext = url.substring(index2 + 1, index1);
        if (ext.matches("[^\\/\\\\]*")) {
            return ext;
        }
        return null;
    }

    /**
     * 得到URL的文件名
     */
    public static String getURLFileName(String url) {
        if (isEmpty(url)) {
            return null;
        }
        int index1 = url.indexOf('?');
        if (index1 == -1) {
            index1 = url.length();
        }
        int index2 = url.lastIndexOf('/', index1);
        if (index2 == -1 || index2 < 8) {
            return null;
        }
        String ext = url.substring(index2 + 1, index1);
        return ext;
    }

    /**
     * 将一个GBK编码的字符串转成UTF-8编码的二进制数组，转换后没有BOM位
     */
    public static byte[] GBKToUTF8(String chinese) {
        return GBKToUTF8(chinese, false);
    }

    /**
     * 将一个GBK编码的字符串转成UTF-8编码的二进制数组，如果参数bomFlag为true，则转换后有BOM位
     */
    public static byte[] GBKToUTF8(String chinese, boolean bomFlag) {
        return CharsetConvert.GBKToUTF8(chinese, bomFlag);
    }

    /**
     * 将UTF-8编码的字符串转成GBK编码的字符串
     */
    public static byte[] UTF8ToGBK(String chinese) {
        return CharsetConvert.UTF8ToGBK(chinese);
    }

    /**
     * 判断一个文件名是否合法(目前仅判断了是否包含斜杠与控制字符)
     *
     * @param fileName
     * @return
     */
    public static boolean checkForFileName(String fileName) {
        if (isEmpty(fileName)) {
            return false;
        }
        fileName = fileName.trim();
        int len = fileName.length();
        if (len > 255) {
            return false;
        } else {
            char firstChar = fileName.charAt(0);
            if (firstChar == '/' || firstChar == '\\') {
                return false;
            }
            char c;
            for (int i = 1; i < len; i++) {
                c = fileName.charAt(i);
                if (c <= 31 || c == 127) {
                    return false;
                } else {
                    continue;
                }
            }
            return true;
        }

    }

    /**
     * 检测变量名是否合法
     * (长度在1-255个字符,必须以 '_','$',字母 开头,并且只能包含'_','$',字母,数字)
     *
     * @param varName
     * @return
     */
    public static boolean checkForVarName(String varName) {
        if (isEmpty(varName)) {
            return false;
        }
        int len = varName.length();
        if (len > 255) {
            return false;
        } else {
            char firstChar = varName.charAt(0);
            if (firstChar != '_' && firstChar != '$' && !(firstChar >= 'a' && firstChar <= 'z' || firstChar >= 'A' && firstChar <= 'Z')) {
                return false;
            }
            char c;
            for (int i = 1; i < len; i++) {
                c = varName.charAt(i);
                if (c == '_' || c == '$' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c >= '0' && c <= '9') {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        }
    }


    /**
     * 字符串格式化，类似于参数化SQL以及JS中的String.format()。<br>
     * 示例：StringUtil.format("&lt;div&gt;?&lt;/div&gt;?","txt0","txt1"); <br>
     * 返回 "&lt;div&gt;txt0&lt;/div&gt;txt1"
     */
    public static String format(String str, Object... vs) {
        return StringFormat.format(str, vs);
    }

    /**
     * 拼接字符串
     */
    public static String concat(Object... args) {
        if (ObjectUtil.empty(args)) {
            return null;
        }
        FastStringBuilder sb = new FastStringBuilder();
        for (Object arg : args) {
            if (ObjectUtil.notEmpty(arg)) {
                sb.append(arg);
            }
        }
        return sb.toStringAndClose();
    }

    /**
     * 快速地Html转码，只转码空格、单双引号、左右尖括号以及&amp;符号。<br>
     * 一般只需转码这6个字符就可以了，其他符号在页面字符集为UTF-8时都能够不转码直接显示。
     */
    public static String quickHtmlEncode(String html) {
        if (isEmpty(html)) {
            return html;
        }
        boolean flag = false;
        for (int j = 0; j < html.length(); j++) {
            char c = html.charAt(j);
            if (c == ' ' || c == '\"' || c == '\'' || c == '<' || c == '>' || c == '&') {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return html;
        }
        FastStringBuilder sb = new FastStringBuilder();
        int last = 0;
        for (int j = 0; j < html.length(); j++) {
            char c = html.charAt(j);
            if (c == ' ' || c == '\"' || c == '\'' || c == '<' || c == '>' || c == '&') {
                if (last != j) {
                    sb.append(html.substring(last, j));
                }
                if (c == '\"') {
                    sb.append("&quot;");
                } else if (c == '\'') {
                    sb.append("&apos;");
                } else if (c == ' ') {
                    sb.append("&#32;");// 不能使用&nbsp;，在有些浏览器会转为charcode=160的不间断空格
                } else if (c == '<') {
                    sb.append("&lt;");
                } else if (c == '>') {
                    sb.append("&gt;");
                } else if (c == '&') {
                    sb.append("&amp;");
                }
                last = j + 1;
            }
        }
        if (last != html.length()) {
            sb.append(html.substring(last));
        }
        return sb.toStringAndClose();
    }

    /**
     * 快速地Html解码，只解码&amp;nbsp;、&amp;lt;、&amp;gt;、&amp;quot;、&amp;apos;、&amp;amp;，<br>
     * 以及&amp#0032;、&amp#x0032;之类的以数字标识的Unicode字符
     */
    public static String quickHtmlDecode(String html) {
        boolean flag = false;
        for (int j = 0; j < html.length(); j++) {
            if (html.charAt(j) == '&') {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return html;
        }
        char[] buf = new char[html.length()];
        int j = 0;
        for (int i = 0; i < html.length(); i++) {
            char c = html.charAt(i);
            if (c == '&') {
                if (html.startsWith("&quot;", j)) {
                    buf[j++] = '\"';
                    i += 5;
                    continue;
                } else if (html.startsWith("&amp;", i)) {
                    buf[j++] = '&';
                    i += 4;
                    continue;
                } else if (html.startsWith("&lt;", i)) {
                    buf[j++] = '<';
                    i += 3;
                    continue;
                } else if (html.startsWith("&gt;", i)) {
                    buf[j++] = '>';
                    i += 3;
                    continue;
                } else if (html.startsWith("&apos;", i)) {
                    buf[j++] = '\'';
                    i += 5;
                } else if (html.startsWith("&nbsp;", i)) {
                    buf[j++] = ' ';
                    i += 5;
                    continue;
                } else if (html.startsWith("&#32;", i)) {
                    buf[j++] = ' ';
                    i += 4;
                    continue;
                } else if (i + 1 < html.length() && html.charAt(i + 1) == '#') {
                    int k = i + 2;
                    flag = false;
                    int radix = 10;
                    if (html.charAt(k) == 'x' || html.charAt(k) == 'X') {// 十六制进
                        radix = 16;
                        k++;
                    }
                    for (; k < i + 9 && k < html.length(); k++) {
                        if (html.charAt(k) == ';') {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        char ch = (char) Integer.parseInt(html.substring(radix == 10 ? i + 2 : i + 3, k), radix);
                        buf[j++] = ch;
                        i += k;
                    }
                }
            } else {
                buf[j++] = c;
            }
        }
        return new String(buf, 0, j);
    }

    /**
     * 获取32位长度的UUID 16进制字符串
     *
     * @return
     */
    public static String randomUUID() {
        UUID uuid = UUID.randomUUID();
        return leftPad(Long.toHexString(uuid.getMostSignificantBits()), '0', 16)
                + leftPad(Long.toHexString(uuid.getLeastSignificantBits()), '0', 16);
    }
}
