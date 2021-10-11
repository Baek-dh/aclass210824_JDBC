package edu.kh.board.model.dao;

// static 임포트 : 지정된 경로에 있는 static 변수, 메소드 호출 시 클래스명을 생략 가능
import static edu.kh.board.common.JDBCTemplate.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import edu.kh.board.common.JDBCTemplate;
import edu.kh.board.model.vo.Board;

public class BoardDAO {
	
	// 필드
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	

	/** 게시글 삽입 DAO
	 * @param board
	 * @param conn
	 * @return result (DML 성공한 행의 수)
	 */
	public int insertBoard(Board board, Connection conn) throws Exception{
												// -> 호출한 곳으로 발생 한 예외를 던짐
		// 결과 저장용 변수
		int result = 0;
		
		try {
			// Connection 얻어오기 -> 매개변수로 전달 받음 
			
			// SQL 작성
			String sql = "INSERT INTO TB_BOARD "
					   + "VALUES( SEQ_BOARD_NO.NEXTVAL, ?, ?, ?, ?, DEFAULT, DEFAULT )";
			
			// PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더에 알맞은 값 세팅
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getBoardContent());
			pstmt.setString(3, board.getBoardWriter());
			pstmt.setString(4, board.getBoardPw());
			
			// SQL 수행 후 결과를 반환 받기
			result = pstmt.executeUpdate();
			
		}finally {
			// 사용한 JDBC 객체 자원 반환
			close(pstmt);
		}
		
		return result;
	}



	/** 게시글 수정(비밀번호 X) DAO
	 * @param board
	 * @param conn
	 * @return result
	 * @throws Exception
	 */
	public int updateBoardPwx(Board board, Connection conn) throws Exception {
		
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. 커넥션 얻어오기 == Service에서 매개변수로 전달 받음
			
			// 3. SQL 작성
			String sql = "UPDATE TB_BOARD SET BOARD_TITLE = ?, BOARD_CONTENT = ? WHERE BOARD_NO = ?";
			
			// 4. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 5. 위치 홀더에 알맞은 값 세팅
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getBoardContent());
			pstmt.setInt(3, board.getBoardNo());
			
			// 6. SQL 수행 후 결과를 반환 받아 저장
			result = pstmt.executeUpdate();
			
		}finally {
			// 7. 사용한 JDBC 객체 자원 반환
			close(pstmt);
		}
		
		return result;
	}

	
	
	
	
	
}
