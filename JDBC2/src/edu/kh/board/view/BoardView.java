package edu.kh.board.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.board.model.service.BoardService;
import edu.kh.board.model.vo.Board;

public class BoardView { 

	// 필드 
	private BoardService service = new BoardService();
	private Scanner sc = new Scanner(System.in);
	
	
	// 메소드
	public void displayMenu() {
		
		int sel = -1; // 입력된 메뉴 번호 저장b용 변수
		
		do {
			try {
				
				System.out.println("====================");
				System.out.println("1. 게시글 작성");
				System.out.println("2. 게시글 수정(비밀번호X)");
				System.out.println("3. 게시글 수정");
				System.out.println("4. 게시글 삭제");
				System.out.println("5. 게시글 목록 조회");
				System.out.println("6. 게시글 제목 검색");
				
				System.out.println("7. 게시글 상세 조회"); // 상세 조회 시 조회수 증가
				
				System.out.println("0. 프로그램 종료");
				System.out.println("====================");
				
				System.out.print("메뉴 번호 선택 >> ");
				sel = sc.nextInt();
				sc.nextLine(); // 개행 문자 제거
				
				System.out.println(); // 줄바꿈
				
				
				switch (sel) {
				case 1: insertBoard(); break; 		// 1. 게시글 작성
				case 2: updateBoardPwx(); break; 	// 2. 게시글 수정(비밀번호X) 
				case 3: updateBoard(); break; 		// 3. 게시글 수정
				case 4: deleteBoard(); break; 		// 4. 게시글 삭제
				case 5: selectAll(); break;			// 5. 게시글 목록 조회
				case 6: searchTitle(); break;		// 6. 게시글 제목 검색
				
				case 7: selectBoard(); break;  		// 7. 게시글 상세 조회
				
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
	
	
	// 3.게시글 수정
	private void updateBoard() {
		
		System.out.println("*** 게시글 수정 ***");
		
		// 1. 게시글 번호, 비밀번호를 입력 받아 일치하는 글이 DB에 있는지 조회
		// -> 조회 결과를 얻어옴
		
		// 1) 게시글 번호, 비밀번호를 입력 받기
		System.out.print("수정할 게시글 번호 입력 : ");
		int boardNo = sc.nextInt();
		sc.nextLine();
		
		System.out.print("해당 글의 비밀번호 입력 : ");
		String boardPw = sc.nextLine();
		
		// 2) 게시글 번호, 비밀번호가 일치하는 글이 있는지 조회하는 Service 호출 후 결과 반환
		try {
			int result = service.checkBoard(boardNo, boardPw);
			// -> 반환형이 int인 이유 : DB 조회 시 COUNT()를 사용하여 있으면 1, 없으면 0 반환
			
			// 2. 조회 결과가 있을 경우 게시글 수정 코드 수행
			if(result == 0) { // 조회 결과 없을 경우
				System.out.println("게시글 번호가 일치하는 글이 없거나, 비밀번호가 틀렸습니다.");
		
			} else { // 게시글이 있고, 비밀번호도 일치하는 경우
				
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
				
				// 수정된 제목, 내용과 앞서 입력 받은 게시글 번호를 
				// 하나의 Board 객체에 저장
				Board board = new Board(boardNo, boardTitle, boardContent);
				
				
				// 2번에 작성했던 service.updateBoardPwx(board) 재활용
				int result2 = service.updateBoardPwx(board);
				
				if(result2 > 0) { // 수정 성공 시
					System.out.println("게시글 수정 성공");
				}else {
					System.out.println("게시글 수정 실패");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 4. 게시글 삭제
	private void deleteBoard() {
		
		System.out.println("*** 게시글 삭제 ***");
		
		// 1. 게시글 번호, 비밀번호를 입력 받아 해당 게시글이 존재하는지 확인
		System.out.print("삭제할 게시글 번호 : ");
		int boardNo = sc.nextInt();
		sc.nextLine(); // 입력 버퍼에 남아있는 개행문자 제거
		
		System.out.print("해당 게시글의 비밀번호 : ");
		String boardPw = sc.nextLine();
		
		// 메뉴 3번에서 작성했던 게시글 번호, 비밀번호 확인 Service 메소드 재활용
		try {
			int result = service.checkBoard(boardNo, boardPw);
			// alt + shift + r : 이름 일괄 변경 (이클립스)
			
			if(result == 0) { // 일치하는 글이 없음
				System.out.println("게시글 번호가 일치하는 글이 없거나, 비밀번호가 틀렸습니다.");
				
			}else {
				// 2. 존재하는 경우 
				// "정말로 삭제하시겠습니까?(y/n)" 문구를 출력 후
				// 'y'를 입력하면 삭제를 수행
				// 'n'을 입력하면 삭제 취소 
				
				System.out.print("정말로 삭제하시겠습니까?(y/n) : ");
				char input = sc.next().charAt(0);
				// String.charAt(index) : 문자열에서 index번째 문자를 얻어옴
				
				
				if(input == 'y') {
					
					// 게시글 삭제 Service 메소드 호출 후 결과 반환 받기
					int result2 = service.deleteBoard(boardNo); 
					
					if( result2 > 0) {
						System.out.println("게시글 삭제 성공");
					}else {
						System.out.println("게시글 삭제 실패");
					}
					
				} else if(input == 'n') {
					System.out.println("삭제 취소");
				} else {
					System.out.println("잘못 입력 하셨습니다.");
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 5,6에서 사용될 목록 출력 메소드
	private void printList(List<Board> list) {
		
		if( !list.isEmpty() ) { // list가 비어있지 않을 때
			
			System.out.printf("%3s | %20s | %5s | %s | %3s\n",
					"글번호", "제목", "작성자", "작성일", "조회수");
			
			// 향상된 for문 사용
			for( Board board : list ) {
				System.out.printf("%3d | %20s | %5s | %s | %3d\n",
						board.getBoardNo(), 
						board.getBoardTitle(), 
						board.getBoardWriter(),
						board.getCreateDt().toString(), 
						board.getReadCount());
					
			}	
			
		} else {
			System.out.println("조회 결과가 없습니다.");
			
		}
		
	}

	
	// 5. 게시글 목록 조회
	private void selectAll() {
		
		System.out.println("*** 게시글 목록 조회 ***");
		
		// 게시글 목록 조회 Service 호출 후 결과를 반환 받아 저장
		List<Board> list;
		try {
			list = service.selectAll();
			
			// 목록 모양으로 출력
			printList(list);
			
		} catch (Exception e) {
			System.out.println("게시글 목록 조회 과정에서 문제가 발생했습니다.");
			e.printStackTrace();
		}
		
	}
	
	
	// 6. 게시글 제목 검색(목록 조회)
	private void searchTitle() {
		System.out.println("*** 게시글 제목 검색 ***");
		
		// 1. 검색할 제목 입력 받기
		System.out.print("검색할 게시글 제목에 포함되는 내용 입력 : ");
		String boardTitle = sc.nextLine();
		
		// 2. 게시글 제목 검색 Service를 호출한 후 결과 반환 받기
		List<Board> list;
		try {
			list = service.searchTitle(boardTitle);
			
			// 3. 목록 모양으로 출력
			printList(list);
			
		} catch (Exception e) {
			System.out.println("게시글 목록 조회 과정에서 문제가 발생했습니다.");
			e.printStackTrace();
		}
		
	}
	
	
	// 7. 게시글 상세 조회
	private void selectBoard() {
		
		System.out.println("*** 게시글 상세 조회 ***");
		
		// 1. 상세 조회할 게시글 번호를 입력 받기
		System.out.print("상세 조회할 게시글 번호 입력 : ");
		int input = sc.nextInt();
		sc.nextLine();
		
		try {
			// 2. 상세 조회 Service 호출 후 결과 반환 받기
			Board board = service.selectBoard(input);
			
			// 3. 상세 조회 결과에 따라 출력 화면 제어
			if( board != null ) { // 1) 조회 결과가 있는 경우
				System.out.println("--------------------------------------------");
				System.out.printf("[%2d ]  %s\n", 
							board.getBoardNo(), board.getBoardTitle());
				System.out.println("--------------------------------------------");
				System.out.printf("작성자 : %s      | 작성일 : %s\n", 
							board.getBoardWriter(), board.getCreateDt().toString());
				System.out.println("--------------------------------------------");
				System.out.printf("조회수 : %d\n", board.getReadCount());
				System.out.println("--------------------------------------------");
				System.out.println(board.getBoardContent());
				System.out.println("--------------------------------------------");
				
				
			} else { // 2) 조회 결과가 없는 경우 == 입력된 게시글 번호와 일치하는 글이 없음.
				System.out.println("입력된 번호의 게시글이 존재하지 않습니다.");
			}
			
			
			
		}catch (Exception e) {
			System.out.println("게시글 상세 조회 과정에서 문제가 발생했습니다.");
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
	
	
	
}
