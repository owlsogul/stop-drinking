package me.blog.owlsogul.stopdrinkingserver;

import me.blog.owlsogul.jttp.server.JttpServer;
import me.blog.owlsogul.jttp.server.request.IRequestController;
import me.blog.owlsogul.jttp.server.request.exception.DuplicatePageException;
import me.blog.owlsogul.stopdrinkingserver.database.DatabaseController;
import me.blog.owlsogul.stopdrinkingserver.request.*;

public class StopDrinkingServer {

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
		
		Server = new StopDrinkingServer();
		Server.startServer();
		System.out.println("소켓 서버가 열렸습니다.");
	}
	
	private static int port = 31234;
	
	private JttpServer server;
	private IRequestController reqController;
	
	public StopDrinkingServer() {
		server = new JttpServer();
	}
	
	public void startServer() {
		server.open(port);
		server.listen();
		
		reqController = server.getRequestController();
		try {
			reqController.addRequestPage("login", new RequestLogin());
			reqController.addRequestPage("register", new RequestRegister());
			reqController.addRequestPage("check_dup_member_id", new RequestCheckDuplicatedMemberId());
			reqController.addRequestPage("check_dup_member_email", new RequestCheckDuplicatedMemberEmail());
			reqController.addRequestPage("validate_token", new RequestValidateToken());
			reqController.addRequestPage("add_feedbacks", new RequestAddFeedbacks());
			reqController.addRequestPage("add_party", new RequestAddParty());
		} catch (DuplicatePageException e) {
			e.printStackTrace();
		}
			
	}
	

}
