package me.blog.owlsogul.stopdrinkingserver.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.blog.owlsogul.stopdrinkingserver.StopDrinkingServer;

public class DAOParty {
	
	public int getPartyId(Party party) {
		DatabaseController db = StopDrinkingServer.Database;
		PreparedStatement prep = null;
		ResultSet row = null;
		int chk = -1;
		String sql = "SELECT partyId FROM Party WHERE partyHolder = ? AND partyTitle = ? AND partyDate = ?";
		
		try {
			prep = db.getDatabaseConnection().prepareStatement(sql);
			prep.setString(1, party.getPartyHolder());
			prep.setString(2, party.getPartyTitle());
			prep.setDate(3, (java.sql.Date)party.getPartyDate());
			row = prep.executeQuery();
			if (row.next()) {
				chk = row.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		closeResultSet(row);
		closePreparedStatement(prep);
		return chk;
	}
	
	public int addParty(Party party) {
		DatabaseController db = StopDrinkingServer.Database;
		PreparedStatement prep = null;
		int chk = 400;
		String sql = "INSERT INTO Party (partyTension, partyDrinkingYesterday, partySleepHour, partyHolder, partyTitle, partyDate, partyCompany) VALUES(?, ?, ?, ?, ?, ?, ?)";
		try {
			prep = db.getDatabaseConnection().prepareStatement(sql);
			prep.setInt(1, party.getPartyTension());
			prep.setInt(2, party.getPartyDrinkingYesterday());
			prep.setInt(3, party.getPartySleepHour());
			prep.setString(4, party.getPartyHolder());
			prep.setString(5, party.getPartyTitle());
			prep.setDate(6, (java.sql.Date)party.getPartyDate());
			prep.setString(7, party.getPartyCompany());
			
			chk = prep.executeUpdate();
			chk = 200;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
