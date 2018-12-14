package me.blog.owlsogul.stopdrinkingserver.request;

import org.json.simple.JSONObject;

import me.blog.owlsogul.stopdrinkingserver.database.DAOMember;
import me.blog.owlsogul.stopdrinkingserver.database.Member;

public class RequestRegister extends Request{

	@Override
	public String request(String command, JSONObject dataObj) {
		
		String memberId, memberPw, memberEmail;
		DAOMember dao = new DAOMember();
		Member member = null;
		
		memberId = (String) dataObj.get("memberId");
		memberPw = (String) dataObj.get("memberPw");
		memberEmail = (String) dataObj.get("memberEmail");
		member = Member.createMember(memberId, memberPw, memberEmail);
		
		int resCode = 400;
		if (dao.checkDuplicatedMemberId(member)) {
			if (dao.checkDuplicatedMemberEmail(member)) {
				resCode = dao.register(member);
			}
		}
		
		JSONObject resObj = new JSONObject();
		resObj.put("result", resCode);
		return resObj.toJSONString();
	}

}
