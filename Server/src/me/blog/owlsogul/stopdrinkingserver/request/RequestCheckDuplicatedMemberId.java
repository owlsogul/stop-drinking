package me.blog.owlsogul.stopdrinkingserver.request;

import com.google.gson.JsonObject;

import me.blog.owlsogul.jttp.server.client.Client;
import me.blog.owlsogul.jttp.server.request.Request;
import me.blog.owlsogul.jttp.server.request.Response;
import me.blog.owlsogul.jttp.server.request.page.RequestPage;
import me.blog.owlsogul.stopdrinkingserver.database.DAOMember;
import me.blog.owlsogul.stopdrinkingserver.database.Member;

public class RequestCheckDuplicatedMemberId extends RequestPage{

	@Override
	public void onLoad(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Response request(Client client, Request req) {
		JsonObject dataObj = req.getData();
		String memberId = (String) dataObj.get("memberId").getAsString();
		DAOMember dao = new DAOMember();
		boolean chk = dao.checkDuplicatedMemberId(Member.createMember(null, memberId, null));
		
		int resCode = 400;
		if (chk) {
			resCode = 200;
		}
		return new Response(resCode, null);
	}

}
