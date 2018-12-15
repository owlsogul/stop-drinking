package me.blog.owlsogul.stopdrinkingserver.database;

public class Feedback {
	
	private int partyId;
	private int feedbackAmountDrink;
	private int feedbackDrinkness;
	private String feedbackMemo;
	
	public static Feedback createFeedback(int partyId, int feedbackAmountDrink, int feedbackDrinkness, String feedbackMemo) {
		Feedback feedback = new Feedback();
		feedback.setPartyId(partyId);
		feedback.setFeedbackAmountDrink(feedbackAmountDrink);
		feedback.setFeedbackDrinkness(feedbackDrinkness);
		feedback.setFeedbackMemo(feedbackMemo);
		return feedback;
	}
	
	public int getPartyId() {return partyId;}
	public void setPartyId(int partyId) {this.partyId = partyId;}
	public int getFeedbackAmountDrink() {return feedbackAmountDrink;}
	public void setFeedbackAmountDrink(int feedbackAmountDrink) {this.feedbackAmountDrink = feedbackAmountDrink;}	
	public int getFeedbackDrinkness() {return feedbackDrinkness;}	
	public void setFeedbackDrinkness(int feedbackDrinkness) {this.feedbackDrinkness = feedbackDrinkness;}
	public String getFeedbackMemo() {return feedbackMemo;}
	public void setFeedbackMemo(String feedbackMemo) {this.feedbackMemo = feedbackMemo;}
	
	
	@Override
	public String toString() {
		return String.format("Feedback(%d, %d, %d)", partyId, feedbackAmountDrink, feedbackDrinkness);
	}

}
