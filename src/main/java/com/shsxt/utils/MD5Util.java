package com.shsxt.utils;

import java.security.MessageDigest;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * 将字符串通过md5算法加密
 * @author Lisa Li
 *
 */
public class MD5Util {

	public static String encode(String str) {
		String value = null;
		try {
			// 得到md5的算法程序对象
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			// 将字符串加密，返回字节数组
			byte[] bytes = messageDigest.digest(str.getBytes());
			// 将字节通过Base64转换成字符串
			value = Base64.encodeBase64String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static void main(String[] args) {
		System.out.println(encode(encode("1234")));
	}
	
}


