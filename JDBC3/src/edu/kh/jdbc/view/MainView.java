package edu.kh.jdbc.view;

import java.util.InputMismatchException;
import java.util.Scanner;

import edu.kh.jdbc.member.model.vo.Member;

public class MainView {

	// 필드
	private Scanner sc = new Scanner(System.in);
	
	private MemberView memberView = new MemberView();
	private BoardView boardView = new BoardView();
	
	// 로그인된 회원 정보를 저장할 변수 선언
	public static Member loginMember = null;
	
	// static : 프로그램이 종료되기 전 까지 어디서든지 공유 가능
	
	
	public void displayMenu() {
		
		int sel = -1; // 메뉴 번호 저장용 변수
		
		do {
			
			try {
				
				if(loginMember == null  ) { // 로그인이 안되어 있을 경우
					System.out.println("------------");
					System.out.println("[메인 메뉴]");
					System.out.println("1. 로그인");
					System.out.println("2. 회원 가입");
					System.out.println("0. 종료");
					System.out.println("------------");
					System.out.println();
					
					System.out.print("메뉴 선택 >> ");
					sel = sc.nextInt();
					sc.nextLine();
					System.out.println();
					
					switch(sel) {
					case 1: memberView.login();  break;
					case 2: memberView.signUp(); break;
					
					case 0: System.out.println("프로그램을 종료합니다..."); break;
					default: System.out.println("잘못 입력 하셨습니다. 다시 입력 해주세요.");
					}
					
				
				} else { // 로그인이 되어있는 경우
					System.out.println();
					
					System.out.println("[로그인 전용 메뉴]");
					System.out.println("1. 내 정보 조회");
					System.out.println("2. 내 정보 수정");
					System.out.println("3. 회원 탈퇴");
					
					System.out.println("4. 게시글 작성");
					System.out.println("5. 게시글 수정");
					// 1) 게시글 번호 입력 받기
					// 2) 입력 받은 번호의 게시글이 현재 로그인한 회원의 게시글인지 확인
					// 3-1) 로그인한 회원의 글이 아닌 경우 -> "본인의 게시글만 수정할 수 있습니다."
					// 3-2) 로그인한 회원의 글이 맞는 경우 
					//		-> 제목, 내용을 입력 받아 update 진행
					
					System.out.println("6. 게시글 삭제");
					
					System.out.println("7. 게시글 목록 조회");
					System.out.println("8. 게시글 상세");
					System.out.println("9. 게시글 검색(제목, 내용, 작성자)");
					
					System.out.println("10. 로그아웃");
					
					System.out.print("메뉴 선택 >>");
					sel = sc.nextInt();
					sc.nextLine();
					
					System.out.println();
					
					switch(sel) {
					case 1 : memberView.myInfo(); break; // 1. 내 정보 조회
					case 2 : memberView.updateMember(); break; // 2.내 정보 수정(비밀번호, 전화번호)
					case 3 : memberView.deleteMember(); break; // 3.회원 탈퇴
					
					case 4 : boardView.insertBoard(); break; // 4. 게시글 작성
					case 5 : boardView.updateBoard(); break; // 5. 게시글 수정
					case 6 : boardView.deleteBoard(); break; // 6. 게시글 삭제
					case 7 : break;
					
					case 8 : boardView.selectBoard();  break; // 8. 게시글 상세 조회
					
					case 9 : break;
					
					case 10 :   
						System.out.println("로그아웃 되었습니다...");
						loginMember = null; // 로그인 정보가 담긴 객체를 참조하지 않음.
						break;
						
					default : System.out.println("잘못 입력 하셨습니다. 다시 입력 해주세요.");
					}
					
					
				}
			
				
			}catch (InputMismatchException e) {
				System.out.println("메뉴에 있는 숫자만 입력해주세요.");
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못 입력된 값 제거
			}
			
		}while(sel != 0);
		
	}
	
	
}
