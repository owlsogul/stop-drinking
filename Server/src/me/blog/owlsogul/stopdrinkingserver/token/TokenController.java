package me.blog.owlsogul.stopdrinkingserver.token;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class TokenController {

	private TokenController() {}
	private static TokenController instance;
	public static TokenController getInstance() {
		if (instance == null) {
			instance = new TokenController();
		}
		return instance;
	}
	
	private HashMap<String, String> tokens = new HashMap<>();
	
	public String getMemberId(String token) {
		String memberId = null;
		try {
			memberId = AES256Util.aesDecode(token);
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return memberId;
	}
	
	public String getToken(String memberId) {
		return tokens.get(memberId);
	}
	
	public void setToken(String memberId, String token) {
		tokens.put(memberId, token);
	}
	
	public boolean isValidToken(String token) {
		try {
			String memberId = AES256Util.aesDecode(token);
			String originToken = tokens.get(memberId);
			System.out.printf("[토큰 비교] 주어진 토큰 : %s, 멤버 아이디 : %s, 찾아본 토큰 : %s\n", token, memberId, originToken);
			if (token.equals(originToken)) {
				return true;
			}
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String makeToken(String memberId) {
		String token = "";
		try {
			token = AES256Util.aesEncode(memberId);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
	}
	
}
