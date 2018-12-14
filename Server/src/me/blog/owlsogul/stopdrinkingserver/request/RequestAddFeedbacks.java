package me.blog.owlsogul.stopdrinkingserver.request;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import me.blog.owlsogul.stopdrinkingserver.database.DAOFeedback;
import me.blog.owlsogul.stopdrinkingserver.database.DAOParty;
import me.blog.owlsogul.stopdrinkingserver.database.Feedback;
import me.blog.owlsogul.stopdrinkingserver.database.Party;
import me.blog.owlsogul.stopdrinkingserver.token.TokenController;

public class RequestAddFeedbacks extends Request{

	@Override
	public String request(String command, JSONObject dataObj) {
		
		DAOParty daoParty = new DAOParty();
		DAOFeedback daoFeedback = new DAOFeedback();
		
		String token = (String) dataObj.get("token");
		if (!TokenController.getInstance().isValidToken(token)) { // 토큰 에러
			return "{\"result\":300}";
		}
		String memberId = TokenController.getInstance().getMemberId(token);
		JSONArray feedbackArr = (JSONArray) dataObj.get("feedback");
		for (int idx = 0; idx < feedbackArr.size(); idx++) {
			JSONObject feedbackObj = (JSONObject) feedbackArr.get(idx);
			int sleepHour = getInt(feedbackObj.get("sleep_hour"));
			int tension = getInt(feedbackObj.get("tension"));
			int drinkingYesterday = getInt(feedbackObj.get("drinking_yesterday"));
			int amountDrink = getInt(feedbackObj.get("amount_drink"));
			int drinkness = getInt(feedbackObj.get("drinkness"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
			java.util.Date currentDate = new java.util.Date();
			String sqlDateString = format.format(currentDate);
			Party party = Party.createParty(-1, tension, drinkingYesterday, sleepHour, memberId, String.format("#train-%d", (int)(Math.random()*Integer.MAX_VALUE)), Date.valueOf(sqlDateString), null);
			daoParty.addParty(party);
			int partyId = daoParty.getPartyId(party);
			if (partyId != -1) {
				party.setPartyId(partyId);
				Feedback feedback = Feedback.createFeedback(partyId, amountDrink, drinkness, null);
				daoFeedback.addFeedback(feedback);
				System.out.printf("추가 완료::\n - %s\n - %s\n", party.toString(), feedback.toString());
			}
			else {
				System.out.printf("%s 추가에 실패하였습니다.\n", party.toString());
			}
		}
		JSONObject resObj = new JSONObject();
		resObj.put("result", 200);
		return resObj.toJSONString();
	}
	
	private int getInt(Object obj) {
		return ((Long) obj).intValue();
	}

}
