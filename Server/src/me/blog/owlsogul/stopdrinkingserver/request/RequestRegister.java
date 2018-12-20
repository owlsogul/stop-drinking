package me.blog.owlsogul.stopdrinkingserver.request;

import com.google.gson.JsonObject;

import me.blog.owlsogul.jttp.server.client.Client;
import me.blog.owlsogul.jttp.server.request.Request;
import me.blog.owlsogul.jttp.server.request.Response;
import me.blog.owlsogul.jttp.server.request.page.RequestPage;
import me.blog.owlsogul.stopdrinkingserver.database.DAOMember;
import me.blog.owlsogul.stopdrinkingserver.database.Member;

public class RequestRegister extends RequestPage{

	@Override
	public void onLoad(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Response request(Client c, Request req) {
		JsonObject dataObj = req.getData();
		String memberId, memberPw, memberEmail;
		DAOMember dao = new DAOMember();
		Member member = null;
		
		memberId = dataObj.get("memberId").getAsString();
		memberPw = dataObj.get("memberPw").getAsString();
		memberEmail = dataObj.get("memberEmail").getAsString();
		member = Member.createMember(memberId, memberPw, memberEmail);
		
		int resCode = 400;
		if (dao.checkDuplicatedMemberId(member)) {
			if (dao.checkDuplicatedMemberEmail(member)) {
				resCode = dao.register(member);
			}
		}
		
		Response res = new Response(resCode, null);
		return res;
	}

}
