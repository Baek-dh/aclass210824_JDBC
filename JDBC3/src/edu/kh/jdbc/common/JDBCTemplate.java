package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/* DB 연결 정보를 xml 파일에 별도로 작성해서 읽어오는 형식의 코드를 작성
 * -> DB 연결 정보는 변경될 가능성이 있는데
 *    그때 마다 컴파일 하는 것은 시간 낭비가 심하다!
 *    --> 파일에 작성된 내용을 읽어오는 고정된 코드를 작성하고
 *        외부 파일의 내용을 변경하는 형식으로 코드를 작성
 * 
 * XML : 프로그래밍 언어 종류 상관 없이 데이터를 저장, 전달 가능한 파일
 * 
 * 
 * XML에 작성된 DB 연결 정보를 Properties 라는 객체를 이용해 얻어올 예정
 * 
 * Properties : 컬렉션 중 Map<K,V> 의 후손 클래스로
 * 				K, V가 모두 String으로 제한된 Map
 *  장점 : 파일 입출력에 특화 되어 있음 (특히 XML)
 * */



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
				
				// 외부 xml파일(driver.xml) 내용을 읽어와 저장할 Properties 객체 생성
				Properties prop = new Properties();
				
				// driver.xml 파일을 읽어와 저장
				prop.loadFromXML(new FileInputStream("driver.xml"));
				
				Class.forName( prop.getProperty("driver") );
				String url = prop.getProperty("url");
				String userName = prop.getProperty("userName") ;
				String password = prop.getProperty("password");
				
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
