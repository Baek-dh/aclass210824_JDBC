package edu.kh.board.model.service;

// static 임포트 추가
import static edu.kh.board.common.JDBCTemplate.*;

import java.sql.Connection;

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
	
	
	
	
}
