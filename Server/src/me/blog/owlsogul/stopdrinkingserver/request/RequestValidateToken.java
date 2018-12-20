package me.blog.owlsogul.stopdrinkingserver.request;

import com.google.gson.JsonObject;

import me.blog.owlsogul.jttp.server.client.Client;
import me.blog.owlsogul.jttp.server.request.Request;
import me.blog.owlsogul.jttp.server.request.Response;
import me.blog.owlsogul.jttp.server.request.page.RequestPage;
import me.blog.owlsogul.stopdrinkingserver.token.TokenController;

public class RequestValidateToken extends RequestPage{

	@Override
	public void onLoad(String arg0) {
		
	}

	@Override
	public Response request(Client c, Request req) {
		JsonObject dataObj = req.getData();
		String token = dataObj.get("token").getAsString();
		boolean isValidToken = TokenController.getInstance().isValidToken(token);
		
		int resCode = 400;
		if (isValidToken) {
			resCode = 200;
		}
		Response res = new Response(resCode, null);
		return res;
	}

}
