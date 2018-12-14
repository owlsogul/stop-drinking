package me.blog.owlsogul.stopdrinkingserver;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ClientController {
	
	private List<Client> clients;
	private RequestTranslator requestTranslator;
	public ClientController() {
		clients = new LinkedList<>();
		setRequestTranslator(new RequestTranslator());
	}
	
	public void registerClient(Socket socket) {
		Client client = new Client(this, socket);
		clients.add(client);
	}
	
	public void disconnectCallback(Client client) {
		clients.remove(client);
		System.out.println(client.toString() + "가 접속을 종료했습니다.");
	}

	public RequestTranslator getRequestTranslator() {
		return requestTranslator;
	}

	public void setRequestTranslator(RequestTranslator requestTranslator) {
		this.requestTranslator = requestTranslator;
	}
	

}
