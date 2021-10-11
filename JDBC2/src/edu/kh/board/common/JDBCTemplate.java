package edu.kh.board.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTemplate {
	// DB 연결, JDBC 자원 반환과 같은
	// JDBC 관련 공통 내용을 추출해 모아둔 클래스
	
	// 1. Connection 객체 생성해서 반환
	
	// 2. 전달 받은 JDBC 객체 자원 반환 (close())
	//    -> conn, stmt, pstmt, rs
	
	// 3. 트랜잭션 제어 구문
	//    -> commit(), rollback()
	
	// 객체 생성을 하지 않아도 되는 변수 선언
	// 단, 외부로 부터 직접 접근은 차단
	private static Connection conn = null;
	
	
	// DB 연결 정보를 담고있는 Connection 객체 반환용 메소드
	public static Connection getConnection() {
		
		// 이전에 생성된 Connection이 있고, close() 되지 않았을 경우
		// 이전 Connection을 재활용하고
		
		// 이전에 생성된 Connection이 없거나, close()되었으면
		// 새로운 Connection을 반환
		
		// 왜? 이렇게 해야 되는가??
		//  -> 우리는 한 프로그램에 여러 명이 접속하는 형태를 만들 예정
		//   --> 접속한 사람 당 커넥션을 1개씩만 만들 수 있게 함
		
		try {
			
			if( conn == null  || conn.isClosed() ) {
				//  conn.isClosed() : Connection 객체가 close() 된적 있으면 true;
			
				Class.forName("oracle.jdbc.driver.OracleDriver");
				
				String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
				String userName = "board";
				String password = "board1234";
				
				conn = DriverManager.getConnection(url, userName, password);
				
				conn.setAutoCommit(false); // 자동 커밋 기능 off
				// 만약 true인 경우 SQL이 수행 되자마자 자동으로 Commit이 수행됨.
				
				// (주의사항) conn.close()가 수행되는 경우 자동으로 commit()이 같이 수행된다.
				// -> conn.close() 호출 전에 트랜잭션 제어 구문을 작성해야된다!
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	// 		   Statement 객체 반환 메소드  
	// PreparedStatement
	public static void close(Statement stmt) {
							// -> 매개변수에 다형성을 적용하여
							// Statement, PreparedStatement 두 객체의
							// close() 동작을 하나의 메소드로 처리할 수 있다.
		try {
			
			if(stmt != null && !stmt.isClosed() ) {
				stmt.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// ResultSet 반환 메소드
	public static void close(ResultSet rs) {
		try {
		
			if(rs != null && !rs.isClosed() ) {
			rs.close();
		}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	// Connection 반환 메소드
	public static void close(Connection conn) {
		try {
		
			if(conn != null && !conn.isClosed() ) {
				conn.close();
		}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}		
	
	
	// commit용 메소드
	public static void commit(Connection conn) {
		try {
		
			if(conn != null && !conn.isClosed() ) {
				conn.commit();
		}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	// rollback용 메소드
	public static void rollback(Connection conn) {
		try {
		
			if(conn != null && !conn.isClosed() ) {
				conn.rollback();
		}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
