package edu.kh.jdbc.board.model.service;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;

import edu.kh.jdbc.board.model.dao.BoardDAO;
import edu.kh.jdbc.board.model.vo.Board;

public class BoardService {

	private BoardDAO dao = new BoardDAO();

	
	/** 게시글 삽입 Service
	 * @param boardTitle
	 * @param boardContent
	 * @return result (성공 1, 실패 0)
	 * @throws Exception
	 */
	public int insertBoard(String boardTitle, String boardContent) throws Exception{
	
		Connection conn = getConnection();
		
		int result = dao.insertBoard(boardTitle, boardContent, conn);
		
		if(result > 0)	commit(conn);
		else			rollback(conn);
		
		close(conn);
		
		return result;
	}


	
	/** 로그인한 회원의 작성글이 맞는지 확인 Service
	 * @param boardNo
	 * @return result (맞으면 1, 아니면 0)
	 * @throws Exception
	 */
	public int checkBoard(int boardNo) throws Exception{
		
		Connection conn = getConnection();
		
		int result = dao.checkBoard(boardNo, conn);
			
		close(conn);
		
		return result;
	}



	/** 게시글 수정 Service
	 * @param board
	 * @return result (성공1, 실패0)
	 * @throws Exception
	 */
	public int updateBoard(Board board) throws Exception{
		
		Connection conn = getConnection();
		
		int result = dao.updateBoard(board, conn);
		
		if(result > 0) 	commit(conn);
		else			rollback(conn);

		close(conn);
		
		return result;
	}


 
	/** 게시글 삭제 Service
	 * @param boardNo
	 * @return result (성공 1, 실패 0)
	 * @throws Exception
	 */
	public int deleteBoard(int boardNo) throws Exception{
		
		Connection conn = getConnection();
		
		int result = dao.deleteBoard(boardNo, conn);
		
		if(result > 0)	commit(conn);
		else			rollback(conn);
		
		close(conn);
		
		return result;
	}


	/** 게시글 상세 조회 Service
	 * @param boardNo
	 * @return board (성공 != null, 실패 == null)
	 * @throws Exception
	 */
	public Board selectBoard(int boardNo) throws Exception {
		
		Connection conn = getConnection();
		
		Board board = dao.selectBoard(boardNo, conn);
		
		// 조회수 증가 
		if(board != null) {
			int result = dao.increaseReadCount(boardNo, conn);
			
			if(result > 0) {
				commit(conn);
				// DB는 조회수가 증가 했지만,
				// 이전에 조회한 board에는 조회수가 증가되지 않음
				// board에 있는 readCount도 1 증가
				board.setReadCount(  board.getReadCount() + 1  );
				
			}else {
				rollback(conn);
			}
			
		}
		
		close(conn);
		
		return board;
	}
	
	
	
	
	
	
	
}
