package cn.bforce.common.utils.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.bforce.common.persistence.OperationException;
import cn.bforce.common.utils.security.CryptRSA;




public class ExDigestUtils {
	
	protected static final Logger log = LogManager.getLogger(ExDigestUtils.class);

	public static String sha256Hex(String data){
		return DigestUtils.sha256Hex(data);
	}
	
	public static String sha256Base64(String data){
		return base64Encode(DigestUtils.sha256(data));
	}
	
	public static String hmacSHA256(String data, String password) {
		
		try{
			
			// 初始化HmacMD5摘要算法的密钥产生器  
	        KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");  
	        // 产生密钥  
	        SecretKey secretKey = generator.generateKey();  
	        
			// 还原密钥  
	        SecretKey secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "HmacSHA256");  
	        // 实例化Mac  
	        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
	      
	        //初始化mac  
	        mac.init(secretKeySpec);  
	        
	        //执行消息摘要  
	        byte[] digest = mac.doFinal(data.getBytes());
	        
	        //转为十六进制的字符串 
	        return   getHexString(digest);
	        
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
		}		
		return null;
	}
	
	public static String sha1Base64(String data){
		return base64Encode(DigestUtils.sha1(data));
	}
	
	
	public static String sha1Base64(byte[] data){
		return base64Encode(DigestUtils.sha1(data));
	}
	
	public static String sha1Hex(byte[] data){
		return DigestUtils.sha1Hex(data);
	}

	public static String md5Hex(String data){
		return DigestUtils.md5Hex(data);
	}
	
	public static String md5Hex(byte[] data){
		return DigestUtils.md5Hex(data);
	}

	/**
	 * Base64解码
	 * @param b
	 * @return
	 */
	public static String base64Decode(byte[] bytes) {
		return new String(Base64.decodeBase64(bytes));
	}
	
	/**
	 * Base64编码
	 * @param input
	 * @return
	 */
	public static String base64Encode(String input){		
		return base64Encode(input.getBytes());
	}
	
	/**
	 * Base64编码
	 * @param bytes
	 * @return
	 */
	public static String base64Encode(byte[] bytes){		
		return new String(Base64.encodeBase64(bytes));
	}
	
	
	public static String getHexString(byte[] b) throws Exception {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
		 
	public static byte[] getByteArray(String hexString) {
		return new BigInteger(hexString, 16).toByteArray();
	}
	public static String getPublicKey(InputStream inputStream){
		
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
			// 公钥
			PublicKey pk = cert.getPublicKey();
			
			return base64Encode(pk.getEncoded());

		} catch (Exception ex) {
			throw new OperationException(ex);
		}
	}
	
	/**
	 * 
	 * @param inputStream
	 * @param password
	 * @return
	 */
	public static PrivateKey getPrivateKey(InputStream inputStream, String password){
		return getPrivateKey(inputStream, password, null);
	}
		
		
	public static PrivateKey getPrivateKey(InputStream inputStream, String password, String keyAlias){
		
		try {
			
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(inputStream, password.toCharArray());
			
			if (keyAlias == null){
				Enumeration<String> enum1 = keyStore.aliases();
				if (enum1.hasMoreElements()) {
					keyAlias = enum1.nextElement();
				}
			}
			// 私钥
			PrivateKey priK = (PrivateKey) keyStore.getKey(keyAlias, password.toCharArray());
			return priK;
			
		}catch(Exception ex){
			throw new OperationException(ex);
		}
	}
	
	public static String buildSignUseSHA1withRSA(InputStream inputStream, String password, String data){
		try {
			PrivateKey privateKey = getPrivateKey(inputStream, password);
			CryptRSA rsa = new CryptRSA("SHA1withRSA");
			// 私钥签名
			return rsa.sign(data.getBytes(), privateKey);
		}catch(Exception ex){
			throw new OperationException(ex);
		}
	}
	
	public static boolean verifySignUseSHA1withRSA(InputStream inputStream, String data, String sign){
		try {
			String publicKey = getPublicKey(inputStream);
			CryptRSA rsa = new CryptRSA("SHA1withRSA");
			// 公钥验签
			return rsa.verify(data.getBytes(), publicKey, sign);
		}catch(Exception ex){
			throw new OperationException(ex);
		}
	}
	
	public static String encryptByPublicKey(InputStream inputStream, String data, String sign){
		try {
			String publicKey = getPublicKey(inputStream);
			CryptRSA rsa = new CryptRSA();
			// 公钥验签
			return new String(rsa.encryptByPublicKey(data.getBytes(), publicKey));
		}catch(Exception ex){
			throw new OperationException(ex);
		}
	}
	
	
	public static byte[] toByteArray(InputStream input) throws IOException {
		int BUFFER_SIZE = 4096;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[BUFFER_SIZE];
		int len = 0;
		while ((len = input.read(b, 0, BUFFER_SIZE)) != -1) {
			baos.write(b, 0, len);
		}
		baos.flush();

		byte[] bytes = baos.toByteArray();
		return bytes;
	}
	
}
