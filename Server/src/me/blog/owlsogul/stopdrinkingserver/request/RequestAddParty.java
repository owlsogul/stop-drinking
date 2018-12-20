package me.blog.owlsogul.stopdrinkingserver.request;

import com.google.gson.JsonObject;

import me.blog.owlsogul.jttp.server.client.Client;
import me.blog.owlsogul.jttp.server.request.Request;
import me.blog.owlsogul.jttp.server.request.Response;
import me.blog.owlsogul.jttp.server.request.page.RequestPage;
import me.blog.owlsogul.stopdrinkingserver.token.TokenController;
import me.blog.owlsogul.stopdrinkingserver.trainer.TrainController;

public class RequestAddParty extends RequestPage{

	@Override
	public void onLoad(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Response request(Client client, Request request) {
		JsonObject dataObj = request.getData();
		Response response = new Response(200, new JsonObject());
		String token = dataObj.get("token").getAsString();
		String date =  dataObj.get("date").getAsString();
		String company =  dataObj.get("company").getAsString();
		int tension = dataObj.get("tension").getAsInt();
		int drinkingYesterday = dataObj.get("drinking_yesterday").getAsInt();
		int sleepHour = dataObj.get("sleep_hour").getAsInt();
		
		String memberId = TokenController.getInstance().getMemberId(token);
		int recommendedDrink = (int) TrainController.getInstance().useModel(memberId, tension, drinkingYesterday, sleepHour, 5);
		
		response.getResponseData().addProperty("recommended_drink", recommendedDrink);
		return response;
	}

	
}
