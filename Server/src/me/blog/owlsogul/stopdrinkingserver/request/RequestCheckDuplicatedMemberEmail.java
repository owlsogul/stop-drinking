package me.blog.owlsogul.stopdrinkingserver.request;

import com.google.gson.JsonObject;

import me.blog.owlsogul.jttp.server.client.Client;
import me.blog.owlsogul.jttp.server.request.Request;
import me.blog.owlsogul.jttp.server.request.Response;
import me.blog.owlsogul.jttp.server.request.page.RequestPage;
import me.blog.owlsogul.stopdrinkingserver.database.DAOMember;
import me.blog.owlsogul.stopdrinkingserver.database.Member;

public class RequestCheckDuplicatedMemberEmail extends RequestPage{

	@Override
	public void onLoad(String arg0) {
		
	}

	@Override
	public Response request(Client client, Request req) {
		JsonObject dataObj = req.getData();
		String memberEmail = (String) dataObj.get("memberEmail").getAsString();
		DAOMember dao = new DAOMember();
		boolean chk = dao.checkDuplicatedMemberEmail(Member.createMember(null, null, memberEmail));
		
		int resCode = 400;
		if (chk) {
			resCode = 200;
		}
		return new Response(resCode, null);
	}

}
