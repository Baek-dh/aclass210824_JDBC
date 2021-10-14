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
	
	
	
	
}
