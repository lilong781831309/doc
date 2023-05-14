package org.xinhua.example.zving.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {

	public static byte[] getBytesFromStream(InputStream is) throws IOException {
		return getBytesFromStream(is, Integer.MAX_VALUE);
	}

	public static byte[] getBytesFromStream(InputStream is, int max) throws IOException {
		byte[] buffer = new byte[1024];
		int read = -1;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] data = null;
		try {
			while ((read = is.read(buffer)) != -1) {
				if (read > 0) {
					byte[] chunk = null;
					if (read == 1024) {
						chunk = buffer;
					} else {
						chunk = new byte[read];
						System.arraycopy(buffer, 0, chunk, 0, read);
					}
					bos.write(chunk);
				}
			}
			if (bos.size() > max) {
				throw new IOException("InputStream length is out of range,max=" + max);
			}
			data = bos.toByteArray();
		} finally {
			if (bos != null) {
				bos.close();
				bos = null;
			}
		}
		return data;
	}

	public static final long ONE_KB = 1024;
	public static final long ONE_MB = ONE_KB * ONE_KB;
	public static final long ONE_GB = ONE_KB * ONE_MB;
	public static final long ONE_TB = ONE_KB * ONE_GB;
	public static final long ONE_PB = ONE_KB * ONE_TB;
	public static final long ONE_EB = ONE_KB * ONE_PB;

	/**
	 * 字节数可读性转换
	 */
	public static String byteCountToDisplaySize(long size) {
		return formatByteSize(size);
	}

	/**
	 * 将字节大小转换为不同单位的大小显示
	 * 
	 * @param byteSize
	 * @return
	 */
	public static String formatByteSize(long byteSize) {
		return formatByteSize(byteSize, "0");
	}

	/**
	 * 将字节大小转换为不同单位的大小显示
	 * 
	 * @param byteSize
	 * @param zeroValue
	 * @return
	 */
	public static String formatByteSize(long byteSize, String zeroValue) {
		double dataSize = byteSize;
		if (dataSize >= ONE_EB) {
			return Math.round(dataSize / ONE_EB * 100) / 100D + "EB";
		} else if (dataSize >= ONE_PB) {
			return Math.round(dataSize / ONE_PB * 100) / 100D + "PB";
		} else if (dataSize >= ONE_TB) {
			return Math.round(dataSize / ONE_TB * 100) / 100D + "TB";
		} else if (dataSize >= ONE_GB) {
			return Math.round(dataSize / ONE_GB * 100) / 100D + "GB";
		} else if (dataSize >= ONE_MB) {
			return Math.round(dataSize / ONE_MB * 100) / 100D + "MB";
		} else if (dataSize >= ONE_KB) {
			return Math.round(dataSize / ONE_KB * 100) / 100D + "KB";
		} else if (dataSize > 0) {
			return Math.round(dataSize) + "B";
		} else {
			return zeroValue;
		}
	}

	/**
	 * 解析字节大小(只能包含整数、小数、空格、逗号 及字节大小单位，如果包含其他字符则会抛出运行时异常)
	 * 
	 * @param sizeString
	 * @return
	 */
	public static long parseByteSize(String sizeString) {
		return parseByteSize(sizeString, false);
	}

	/**
	 * 解析字节大小(只能包含整数、小数、空格、逗号 及字节大小单位)
	 * 
	 * @param sizeString 要解析的字符串
	 * @param checkFormat 是否检测字符串格式(如果为false则忽略所有非格式字符)
	 * @return
	 */
	public static long parseByteSize(String sizeString, boolean checkFormat) {
		String source = sizeString;
		if (sizeString != null) {
			sizeString = sizeString.trim();
		}
		if (StringUtil.isEmpty(sizeString)) {
			if (checkFormat) {
				throw new NullPointerException();
			} else {
				return 0;
			}
		}
		sizeString = sizeString.toUpperCase();
		int len = sizeString.length();
		StringBuilder sbNum = new StringBuilder();
		long result = -1;
		long unitSize = 0;
		for (int i = 0; i < len; i++) {
			char c = sizeString.charAt(i);
			if (unitSize > 0) {// 如果已经识别到了单位字符
				if (c == 'B') {// 则后面的字符只能是B,Byte或空格
					if (!checkFormat) {// 如果不检查格式就终止检测
						break;
					} else {// 否则抛出异常
						if (unitSize == 1) {// 单位是B 重复的字符B
							throw new RuntimeException("无法识别的大小格式,错误的字符'" + source.charAt(i) + "'(位置" + (i + 1) + ")");
						}
						if (sizeString.length() > i + 1) {
							String endStr = sizeString.substring(i);
							if (endStr.equals("BYTE")) {
								break;
							} else {
								throw new RuntimeException("无法识别的大小格式,错误的单位'" + source.substring(i + 1) + "'(位置" + (i + 1) + ")");
							}
						} else {
							break;
						}
					}
				} else if (c == ' ') {
					continue;
				} else {
					if (!checkFormat) {// 如果不检查格式就终止检测
						break;
					} else {// 否则抛出异常
						throw new RuntimeException("无法识别的大小格式,错误的字符'" + source.charAt(i) + "'(位置" + (i + 1) + ")");
					}
				}
			} else {// 如果还没有任何单位
				if (c >= '0' && c <= '9' || c == '.') {// 如果是数字
					sbNum.append(c);
				} else {
					switch (c) {
					case 'B':
						unitSize = 1;
						if (!checkFormat) {// 如果不检查格式就终止检测
							break;
						} else {// 否则抛出异常
							if (sizeString.length() > i + 1) {
								String endStr = sizeString.substring(i);
								if (endStr.equals("BYTE")) {
									break;
								} else {
									throw new RuntimeException("无法识别的大小格式,错误的单位'" + source.substring(i + 1) + "'(位置" + (i + 1) + ")");
								}
							} else {
								break;
							}
						}
					case 'K':
						unitSize = ONE_KB;
						break;
					case 'M':
						unitSize = ONE_MB;
						break;
					case 'G':
						unitSize = ONE_GB;
						break;
					case 'T':
						unitSize = ONE_TB;
						break;
					case 'P':
						unitSize = ONE_PB;
						break;
					case 'E':
						unitSize = ONE_EB;
						break;
					case ' ':
						continue;
					case ',':
						continue;
					default:
						if (checkFormat) {
							throw new RuntimeException("错误的数据大小格式,包含不支持的字符'" + source.charAt(i) + "'(位置" + (i + 1) + ")");
						}
					}
				}
			}
		}
		if (unitSize == 0) {// 默认单位Byte
			unitSize = 1;
		}
		result = ((Double) (Double.valueOf(sbNum.toString()) * unitSize)).longValue();
		return result;
	}
}
