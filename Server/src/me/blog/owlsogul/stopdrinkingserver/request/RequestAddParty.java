package me.blog.owlsogul.stopdrinkingserver.request;

import org.json.simple.JSONObject;

import me.blog.owlsogul.stopdrinkingserver.token.TokenController;
import me.blog.owlsogul.stopdrinkingserver.trainer.TrainController;

public class RequestAddParty extends Request{

	@Override
	public String request(String command, JSONObject dataObj) {
		
		String token = (String) dataObj.get("token");
		String date = (String) dataObj.get("date");
		String company = (String) dataObj.get("company");
		int tension = getInt(dataObj.get("tension"));
		int drinkingYesterday = getInt(dataObj.get("drinking_yesterday"));
		int sleepHour = getInt(dataObj.get("sleep_hour"));
		
		String memberId = TokenController.getInstance().getMemberId(token);
		int recommendedDrink = (int) TrainController.getInstance().useModel(memberId, tension, drinkingYesterday, sleepHour, 5);
		
		JSONObject resObj = new JSONObject();
		resObj.put("result", 200);
		resObj.put("recommended_drink", recommendedDrink);
		return resObj.toJSONString();
	}

	private int getInt(Object obj) {
		return ((Long) obj).intValue();
	}

	
}
