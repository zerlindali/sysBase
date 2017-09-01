package cn.bforce.common.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p class="detail">
 * 功能：加解密工具类，支持MD5，SHA,SHA1,AES(对称加密)算法，
 * </p>
 * 
 * @ClassName: DescriptUtil
 * @version V1.0
 * @date 2015-8-18
 * @author tangy Copyright 2015 b-force.cn, Inc. All rights reserved
 */
public class EncryptUtil {

	/**
	 * <p class="detail">
	 * 功能：SHA1摘要算法
	 * </p>
	 * 
	 * @author tangy
	 * @date 2015-8-18
	 * @param content
	 *            待加密字符串
	 * @return
	 */
	public static String SHA1(String content) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(content.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * <p class="detail">
	 * 功能：SHA加密算法
	 * </p>
	 * 
	 * @author tangy
	 * @date 2015-8-18
	 * @param content
	 *            待加密字符串
	 * @return
	 */
	public static String SHA(String content) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA");
			digest.update(content.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * <p class="detail">
	 * 功能：MD5摘要算法
	 * </p>
	 * 
	 * @author tangy
	 * @date 2015-8-18
	 * @param content
	 *            待加密字符串
	 * @return
	 */
	public static String MD5(String content) {
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(content.getBytes());
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < md.length; i++) {
				String shaHex = Integer.toHexString(md[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * <p class="detail">
	 * 功能：AES加密
	 * </p>
	 * 
	 * @author tangy
	 * @date 2015-8-18
	 * @param content
	 *            待加密字符串
	 * @param password
	 *            秘钥
	 * @return
	 */
	public static String encryptAES(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return new String(result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p class="detail">
	 * 功能：AES解密
	 * </p>
	 * 
	 * @author tangy
	 * @date 2015-8-18
	 * @param content
	 *            待解密字符串
	 * @param password
	 *            秘钥
	 * @return
	 */
	public static String decryptAES(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return new String(result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p class="detail">
	 * 功能：base64解密
	 * </p>
	 * 
	 * @author tangy
	 * @date 2015-8-18
	 * @param content
	 * @return
	 */
	public static String encryptBASE64(String content) {
		String retValue = null;
		if (content != null) {
				retValue=Base64Util.put(content);
				//retValue = new String(new sun.misc.BASE64Decoder().decodeBuffer(content));
		}
		return retValue;
	}
	/**
	 * <p class="detail">
	 * 功能：base64加密
	 * </p>
	 * 
	 * @author tangy
	 * @date 2015-8-18
	 * @param content
	 * @return
	 */
	public static String decryptBASE64(String content) {
		String retValue = null;
		if (content != null) {
			retValue = Base64Util.get(content);
			//retValue = new sun.misc.BASE64Encoder().encode(content.getBytes());
		}
		return retValue;
	}

	public static void main(String[] args){
		String enStr="25sdd的奋斗1";
		String key="123456";
		String deStr="";
		String deEnStr="";
		System.out.println("原字符串："+enStr);
		System.out.println("原秘钥："+key);
		//base64加密
		deStr=EncryptUtil.encryptBASE64(enStr);
		System.out.println("base64加密后:"+deStr);
		deEnStr=EncryptUtil.decryptBASE64(deStr);
		System.out.println("base64解密后："+deEnStr);
		
		//aes
	}
}