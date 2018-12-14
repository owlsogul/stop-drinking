package me.blog.owlsogul.stopdrinkingserver.database;

public class Party {

	private int partyId;
	private int partyTension;
	private int partyDrinkingYesterday;
	private int partySleepHour;
	
	private String partyHolder;
	private String partyTitle;
	private String partyDate;
	private String partyCompany;
	
	public static Party createParty(int partyId, int partyTension, int partyDrinkingYesterday, int partySleepHour, String partyHolder, String partyTitle, String partyDate, String partyCompany) {
		Party party = new Party();
		party.setPartyId(partyId);
		party.setPartyTension(partyTension);
		party.setPartyDrinkingYesterday(partyDrinkingYesterday);
		party.setPartySleepHour(partySleepHour);
		party.setPartyHolder(partyHolder);
		party.setPartyTitle(partyTitle);
		party.setPartyDate(partyDate);
		party.setPartyCompany(partyCompany);
		return party;
	}
	
	public int getPartyId() {return partyId;}
	public void setPartyId(int partyId) {this.partyId = partyId;}
	public int getPartyTension() {return partyTension;}
	public void setPartyTension(int partyTension) {this.partyTension = partyTension;}
	public int getPartyDrinkingYesterday() {return partyDrinkingYesterday;}
	public void setPartyDrinkingYesterday(int partyDrinkingYesterday) {this.partyDrinkingYesterday = partyDrinkingYesterday;}
	public int getPartySleepHour() {return partySleepHour;}
	public void setPartySleepHour(int partySleepHour) {this.partySleepHour = partySleepHour;}
	public String getPartyHolder() {return partyHolder;}
	public void setPartyHolder(String partyHolder) {this.partyHolder = partyHolder;}
	public String getPartyTitle() {return partyTitle;}
	public void setPartyTitle(String partyTitle) {this.partyTitle = partyTitle;}
	public String getPartyDate() {return partyDate;}
	public void setPartyDate(String partyDate) {this.partyDate = partyDate;}
	public String getPartyCompany() {return partyCompany;}
	public void setPartyCompany(String partyCompany) {this.partyCompany = partyCompany;}
	
}
