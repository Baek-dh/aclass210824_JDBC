package edu.kh.jdbc.view;

import java.util.Scanner;

import edu.kh.jdbc.member.model.service.MemberService;
import edu.kh.jdbc.member.model.vo.Member;

public class MemberView {

	// 필드
	private Scanner sc = new Scanner(System.in);
	
	private MemberService service = new MemberService();
	
	
	// 메인 메뉴 1. 로그인
	public void login() {
	
		System.out.println("[로그인]");
		
		System.out.print("아이디 : ");
		String memberId = sc.nextLine();
		
		System.out.print("비밀번호 : ");
		String memberPw = sc.nextLine();
		
		try {
			// 로그인 Service 호출 후 결과 반환 받기
			Member member = service.login(memberId, memberPw);
			
			if(member == null) { // 조회 결과 없음 == 로그인 실패
				System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
			
				
			}else { // 조회 결과 있음 == 로그인 성공
				System.out.println(member.getMemberNm() + "님 환영합니다."); 
				
				// static 변수로 선언된 loginMember에 로그인된 정보를 저장
				MainView.loginMember = member;
			}
			
			
			
		} catch (Exception e) {
			System.out.println("로그인 과정에서 문제가 발생했습니다.");
			e.printStackTrace();
		}
	}
	
	
	
	// 메인 메뉴 2. 회원 가입
	public void signUp() {
		System.out.println("[회원 가입]");
		
		
		try {
			
			String memberId = null;
			
			while(true) {
			
				// 1. 아이디 중복 검사
				// -> 아이디를 입력 받으면 바로 중복 검사 진행
				System.out.print("아이디 : ");
				memberId = sc.nextLine();
				
				// 아이디 중복 검사 Service 호출 후 결과 반환 받기
				int result = service.idDupCheck(memberId);
				
				//  1) 중복 O : 다시 아이디 입력 받기
				if(result > 0) {
					System.out.println("이미 사용중인 아이디 입니다.");
				}else {
					System.out.println("사용 가능한 아이디 입니다.");
					break; // while문 종료
				}
			}
			
			//  2) 중복 X : 다음 회원 정보 입력 받기
			System.out.print("비밀번호 : ");
			String memberPw = sc.nextLine();
			
			System.out.print("이름 : ");
			String memberNm = sc.nextLine();
			
			System.out.print("전화번호 : ");
			String phone = sc.nextLine();
			
			
			// 2. 회원 가입 Service 호출 후 결과 반환 받기
			Member member = new Member(memberId, memberPw, memberNm, phone);
			
			// insert -> 성공한 행의 개수 -> int가 저장하기 적합한 자료형
			int result = service.signUp(member);
			
			if(result > 0) { // 회원 가입 성공
				System.out.println("회원 가입 성공!!!");
			}else {
				System.out.println("회원 가입 실패...");
			}
			
			
		} catch (Exception e) {
			System.out.println("회원가입 중 문제가 발생했습니다.");
			e.printStackTrace();
		}
		
		
		
		
	}

	

	// 로그인 메뉴 1. 내 정보 조회
	public void myInfo() {
		
		// 로그인된 회원 정보를 저장하고있는 static 변수 
		// MainView.loginMember
		System.out.println("[내 정보 조회]");
		
		System.out.println("아이디 : " + MainView.loginMember.getMemberId());
		
		System.out.println("이름 : " + MainView.loginMember.getMemberNm());
		
		System.out.println("전화번호 : " + MainView.loginMember.getPhone());
		
	}


	// 로그인 메뉴 2. 내 정보 수정
	public void updateMember() {
		
		System.out.println("[내 정보 수정]");
		System.out.print("수정할 비밀번호 입력 : ");
		String memberPw = sc.nextLine();
		
		System.out.print("수정할 전화번호 입력 : ");
		String phone = sc.nextLine();
		
		try {
			// 내 정보 수정 Service 호출 후 결과 반환 받기
			int result = service.updateMember(memberPw, phone);
			
			if(result > 0) {
				System.out.println("회원 정보가 수정되었습니다.");
			}else {
				System.out.println("내 정보 수정 실패");
			}
			
		}catch (Exception e) {
			System.out.println("내 정보 수정 중 문제가 발생했습니다.");
			e.printStackTrace();
		}
		
	}


	// 로그인 메뉴 3. 회원 탈퇴
	public void deleteMember() {
		
		System.out.println("[회원 탈퇴]");
		
		// 1. 비밀번호 입력 받기
		System.out.print("비밀번호 입력 : ");
		String memberPw = sc.nextLine();
		
		while(true) {
			// 2. 정말로 탈퇴 하시겠습니까? y/n
			System.out.print("정말로 탈퇴 하시겠습니까?(y/n) : ");
			char ch = sc.nextLine().toLowerCase().charAt(0);
			// String.toLowerCase() : 문자열을 모두 소문자로 변경
			// String.toUpperCase() : 문자열을 모두 대문자로 변경
			
			if(ch == 'y') {
				// 탈퇴 Service 호출 후 결과 반환 받기
				try {
					int result = service.deleteMember(memberPw);
					
					if(result > 0) {
						System.out.println("탈퇴 되었습니다.");
						MainView.loginMember = null; // 로그아웃
					
					} else {
						System.out.println("비밀번호 일치하지 않습니다.");
					}
					
				}catch (Exception e) {
					System.out.println("회원 탈퇴 중 문제가 발생했습니다.");
					e.printStackTrace();
				}
				
				
				break;
			} else if(ch == 'n') {
				System.out.println("취소 되었습니다.");
			
				break;
			} else {
				System.out.println("잘못 입력 하셨습니다.");
			}
		}
		
	}
	
	
	
	
	
}
