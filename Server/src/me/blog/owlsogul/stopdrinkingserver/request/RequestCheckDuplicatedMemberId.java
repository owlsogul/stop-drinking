package me.blog.owlsogul.stopdrinkingserver.request;

import org.json.simple.JSONObject;

import me.blog.owlsogul.stopdrinkingserver.database.DAOMember;
import me.blog.owlsogul.stopdrinkingserver.database.Member;

public class RequestCheckDuplicatedMemberId extends Request{

	@Override
	public String request(String command, JSONObject dataObj) {
		String memberId = (String) dataObj.get("memberId");
		DAOMember dao = new DAOMember();
		boolean chk = dao.checkDuplicatedMemberEmail(Member.createMember(memberId, null, null));
		
		JSONObject resObj = new JSONObject();
		if (chk) {
			resObj.put("result", 200);
		}
		else {
			resObj.put("result", 500);
		}
		return resObj.toJSONString();
	}

}
