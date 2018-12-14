package me.blog.owlsogul.stopdrinkingserver.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.blog.owlsogul.stopdrinkingserver.StopDrinkingServer;

public class DAOFeedback {
	
	
	public int addFeedback(Feedback feedback) {
		DatabaseController db = StopDrinkingServer.Database;
		PreparedStatement prep = null;
		int chk = 400;
		String sql = "INSERT INTO Feedback VALUES(?, ?, ?, ?)";
		try {
			prep = db.getDatabaseConnection().prepareStatement(sql);
			prep.setInt(1, feedback.getPartyId());
			prep.setInt(2, feedback.getFeedbackAmountDrink());
			prep.setInt(3, feedback.getFeedbackDrinkness());
			prep.setString(4, feedback.getFeedbackMemo());
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
