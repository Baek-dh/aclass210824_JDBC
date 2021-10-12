package edu.kh.board.model.service;

// static 임포트 추가
import static edu.kh.board.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import edu.kh.board.common.JDBCTemplate;
import edu.kh.board.model.dao.BoardDAO;
import edu.kh.board.model.vo.Board;

/**
 * @author 백동현
 *
 */
public class BoardService { // 비즈니스 로직 처리(데이터 가공, 트랜잭션 제어)
	// 트랜잭션 제어를 한다 == commit(), rollback()을 수행한다
	// --> commit(), rollback() 메소드는 Connection 객체에 있다!!
	//    --> Service 클래스 안에 Connection 객체가 존재해야 한다.
	
	private BoardDAO dao = new BoardDAO();
	
	
	// alt + shift + j : 클래스/메소드 설명용 주석
	// @param : 매개변수
	// @return :  반환값
	
	/** 게시글 삽입 Service 
	 * @param board
	 * @return result
	 */
	public int insertBoard(Board board) throws Exception{
		
		// JDBCTemplate을 이용하여 Connection 얻어오기
		// (static이 붙은 변수, 메소드는 클래스명.변수명,  클래스명.메소드명() 으로 호출)
		Connection conn = getConnection();
		
		// 게시글 정보 삽입을 수행하는 DAO 메소드 호출 후 결과 반환
		int result = dao.insertBoard( board, conn );
										  // -> Statement 관련 객체 생성 시 필요
		
		// DAO 수행 후 가능한 조건
		// 1) result 가 0 또는 1
		// 2) 예외가 발생하여 정상 수행되지 않음
		
		// 트랜잭션 제어 처리
		if( result > 0 ) commit(conn);
		else			 rollback(conn);
		
		// Connection 반환
		close(conn);
		
		return result;
	}

	
	/** 게시글 수정(비밀번호 x) Service
	 * @param board
	 * @return result
	 * @throws Exception
	 */
	public int updateBoardPwx(Board board) throws Exception{
		// ** service, dao 메소드 생성 시 throws Exception 구문을 추가하자!!
		
		// 1. Connection 얻어오기
		Connection conn = getConnection();
		
		// 2. 매개변수 board와 conn을 DAO 메소드로 전달, 결과를 반환 받아 저장
		int result = dao.updateBoardPwx(board, conn);
		
		// 3. DAO 수행 결과에 따른 트랜잭션 제어
		if(result > 0)	commit(conn);
		else			rollback(conn);
		
		// 4. Connection 반환
		close(conn);
		
		// 5. 결과 반환
		return result;
	}

	// alt + shift + j : 설명용 주석
	/** 게시글 수정 Service
	 * @param boardNo
	 * @param boardPw 
	 * @return result (0 : 조회결과 X, 1: 조회결과 O)
	 * @throws Exception
	 */
	public int checkBoard(int boardNo, String boardPw) throws Exception {
												// 발생한 예외를 View에 던져서 처리
		
		// 1. Connection 얻어오기
		Connection conn = getConnection();
						// -> JDBCTemplate에 static 메소드로 작성해둠
		
						// JDBCTemplate.getConnection() 이 정상 호출 방법이지만
						// 앞에 클래스명을 생략할 수 있는 이유 : static import가 추가되었기 때문
		
		// 2. DAO 메소드 호출 -> 결과 반환 받기
		//    --> 메소드 호출 시 DAO에 conn을 같이 전달함
		//        왜? DAO가 DB에 연결할 수 있을려면 Connection이 필요하기 때문
		//			-> 전달된 Connection 객체로 Statement객체 생성, SQL 수행, 결과 반환
		int result = dao.checkBoard(boardNo, boardPw, conn);
		
		// 3. Connection 반환
		close(conn);
		
		// 4. 결과 반환
		return result;
	}


	/** 게시글 삭제 Service
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int deleteBoard(int boardNo) throws Exception {
		
		Connection conn = getConnection(); // 커넥션 얻어오기
		
		int result = dao.deleteBoard(boardNo, conn); // DAO 메소드 수행 후 결과 반환 받기
		
		// 트랜잭션 제어 
		if(result > 0)	commit(conn);
		else			rollback(conn);
		
		close(conn); // Connection 반환
		
		return result; // 결과 반환
	}


	/** 게시글 목록 조회 Service
	 * @return list (조회된 게시글 목록)
	 * @throws Exception
	 */
	public List<Board> selectAll() throws Exception{
		
		Connection conn = getConnection(); // 커넥션 얻어오기
		
		List<Board> list = dao.selectAll(conn);  // DAO 메소드 호출 후 결과 반환 받기
		
		close(conn); // 커넥션 반환
		
		return list;
	}


	/** 게시글 제목 검색 Service
	 * @param boardTitle
	 * @return list (검색 결과)
	 * @throws Exception
	 */
	public List<Board> searchTitle(String boardTitle) throws Exception{
		
		Connection conn = getConnection();
		
		List<Board> list = dao.searchTitle(boardTitle, conn);
		
		close(conn);
		
		return list;
	}


	/** 게시글 상세 조회 Service
	 * @param input
	 * @return board (상세 조회 결과)
	 * @throws Exception
	 */
	public Board selectBoard(int input) throws Exception {
		
		Connection conn = getConnection(); // 커넥션 얻어오기
		
		// 게시글 상세 조회 DAO 메소드 호출 후 결과 반환 받기
		Board board = dao.selectBoard(input, conn);
		
		// ****************************************
		
		// 조회수 증가
		if(board != null) { // 상세 조회 성공
			int result = dao.increaseReadCount(input, conn);
			//  0 / 1
			
			if(result > 0) { // 트랜잭션 제어
				commit(conn);
				
				// DB에 저장되있는 조회수는 증가 되었지만
				// 미리 상세조회한 board의 조회수는 이전 상태이다 -> +1 증가
				board.setReadCount(  board.getReadCount() +1   );
				
			}else {
				rollback(conn);
			}
			
		}
		
		// ****************************************
		
		
		close(conn); // 커넥션 반환
		
		return board;
	}
	
	
	
	
	
	
	
}
