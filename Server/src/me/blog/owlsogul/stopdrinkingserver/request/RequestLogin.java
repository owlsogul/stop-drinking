package me.blog.owlsogul.stopdrinkingserver.request;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;

import me.blog.owlsogul.stopdrinkingserver.database.DAOMember;
import me.blog.owlsogul.stopdrinkingserver.database.Member;
import me.blog.owlsogul.stopdrinkingserver.token.AES256Util;

public class RequestLogin extends Request{

	
	
	@Override
	public String request(String command, JSONObject dataObj) {
		String memberId = (String) dataObj.get("memberId");
		String memberPw = (String) dataObj.get("memberPw");
		DAOMember dao = new DAOMember();
		boolean loginResult = dao.login(Member.createMember(memberId, memberPw, null));
		JSONObject resObj = new JSONObject();
		if (loginResult) {
			try {
				String token = AES256Util.aesEncode(memberId);
				resObj.put("result", 200);
				resObj.put("token", token);
				System.out.printf("계정(Id:%s)가 로그인하였습니다. 토큰이 발급되었습니다. (%s)\n", memberId, token);
				return resObj.toJSONString();
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {
				e.printStackTrace();
			}
		}
		resObj.put("result", 400);
		System.out.printf("계정(Id:%s)으로 로그인 하려는 시도가 있었습니다.\n", memberId);
		return resObj.toJSONString();
	}

}
