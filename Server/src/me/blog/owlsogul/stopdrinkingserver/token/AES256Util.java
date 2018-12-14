package me.blog.owlsogul.stopdrinkingserver.token;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AES256Util {
		private static String key = "stop-drinking-android";
	    private static String iv;
	    private static Key keySpec;
	 
	    // 암호화
	    public static String aesEncode(String str) throws java.io.UnsupportedEncodingException, 
	                                                    NoSuchAlgorithmException, 
	                                                    NoSuchPaddingException, 
	                                                    InvalidKeyException, 
	                                                    InvalidAlgorithmParameterException, 
	                                                    IllegalBlockSizeException, 
	                                                    BadPaddingException {
	    	iv = key.substring(0, 16);
	        byte[] keyBytes = new byte[16];
	        byte[] b = key.getBytes("UTF-8");
	        int len = b.length;
	        if (len > keyBytes.length) {
	            len = keyBytes.length;
	        }
	        System.arraycopy(b, 0, keyBytes, 0, len);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
	        keySpec = secretKeySpec;
	    	
	        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
	 
	        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
	        String enStr = new String(Base64.encodeBase64(encrypted));
	 
	        return enStr;
	    }
	 
	    //복호화
	    public static String aesDecode(String str) throws java.io.UnsupportedEncodingException,
	                                                        NoSuchAlgorithmException,
	                                                        NoSuchPaddingException, 
	                                                        InvalidKeyException, 
	                                                        InvalidAlgorithmParameterException,
	                                                        IllegalBlockSizeException, 
	                                                        BadPaddingException {
	    	iv = key.substring(0, 16);
	        byte[] keyBytes = new byte[16];
	        byte[] b = key.getBytes("UTF-8");
	        int len = b.length;
	        if (len > keyBytes.length) {
	            len = keyBytes.length;
	        }
	        System.arraycopy(b, 0, keyBytes, 0, len);
	        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
	        keySpec = secretKeySpec;
	    	
	        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
	 
	        byte[] byteStr = Base64.decodeBase64(str.getBytes());
	 
	        return new String(c.doFinal(byteStr),"UTF-8");
	    }
}
