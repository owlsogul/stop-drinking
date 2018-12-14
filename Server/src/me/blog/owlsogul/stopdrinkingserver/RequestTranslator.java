package me.blog.owlsogul.stopdrinkingserver;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.blog.owlsogul.stopdrinkingserver.request.Request;
import me.blog.owlsogul.stopdrinkingserver.request.RequestAddFeedbacks;
import me.blog.owlsogul.stopdrinkingserver.request.RequestCheckDuplicatedMemberEmail;
import me.blog.owlsogul.stopdrinkingserver.request.RequestCheckDuplicatedMemberId;
import me.blog.owlsogul.stopdrinkingserver.request.RequestLogin;
import me.blog.owlsogul.stopdrinkingserver.request.RequestRegister;
import me.blog.owlsogul.stopdrinkingserver.request.RequestValidateToken;

public class RequestTranslator {
	
	private HashMap<String, Request> requests;
	public RequestTranslator() {
		initRequest();
	}
	
	private void initRequest() {
		requests = new HashMap<>();
		requests.put("login", new RequestLogin());
		requests.put("register", new RequestRegister());
		requests.put("check_dup_member_id", new RequestCheckDuplicatedMemberId());
		requests.put("check_dup_member_email", new RequestCheckDuplicatedMemberEmail());
		requests.put("validate_token", new RequestValidateToken());
		requests.put("add_feedbacks", new RequestAddFeedbacks());
	}
	
	public String translateRequest(Client client, String data) {
		
		JSONParser jsonParser = new JSONParser();
		String responseString = null;
		try {
			JSONObject motherObj = (JSONObject) jsonParser.parse(data);
			String command = (String) motherObj.get("command");
			if (requests.containsKey(command)) {
				JSONObject dataObj = (JSONObject) motherObj.get("data");
				if (dataObj != null) {
					System.out.println(client.toString() + "가 " + command + " 페이지에 접근합니다.");
					responseString = requests.get(command).request(command, dataObj);
					client.send(responseString);
				}
				else {
					return null;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return responseString;
		
	}

}
