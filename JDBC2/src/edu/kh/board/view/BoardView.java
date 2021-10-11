package edu.kh.board.view;

import java.util.InputMismatchException;
import java.util.Scanner;

import edu.kh.board.model.service.BoardService;
import edu.kh.board.model.vo.Board;

public class BoardView { 

	// 필드 
	private BoardService service = new BoardService();
	private Scanner sc = new Scanner(System.in);
	
	
	// 메소드
	public void displayMenu() {
		
		int sel = -1; // 입력된 메뉴 번호 저장용 변수
		
		do {
			try {
				
				System.out.println("====================");
				System.out.println("1. 게시글 작성");
				System.out.println("2. 게시글 수정(비밀번호X)");
				
				System.out.println("0. 프로그램 종료");
				System.out.println("====================");
				
				System.out.print("메뉴 번호 선택 >> ");
				sel = sc.nextInt();
				sc.nextLine(); // 개행 문자 제거
				
				System.out.println(); // 줄바꿈
				
				
				switch (sel) {
				case 1: insertBoard(); break; // 1. 게시글 작성
				
				case 2: updateBoardPwx(); break; // 2. 게시글 수정(비밀번호X) 
				
				case 0: System.out.println("프로그램을 종료합니다 ..."); break;
				
				default: System.out.println("메뉴에 있는 번호만 입력해주세요.");
				}
				
				
			}catch (InputMismatchException e) {
				System.out.println("메뉴에 있는 번호만 입력해주세요.");
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못 입력한 값 제거
			}
			
		}while(sel != 0);
	
	
	} // displayMenu() 끝
	
	
	// 1. 게시글 삽입
	private void insertBoard() {
		
		System.out.println("*** 게시글 삽입 ***");
		// 작성자, 비밀번호, 제목, 내용
		
		System.out.print("작성자 입력 : ");
		String boardWriter = sc.nextLine();
		
		System.out.print("비밀번호 입력 : ");
		String boardPw = sc.nextLine();
		
		System.out.print("제목 입력 : ");
		String boardTitle = sc.nextLine();
		
		System.out.println("내용 입력 (입력 종료 시 !q 작성 후 엔터)");
		String boardContent = ""; // 빈 문자열
		
		while(true) {
			// 입력
			String temp = sc.nextLine();
			
			// 입력 받은 값이 !q인지 검사
			if( temp.equals("!q")  ) {
				break;
			}else {
				boardContent += temp + "\n"; 
			}
		}
		
		// 입력 받은 값을 Board 객체에 저장
		Board board = new Board(boardTitle, boardContent, boardWriter, boardPw);
		
		// 게시글 삽입 Service 호출 및 결과 값(성공한 행의 개수) 반환
		try {
			int result = service.insertBoard(board);
			
			// 단순 1행 insert 시 result가 0이 나오는 경우는 없지만
			// 여러 행 또는 서브쿼리 insert 시 0이 나올 가능성이 있다.
			if(result > 0) {
				System.out.println("게시글 삽입 성공");
			}else {
				System.out.println("게시글 삽입 실패");
			}
			
		} catch (Exception e) {
			
			System.out.println("게시글 삽입 과정에서 문제가 발생했습니다.");
			e.printStackTrace();
		}
	}
	
	
	// 2. 게시글 수정 (비밀번호 x)
	public void updateBoardPwx() {
		
		// 게시글 번호, 제목, 내용을 입력 받아
		// 번호가 일치하는 게시글의 제목, 내용 수정
		
		System.out.println("*** 게시글 수정 (비밀번호 x) ***");
		
		System.out.print("게시글 번호 입력 : ");
		int boardNo = sc.nextInt();
		sc.nextLine();
		
		System.out.print("수정할 제목 입력 : ");
		String boardTitle = sc.nextLine();
		
		System.out.println("수정할 내용 입력 (입력 종료 시 !q 작성 후 엔터)");
		String boardContent = "";
		
		while(true) {
			String temp = sc.nextLine(); 
			
			if( temp.equals("!q") ) { // 입력된 문자열이 "!q"인 경우
				break;
			
			} else {
				boardContent += temp + "\n";
			}
		}
		
		// 입력 받은 값을 Board 객체에 저장
		Board board = new Board(boardNo, boardTitle, boardContent);
		
		
		// 게시글 수정 Service 호출 후 결과를 반환 받아 저장
		try {
			int result = service.updateBoardPwx(board);
			
			if(result > 0) {
				System.out.println("게시글 수정 성공");
				
			}else {
				System.out.println("입력한 번호의 게시글 존재하지 않습니다.");
			}
			
			
		} catch (Exception e) {
			System.out.println("게시글 수정 과정에서 문제가 발생했습니다.");
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
