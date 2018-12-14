package me.blog.owlsogul.stopdrinkingserver.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.blog.owlsogul.stopdrinkingserver.StopDrinkingServer;

public class DAOMember {
	
	/**
	 * 
	 * @param member
	 * @return 이미 존재하는 이메일일 경우 false, 아닐 경우 true
	 */
	public boolean checkDuplicatedMemberEmail(Member member) {
		
		DatabaseController db = StopDrinkingServer.Database;
		PreparedStatement prep = null;
		ResultSet row = null;
		boolean chk = false;
		String sql = "SELECT memberEmail FROM Member WHERE memberEmail = ?";
		
		try {
			prep = db.getDatabaseConnection().prepareStatement(sql);
			prep.setString(1, member.getMemberEmail());
			row = prep.executeQuery();
			if (!row.next()) {
				chk = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		closeResultSet(row);
		closePreparedStatement(prep);
		return chk;
		
	}
	
	/**
	 * 
	 * @param member
	 * @return 이미 존재하는 아이디일 경우 false, 아닐 경우 true
	 */
	public boolean checkDuplicatedMemberId(Member member) {
		
		DatabaseController db = StopDrinkingServer.Database;
		PreparedStatement prep = null;
		ResultSet row = null;
		boolean chk = false;
		String sql = "SELECT memberId FROM Member WHERE memberId = ?";
		
		try {
			prep = db.getDatabaseConnection().prepareStatement(sql);
			prep.setString(1, member.getMemberId());
			row = prep.executeQuery();
			if (!row.next()) {
				chk = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		closeResultSet(row);
		closePreparedStatement(prep);
		return chk;
		
	}
	
	public int register(Member member) {
		DatabaseController db = StopDrinkingServer.Database;
		PreparedStatement prep = null;
		int chk = 400;
		String sql = "INSERT INTO Member VALUES(?, ?, ?)";
		try {
			prep = db.getDatabaseConnection().prepareStatement(sql);
			prep.setString(1, member.getMemberId());
			prep.setString(2, member.getMemberPw());
			prep.setString(3, member.getMemberEmail());
			prep.executeUpdate();
			chk = 200;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		closePreparedStatement(prep);
		return chk;
	}
	
	public boolean login(Member member) {
		
		DatabaseController db = StopDrinkingServer.Database;
		PreparedStatement prep = null;
		ResultSet row = null;
		boolean chk = false;
		String sql = "SELECT memberId FROM Member WHERE memberId = ? AND memberPw = ?";
		
		try {
			prep = db.getDatabaseConnection().prepareStatement(sql);
			prep.setString(1, member.getMemberId());
			prep.setString(2, member.getMemberPw());
			row = prep.executeQuery();
			if (row.next()) {
				chk = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		closeResultSet(row);
		closePreparedStatement(prep);
		return chk;
	}
	
	private void closeResultSet(ResultSet row) {
		if (row != null) {
			try {
				row.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void closePreparedStatement(PreparedStatement prep) {
		if (prep != null) {
			try {
				prep.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
