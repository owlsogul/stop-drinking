package me.blog.owlsogul.stopdrinkingserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import me.blog.owlsogul.stopdrinkingserver.database.DatabaseController;

public class StopDrinkingServer implements Runnable{

	public static StopDrinkingServer Server;
	public static DatabaseController Database;
	
	public static void main(String[] args) {
		
		Database = new DatabaseController("./database.sqlite3");
		if (Database.openDatabase()) {
			System.out.println("데이터 베이스 연결 완료");
		}
		else {
			System.out.println("데이터 베이스 연결 실패. 서버를 종료합니다.");
			return;
		}
		
		try {
			Server = new StopDrinkingServer();
			Server.startServer();
			System.out.println("소켓 서버가 열렸습니다.");
		} catch (IOException e) {
			System.out.println("소켓 서버 열기 실패 서버를 종료합니다.");
			e.printStackTrace();
			return;
		}
	}
	
	private static int port = 31234;
	private static int CONNECTION_TIME = 3000;
	private ServerSocket socketServer;
	private ClientController clientController;
	private Thread thread;
	
	public StopDrinkingServer() throws IOException {
		socketServer = new ServerSocket(port);
		clientController = new ClientController();
		thread = new Thread(this);
	}
	
	public void startServer() {
		this.thread.start();
	}
	
	private void listen() {
		while (true) {
			try {
				Socket socket = socketServer.accept();
				socket.setSoTimeout(CONNECTION_TIME);
				clientController.registerClient(socket);
				System.out.println(socket.getInetAddress().getHostAddress() + "가 접속했습니다.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		listen();
	}

}
