package org.xinhua.example.zving.utility;

import org.xinhua.example.zving.exception.SkipException;

/**
 * Base64编码/解码类
 * <ul>
 * <li>支持使用自定义Base64 字符字典</li>
 * <li>支持设置行字符数或者不换行</li>
 * <li>支持设置补位字符或者无补位</li>
 * <li>线程安全，高效，比sun.misc.BASE64Decoder/BASE64Encoder的实现要高效很多</li>
 * <li>兼容性提升：decode方法支持解析不换行、无补位、URL安全和标准格式的Base64</li>
 * </ul>
 */
public class ZBase64 {
	/**
	 * 默认字符字典(大写26字母、小写26字母、数字、'+'、'/')
	 */
	public static final char[] CHAR_TABLE_DEFAULT = {
			/* 0, '1', '2', '3', '4', '5', '6', '7' CHAR_TABLE_DEFAULT */
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // 0
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 1
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 2
			'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 3
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 4
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 5
			'w', 'x', 'y', 'z', '0', '1', '2', '3', // 6
			'4', '5', '6', '7', '8', '9', '+', '/', // 7
	};
	/**
	 * URL安全的字符字典(大写26字母、小写26字母、数字、'-'、'_')
	 */
	public static final char[] CHAR_TABLE_URLSAFE = {
			/* 0, '1', '2', '3', '4', '5', '6', '7' CHAR_TABLE_URL_SAFE */
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // 0
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 1
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 2
			'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 3
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 4
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 5
			'w', 'x', 'y', 'z', '0', '1', '2', '3', // 6
			'4', '5', '6', '7', '8', '9', '-', '_', // 7
	};

	/**
	 * 默认补位字符'='
	 */
	public static final char DEFAULT_FILL_CHAR = '=';

	/**
	 * 默认一行宽度76
	 */
	public static final char DEFAULT_LINEWIDTH = 76;

	/**
	 * 不在字符列表中的状态值
	 */
	protected static final int NOT_IN_TABLE_VALUE = -1;
	/**
	 * 跳过的字符状态值
	 */
	protected static final int SKIP_VALUE = -2;
	/**
	 * 补位字符状态值
	 */
	protected static final int PADDING_VALUE = -3;

	/**
	 * 索引位62的字符(在decode时需支持的字符)
	 */
	protected static final char[] CHARS_62 = new char[] { '+', '-' };
	/**
	 * 索引位63的字符(在decode时需支持的字符)
	 */
	protected static final char[] CHARS_63 = new char[] { '/', '_' };

	/**
	 * 跳过的字符(在decode时跳过的字符)
	 */
	protected static final char[] CHARS_SKIP = new char[] { '\r', '\n' };
	/**
	 * 补位字符
	 */
	protected static final char[] CHARS_PADDING = new char[] { '=' };

	public static final ZBase64 B64_URLSAFE = new ZBase64(CHAR_TABLE_URLSAFE, 0, false);

	public static final ZBase64 B64_DEFAULT = new ZBase64();

	public static final ZBase64 B64_InLine = new ZBase64(CHAR_TABLE_DEFAULT, 0, true);

	public static String encode(byte[] data) {
		return B64_DEFAULT.encode2Base64(data);
	}

	public static byte[] decode(String base64) {
		return B64_DEFAULT.decodeBase64(base64);
	}

	public static String encodeURLSafe(byte[] data) {
		return B64_URLSAFE.encode2Base64(data);
	}

	public static String encodeInLine(byte[] data) {
		return B64_InLine.encode2Base64(data);
	}

	/**
	 * Base64字符字典
	 */
	protected char[] charTable = null; // Base64字符列表
	/**
	 * Base64反查字典(Base64字符索引对应的Byte数据)
	 * 值为-2 为正常要跳过的字符,-1为不支持的ASCII字符
	 */
	protected byte[] dTable;
	/**
	 * 换行符
	 */
	protected String line = StringUtil.SYSTEM_LINE_SEPARATOR;
	/**
	 * 补位字符
	 */
	protected char fillChar = 0;
	/**
	 * 每行字符数，0表示不换行
	 */
	protected int lineWidth = 0;
	protected boolean check = true;
	/**
	 * 两位的补位字符串
	 */
	protected String padding_TWO;
	/**
	 * 一位的补位字符串
	 */
	protected String padding_ONE;

	/**
	 * 默认构造
	 * <p>
	 * 默认字符字典，76个字符一换行，使用'='补位
	 * </p>
	 */
	public ZBase64() {
		this(CHAR_TABLE_DEFAULT, DEFAULT_LINEWIDTH, DEFAULT_FILL_CHAR);
	}

	/**
	 * 构造
	 * 
	 * @param charTable 指定字符字典
	 * @param lineWidth 指定每行字符数(0为不换行)
	 * @param fillChar 指定补位字符
	 */
	public ZBase64(char[] charTable, int lineWidth, char fillChar) {
		this.charTable = charTable.clone();
		this.lineWidth = lineWidth;
		this.fillChar = fillChar;
		init();
	}

	/**
	 * 构造
	 * 
	 * @param charTable 指定字符字典
	 * @param lineWidth 指定每行字符数(0为不换行)
	 * @param paddingFill 是否补位
	 */
	public ZBase64(char[] charTable, int lineWidth, boolean paddingFill) {
		this.charTable = charTable.clone();
		this.lineWidth = lineWidth;
		this.fillChar = paddingFill ? DEFAULT_FILL_CHAR : (char) 0;
		init();
	}

	/**
	 * 初始化反查字典
	 */
	protected void init() {
		dTable = new byte[255];
		for (int i = 0; i < dTable.length; i++) {// 初始化反查字典 状态初始为NOT_IN_TABLE_VALUE
			dTable[i] = NOT_IN_TABLE_VALUE;
		}
		for (int i = 0; i < charTable.length; i++) {// 初始化64位字符的反查数据
			dTable[charTable[i]] = (byte) i;
		}
		for (int i = 0; i < line.length(); i++) {// 初始化需跳过的字符(换行符)的反查数据
			dTable[line.charAt(i)] = SKIP_VALUE;
		}
		for (int i = 0; i < CHARS_62.length; i++) {// 初始化最后两位的反查数据
			dTable[CHARS_62[i]] = 62;
		}
		for (int i = 0; i < CHARS_63.length; i++) {// 初始化最后两位的反查数据
			dTable[CHARS_63[i]] = 63;
		}
		for (int i = 0; i < CHARS_SKIP.length; i++) {// 初始化需跳过的字符的反查数据
			dTable[CHARS_SKIP[i]] = SKIP_VALUE;
		}
		for (int i = 0; i < CHARS_PADDING.length; i++) {// 初始化补位字符的反查数据
			dTable[CHARS_PADDING[i]] = PADDING_VALUE;
		}
		padding_ONE = new String(new char[] { fillChar });// 初始化补位字符串
		padding_TWO = new String(new char[] { fillChar, fillChar });
	}

	/**
	 * 编码为Base64字符串
	 * 
	 * @param data
	 * @return
	 */
	public String encode2Base64(byte[] data) {
		int size = data.length * 8 / 6;
		if (data.length * 8 % 6 > 0) {
			size += 1;
		}
		int index = 0;
		char[] charary = new char[size];
		int len = data.length;
		int temp = 0;
		String padding = null;
		for (int i = 0; i < len; i++) {
			temp = (data[i] & 255) << 16;
			i++;
			if (i >= len) {
				charary[index++] = charTable[(temp >>> 18 & 63)];
				charary[index++] = charTable[(temp >>> 12 & 63)];
				padding = padding_TWO;
				break;
			}
			temp = temp | ((data[i] & 255) << 8);
			i++;
			if (i >= len) {
				charary[index++] = charTable[(temp >>> 18 & 63)];
				charary[index++] = charTable[(temp >>> 12 & 63)];
				charary[index++] = charTable[(temp >>> 6 & 63)];
				padding = padding_ONE;
				break;
			}
			temp = temp | (data[i] & 255);
			charary[index++] = charTable[(temp >>> 18 & 63)];
			charary[index++] = charTable[(temp >>> 12 & 63)];
			charary[index++] = charTable[(temp >>> 6 & 63)];
			charary[index++] = charTable[(temp & 63)];
		}

		if (lineWidth > 0) {
			int lineCount = index / lineWidth;
			if (index % lineWidth > 0) {
				lineCount = lineCount + 1;
			}
			int outLength = index + ((lineCount - 1) * line.length());
			char[] tmp = new char[outLength];
			int outLineWidth = line.length() + lineWidth;
			char[] lineBreakChar = line.toCharArray();
			int j = 0;
			for (; j < lineCount - 1; j++) {
				System.arraycopy(charary, lineWidth * j, tmp, outLineWidth * j, lineWidth);
				System.arraycopy(lineBreakChar, 0, tmp, outLineWidth * j + lineWidth, lineBreakChar.length);
			}
			System.arraycopy(charary, lineWidth * j, tmp, outLineWidth * j, index - lineWidth * j);
			charary = tmp;
		}
		if (fillChar != 0 && padding != null) {
			return new String(charary) + padding;
		}
		return new String(charary);
	}

	protected byte readByte(String base64, int index, int step) throws RuntimeException {
		char charIndex = base64.charAt(index);
		if (charIndex < 0 || charIndex > 255) {
			throw new RuntimeException("ZBase64  char '" + charIndex + "' error at index:" + index);
		}
		byte readByte = dTable[charIndex];
		if (readByte >= 0) {
			return readByte;
		} else if (readByte == SKIP_VALUE) {
			if (step == 0) {
				return -1;
			} else {
				throw new RuntimeException("ZBase64 can't decode, char '" + charIndex + "' error location at index:" + index);
			}
		} else if (readByte == PADDING_VALUE) {
			if (step < 2) {
				throw new RuntimeException("ZBase64 can't decode, String is not full, length is not enough, at index:" + index);
			} else {
				throw new SkipException(step);
			}
		} else {
			throw new RuntimeException("ZBase64 char '" + charIndex + "' error at index:" + index);
		}
	}

	/**
	 * 解码Base64字符串
	 * 
	 * @param base64
	 * @return
	 */
	public byte[] decodeBase64(String base64) {
		int size = base64.length() * 6 / 8;
		if (base64.length() * 6 % 8 > 0) {
			size += 1;
		}
		byte[] result = new byte[size];
		int index = 0;
		int len = base64.length();// 读取到最后前两位前
		byte readByte = 0;
		int i = 0;
		int tmp = 0;
		try {
			for (; i < len; i++) {
				readByte = readByte(base64, i, 0);
				if (readByte == -1) {
					continue;
				}
				tmp = readByte << 18;
				i++;
				if (i >= len) {
					throw new RuntimeException("ZBase64 can't decode, String is not full, length is not enough, at index:" + i);
				}
				readByte = readByte(base64, i, 1);
				tmp = tmp | readByte << 12;

				i++;
				if (i >= len) {
					result[index++] = (byte) (tmp >>> 16 & 255);
					break;
				}
				readByte = readByte(base64, i, 2);
				tmp = tmp | readByte << 6;

				i++;
				if (i >= len) {
					result[index++] = (byte) (tmp >>> 16 & 255);
					result[index++] = (byte) (tmp >>> 8 & 255);
					break;
				}
				readByte = readByte(base64, i, 3);
				tmp = tmp | readByte;

				result[index++] = (byte) (tmp >>> 16 & 255);
				result[index++] = (byte) (tmp >>> 8 & 255);
				result[index++] = (byte) (tmp & 255);
			}

		} catch (SkipException e) {
			int step = (Integer) e.getSkipData();
			switch (step) {
			case 2:
				result[index++] = (byte) (tmp >>> 16 & 255);
				break;
			case 3:
				result[index++] = (byte) (tmp >>> 16 & 255);
				result[index++] = (byte) (tmp >>> 8 & 255);
			}
		}
		if (size != index) {
			byte[] tmpAry = new byte[index];
			System.arraycopy(result, 0, tmpAry, 0, index);
			result = tmpAry;
		}
		return result;
	}

}
