package me.blog.owlsogul.stopdrinkingserver.request;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.JsonObject;

import me.blog.owlsogul.jttp.server.client.Client;
import me.blog.owlsogul.jttp.server.request.Request;
import me.blog.owlsogul.jttp.server.request.Response;
import me.blog.owlsogul.jttp.server.request.page.RequestPage;
import me.blog.owlsogul.stopdrinkingserver.database.DAOMember;
import me.blog.owlsogul.stopdrinkingserver.database.Member;
import me.blog.owlsogul.stopdrinkingserver.token.AES256Util;
import me.blog.owlsogul.stopdrinkingserver.token.TokenController;

public class RequestLogin extends RequestPage{
	
	@Override
	public void onLoad(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Response request(Client client, Request req) {
		JsonObject dataObj = req.getData();
		String memberId = dataObj.get("memberId").getAsString();
		String memberPw = dataObj.get("memberPw").getAsString();
		DAOMember dao = new DAOMember();
		boolean loginResult = dao.login(Member.createMember(memberId, memberPw, null));
		
		Response res = new Response(400, null);
		if (loginResult) {
			try {
				res.setResponseCode(200);
				String token = AES256Util.aesEncode(memberId);
				JsonObject resData = new JsonObject();
				resData.addProperty("token", token);
				res.setResponseData(resData);
				TokenController.getInstance().setToken(memberId, token);
				System.out.printf("계정(Id:%s)가 로그인하였습니다. 토큰이 발급되었습니다. (%s)\n", memberId, token);
				return res;
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("계정(Id:%s)으로 로그인 하려는 시도가 있었습니다.\n", memberId);
		return res;
	}

}
