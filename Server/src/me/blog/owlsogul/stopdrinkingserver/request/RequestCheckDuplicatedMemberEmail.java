package me.blog.owlsogul.stopdrinkingserver.request;

import org.json.simple.JSONObject;

import me.blog.owlsogul.stopdrinkingserver.database.DAOMember;
import me.blog.owlsogul.stopdrinkingserver.database.Member;

public class RequestCheckDuplicatedMemberEmail extends Request{

	@Override
	public String request(String command, JSONObject dataObj) {
		String memberEmail = (String) dataObj.get("memberEmail");
		DAOMember dao = new DAOMember();
		boolean chk = dao.checkDuplicatedMemberEmail(Member.createMember(null, null, memberEmail));
		
		JSONObject resObj = new JSONObject();
		if (chk) {
			resObj.put("result", 200);
		}
		else {
			resObj.put("result", 400);
		}
		return resObj.toJSONString();
	}

}
