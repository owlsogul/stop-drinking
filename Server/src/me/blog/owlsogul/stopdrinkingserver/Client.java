package me.blog.owlsogul.stopdrinkingserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class Client implements Runnable{

	private ClientController controller;
	private Socket socket;
	private Thread thread;
	private BufferedReader br;
	private PrintWriter pw;
	public Client(ClientController controller, Socket socket) {
		this.controller = controller;
		this.setSocket(socket);
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	@Override
	public void run() {
		
		try {
			br = new BufferedReader(new InputStreamReader(getSocket().getInputStream(), "UTF-8"));
	        pw = new PrintWriter(new OutputStreamWriter(getSocket().getOutputStream(), "UTF-8"));
	        String request = br.readLine();
	        System.out.println(this.toString() + "가 보냄: " + request);
	        String response = controller.getRequestTranslator().translateRequest(this, request);
	        if (response != null) pw.println(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		controller.disconnectCallback(this);
	}
	
	public void send(String str) {
		pw.println(str);
		pw.flush();
		System.out.println(this.toString() + "로 보냄: " + str);
	}
	public String toString() {
		return socket.getInetAddress().toString();
	}
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
}
