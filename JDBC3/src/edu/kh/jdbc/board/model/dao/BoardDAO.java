package edu.kh.jdbc.board.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import edu.kh.jdbc.board.model.vo.Board;
import edu.kh.jdbc.view.MainView;

public class BoardDAO {

	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private Properties prop = null;
	// Properties : K, V가 String으로 제한된 Map
	
	
	public BoardDAO() { // BoardDAO 객체 생성 시 board-sql.xml 파일에 작성된 내용 읽어오기
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("board-sql.xml"));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	/** 게시글 삽입 DAO
	 * @param boardTitle
	 * @param boardContent
	 * @param conn
	 * @return result (성공 1, 실패 0)
	 * @throws Exception
	 */
	public int insertBoard(String boardTitle, String boardContent, Connection conn) throws Exception {
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("insertBoard");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, boardTitle);
			pstmt.setString(2, boardContent);
			pstmt.setInt(3, MainView.loginMember.getMemberNo());
			
			result = pstmt.executeUpdate();
		}finally {
			close(pstmt);
		}
		
		return result;
	}


	/** 로그인한 회원의 작성글이 맞는지 확인 DAO
	 * @param boardNo
	 * @param conn
	 * @return result(맞으면 1, 아니면 0)
	 * @throws Exception
	 */
	public int checkBoard(int boardNo, Connection conn) throws Exception{
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("checkBoard");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			pstmt.setInt(2, MainView.loginMember.getMemberNo());
			
			// SELECT 수행 결과 == ResultSet
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}
		
		return result;
	}


	/** 게시글 수정 DAO
	 * @param board
	 * @param conn
	 * @return result (성공1, 실패0)
	 * @throws Exception
	 */
	public int updateBoard(Board board, Connection conn) throws Exception{
	
		int result = 0;
		
		try {
			String sql = prop.getProperty("updateBoard");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getBoardContent());
			pstmt.setInt(3, board.getBoardNo());
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}
		
		return result;
	}


	/** 게시글 삭제 DAO
	 * @param boardNo
	 * @param conn
	 * @return result(성공 1, 실패 0)
	 * @throws Exception
	 */
	public int deleteBoard(int boardNo, Connection conn) throws Exception {
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("deleteBoard");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}
		
		return result;
	}


	/** 게시글 상세 조회 DAO
	 * @param boardNo
	 * @param conn
	 * @return board (성공 != null, 실패 == null)
	 * @throws Exception
	 */
	public Board selectBoard(int boardNo, Connection conn) throws Exception{
		
		Board board = null; // 결과 저장용 변수
		
		try {
			String sql = prop.getProperty("selectBoard");
		
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {
				// 컬럼값을 이용하여 객체 생성
				
				board = new Board();
				
				board.setBoardNo( rs.getInt("BOARD_NO") );
				board.setBoardTitle( rs.getString("BOARD_TITLE")  );
				board.setBoardContent( rs.getString("BOARD_CONTENT") );
				board.setCreateDt(  rs.getDate("CREATE_DT")  );
				board.setReadCount( rs.getInt("READ_COUNT") );
				board.setMemberNm( rs.getString("MEMBER_NM") );
			}
			
			
		}finally {
			close(rs);
			close(pstmt);
		}
		
		return board;
	}
	
	
	
	/** 조회수 증가 DAO
	 * @param boardNo
	 * @param conn
	 * @return result (성공 1, 실패 0)
	 * @throws Exception
	 */
	public int increaseReadCount(int boardNo, Connection conn) throws Exception {
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("increaseReadCount");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}
		
		return result;
	}
	
	
	
	
	
	
}




