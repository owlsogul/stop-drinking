package me.blog.owlsogul.stopdrinkingserver.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseController {

	// configuration
	private String dbFileName;
		
	private Connection conn;
	private boolean isOpen;
	
	public DatabaseController(String dbFileName) {
		this.setDBFileName(dbFileName);
		this.isOpen = false;
	}
	
	private boolean firstCreation() {
		boolean isappliedSQL = false;
		try {
			// sql file load
			String sql = "";
			File sqlFile = new File("./sql/Sqlite_CREATE_TABLE.sql");
			FileReader fileReader = new FileReader(sqlFile);
			BufferedReader bufReader = new BufferedReader(fileReader);
			StringBuilder fileBuf = new StringBuilder();
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				fileBuf.append(line);
			}
			bufReader.close();
			sql = fileBuf.toString();
			System.out.println("SQL 문을 읽어옵니다. => \n" + sql);
			System.out.println("SQL 문 로드 완료.");
			// sql
			Statement state = conn.createStatement();
			int returnVal = state.executeUpdate(sql);
			if (returnVal == 0) {
				System.out.println("SQL 문 적용을 완료 하였습니다.");
				isappliedSQL = true;
			}
			else {
				System.out.println("SQL 적용을 실패하였습니다.");
			}
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isappliedSQL;
	}
	
	public boolean openDatabase() {
		try {
			Class.forName("org.sqlite.JDBC");
			File dbFile = new File(dbFileName);
			if (!dbFile.exists()) {
				System.out.println("sqlite 데이터베이스 파일이 존재하지 않습니다. 파일을 생성합니다. " + dbFileName);
				dbFile.createNewFile();
				conn = DriverManager.getConnection("jdbc:sqlite:" + this.getDBFileName());
				if (!firstCreation()) {
					return false;
				}
			}
			else {
				conn = DriverManager.getConnection("jdbc:sqlite:" + this.getDBFileName());
				System.out.println("sqlite 데이터베이스 파일을 불러옵니다. " + dbFileName);
			}
			isOpen = true;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean closeDatabase() {
		if (isOpen) {
			try {
				conn.close();
				conn = null;
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public Connection getDatabaseConnection() {
		return conn;
	}

	public String getDBFileName() {return dbFileName;}
	public void setDBFileName(String dbFileName) {this.dbFileName = dbFileName;}
	
}
