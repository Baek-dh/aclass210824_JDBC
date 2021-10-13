package edu.kh.jdbc.member.model.vo;

public class Member {

	private int memberNo;	 // 회원번호
	private String memberId; // 아이디(이메일 형식)
	private String memberPw; // 비밀번호
	private String memberNm; // 이름
	private String phone;    // 전화번호
	
	// 기본 생성자
	public Member() { }

	// alt + shift + s -> o : 생성자 자동완성
	// 로그인용 생성자
	public Member(int memberNo, String memberId, String memberNm, String phone) {
		super();
		this.memberNo = memberNo;
		this.memberId = memberId;
		this.memberNm = memberNm;
		this.phone = phone;
	}

	// 회원가입용 생성자
	public Member(String memberId, String memberPw, String memberNm, String phone) {
		super();
		this.memberId = memberId;
		this.memberPw = memberPw;
		this.memberNm = memberNm;
		this.phone = phone;
	}
	

	// getter / setter
	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberPw() {
		return memberPw;
	}

	public void setMemberPw(String memberPw) {
		this.memberPw = memberPw;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	// alt + shift + s -> s  : toString() 오버라이딩 자동 완성
	@Override
	public String toString() {
		return "Member [memberNo=" + memberNo + ", memberId=" + memberId + ", memberPw=" + memberPw + ", memberNm="
				+ memberNm + ", phone=" + phone + "]";
	}
	
	
	
	
	
}
