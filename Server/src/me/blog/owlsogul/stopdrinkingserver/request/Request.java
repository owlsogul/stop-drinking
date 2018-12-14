package me.blog.owlsogul.stopdrinkingserver.request;

import org.json.simple.JSONObject;

public abstract class Request {

	public abstract String request(String command, JSONObject dataObj);
	
}
