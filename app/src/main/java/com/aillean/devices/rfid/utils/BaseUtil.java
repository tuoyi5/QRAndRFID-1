package com.aillean.devices.rfid.utils;

public class BaseUtil {

	/**
	 * 指定位置拷贝数组
	 */
	public static void memcpy(byte[] bytesTo, byte[] bytesFrom, int len) {
		memcpy(bytesTo, 0, bytesFrom, 0, len);
	}

	/**
	 * 
	 */
	public static void memcpy(byte[] bytesTo, int startIndexTo, byte[] bytesFrom, int startIndexFrom, int len) {
		while (len-- > 0) {
			bytesTo[startIndexTo++] = bytesFrom[startIndexFrom++];
		}
	}

	/**
	 * 从起始位开始拷贝数组
	 */
	public static boolean memcmp(byte[] bytes1, byte[] bytes2, int len) {

		if (len < 1) {
			return false;
		}

		int startIndex = 0;
		while (len-- > 0) {
			if (bytes1[startIndex] != bytes2[startIndex]) {
				return false;
			}
			startIndex++;
		}
		return true;
	}

	/**
	 * byte数组转十六进制形式的字符串
	 */
	public static String getHexString(byte[] b, int length) {
		return getHexString(b, length, "");
	}

	/**
	 * 指定分割符 转换
	 */
	public static String getHexString(byte[] b, int length, String split) {
		StringBuilder hex = new StringBuilder("");
		String temp = null;
		for (int i = 0; i < length; i++) {
			temp = Integer.toHexString(b[i] & 0xFF);
			if (temp.length() == 1) {
				temp = '0' + temp;
			}
			hex.append(temp + split);
		}
		return hex.toString().trim().toUpperCase();
	}

	/**
	 * 字符串 转 十六进制形式的数组
	 */
	public static byte[] getHexByteArray(String hexString) {
		byte[] buffer = new byte[hexString.length() / 2];
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			buffer[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return buffer;
	}

	/**
	 * 字符串 转 十六进制形式的数组（指定长度 ）
	 */
	public static int getHexByteArray(String hexString, byte[] buffer, int nSize) {

		hexString.replace(" ", "");
		if (nSize > hexString.length() / 2) {
			nSize = hexString.length() / 2;
			if (hexString.length() == 1) {
				nSize = 1;
				String str = "0";
				hexString = str + hexString;
			}
		}
		char[] hexChars = hexString.toCharArray();
		for (int i = 0; i < nSize; i++) {
			int pos = i * 2;
			buffer[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return nSize;
	}

	/**
	 * 
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * byte数组转十六进制形式的字符串
	 */
	public static String ByteArrayToString(byte[] bt_ary) {
		StringBuilder sb = new StringBuilder();
		if (bt_ary != null)
			for (byte b : bt_ary) {
				sb.append(String.format("%02X ", b));
			}
		return sb.toString();
	}
}
