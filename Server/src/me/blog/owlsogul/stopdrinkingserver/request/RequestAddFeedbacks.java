package me.blog.owlsogul.stopdrinkingserver.request;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.blog.owlsogul.jttp.server.client.Client;
import me.blog.owlsogul.jttp.server.request.Request;
import me.blog.owlsogul.jttp.server.request.Response;
import me.blog.owlsogul.jttp.server.request.page.RequestPage;
import me.blog.owlsogul.stopdrinkingserver.database.DAOFeedback;
import me.blog.owlsogul.stopdrinkingserver.database.DAOParty;
import me.blog.owlsogul.stopdrinkingserver.database.Feedback;
import me.blog.owlsogul.stopdrinkingserver.database.Party;
import me.blog.owlsogul.stopdrinkingserver.token.TokenController;
import me.blog.owlsogul.stopdrinkingserver.trainer.TrainController;

public class RequestAddFeedbacks extends RequestPage{
	
	@Override
	public void onLoad(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Response request(Client client, Request request) {

		DAOParty daoParty = new DAOParty();
		DAOFeedback daoFeedback = new DAOFeedback();
		Response response = new Response(300, null);
		String token = request.getData().get("token").getAsString();
		if (!TokenController.getInstance().isValidToken(token)) { // 토큰 에러
			return response;
		}
		String memberId = TokenController.getInstance().getMemberId(token);
		JsonArray feedbackArr = request.getData().get("feedback").getAsJsonArray();
		for (int idx = 0; idx < feedbackArr.size(); idx++) {
			JsonObject feedbackObj = (JsonObject) feedbackArr.get(idx);
			int sleepHour = feedbackObj.get("sleep_hour").getAsInt();
			int tension = feedbackObj.get("tension").getAsInt();
			int drinkingYesterday = feedbackObj.get("drinking_yesterday").getAsInt();
			int amountDrink = feedbackObj.get("amount_drink").getAsInt();
			int drinkness = feedbackObj.get("drinkness").getAsInt();
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
		TrainController trainer = TrainController.getInstance();
		trainer.trainModel(memberId);
		response.setResponseCode(200);
		return response;
	}

}
