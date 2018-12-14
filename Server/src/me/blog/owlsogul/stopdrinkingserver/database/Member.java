package me.blog.owlsogul.stopdrinkingserver.database;

public class Member {

	private String memberId;
	private String memberPw;
	private String memberEmail;
	
	public static Member createMember(String memberId, String memberPw, String memberEmail) {
		Member member = new Member();
		member.setMemberId(memberId);
		member.setMemberPw(memberPw);
		member.setMemberEmail(memberEmail);
		return member;
	}
	
	public String getMemberId() {return memberId;}
	public void setMemberId(String memberId) {this.memberId = memberId;}
	public String getMemberPw() {return memberPw;}
	public void setMemberPw(String memberPw) {this.memberPw = memberPw;}
	public String getMemberEmail() {return memberEmail;}
	public void setMemberEmail(String memberEmail) {this.memberEmail = memberEmail;}
	
}
