package me.blog.owlsogul.stopdrinkingserver.request;

import org.json.simple.JSONObject;

import me.blog.owlsogul.stopdrinkingserver.token.TokenController;

public class RequestValidateToken extends Request{

	@Override
	public String request(String command, JSONObject dataObj) {
		String token = (String) dataObj.get("token");
		boolean isValidToken = TokenController.getInstance().isValidToken(token);
		if (isValidToken) {
			JSONObject resObj = new JSONObject();
			resObj.put("result", 200);
			return resObj.toJSONString();
		}
		JSONObject resObj = new JSONObject();
		resObj.put("result", 400);
		return resObj.toJSONString();
	}

}
