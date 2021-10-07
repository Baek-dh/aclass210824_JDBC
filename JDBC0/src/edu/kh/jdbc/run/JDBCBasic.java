package edu.kh.jdbc.run;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCBasic {
	
	public static void main(String[] args) {
		
		// JDBC(Java DataBase Connectivity)
		// - Java에서 DB에 접근할 수 있게 해주는 Java 프로그래밍 API
		// - Java에서 DB와 연결하기 위한 인터페이스, 클래스를 제공
		//   (java.sql 패키지)
		
		
		// ojdbc6.jar  (Oracle JDBC 드라이버 포함되어 있음. )
		// - Oracle에서 제공하는 
		//   Java와 Oracle DBMS 연결을 위한 라이브러리(클래스, 코드)
		
		// -> 프로젝트에 등록하는 작업 필수!!
		
		//////////////////////////////////////////////////////////////////////
		
		
		// 1. JDBC 객체 참조 변수 선언
		
		Connection conn = null;
		// DB의 연결 정보를 담은 객체
		// -> 연결정보 : 아이디, 비밀번호, 주소, 접속방식, DB 이름
		// --> Java 프로그램과 DB 사이를 연결해주는 통로(길)의 역할을 함
		
		Statement stmt = null;
		// Connection 객체를 통해 DB에 SQL문을 전달하여 실행시키고
		// 결과를 반환 받는 역할을 하는 객체
		
		ResultSet rs = null;
		// SELECT문 질의 성공 시 반환되는 결과를 저장하는 객체
		
		
		try {
			// 2. Connection 객체 얻어오기
			
			// 2-1) Oracle JDBC Driver 메모리에 로드하기 
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 요즘에는 안써도 자동으로 로드된다고 함...
			
			// 2-2) DriverManager 객체를 이용하여 Connection 객체 얻어오기
			
			// DriverManager : 로드된 JDBC 드라이버를 이용하여 커넥션을 만드는 객체
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "kh", "kh");
												// DB 주소,이름, 타입             / 아이디 / 비밀번호       
			
			// jdbc:oracle:thin : JDBC 드라이버 타입이 thin 타입임을 의미
			
			// @127.0.0.1 (루프백 아이피) : 내 컴퓨터 주소
			// :1521 : 포트 번호 (포트? 프로그램이 외부와 통신하기 위해 열려 있는 부분의 주소)
			//			-> 1521번 포트가 Oracle DBMS 설치 시 기본 포트
			
			// xe : Express 의 약자, DB 이름
			
			// -- 중간 확인 --
			// 커넥션이 정상적으로 생성되었는지 확인. -> 오류가 발생하지 않으면 DB 연결이 성공된 것!
			System.out.println(conn); // oracle.jdbc.driver.T4CConnection@258e2e41
			
			
			// 3. DB에 전달할 SQL 구문을 작성
			String sql = "SELECT EMP_NAME, SALARY, DEPT_CODE FROM EMPLOYEE"  ;
			// !!! 경고 !!!
			// JAVA에서 SQL 작성 시 ;(세미콜론) 포함 X
			
			
			// 4. SQL을 전달하여 수행하고, 결과를 반환 받을 객체
			//   Statement를 생성
			stmt = conn.createStatement();
			
			
			// 5. Statment 객체에 SQL(select)을 담아 DB에서 수행하고 
			//    결과(ResultSet)를 받아와 ResultSet rs 변수에 저장
			rs = stmt.executeQuery(sql);
				// executeQuery(sql) : select문을 수행하고 ResultSet을 반환 하는 메소드
			
			
			// 6. 조회 결과 있을 경우 커서를 이용해 행을 순서대로 접근
			while( rs.next() ) {
				// rs.next() : 커서를 이용해 행을 순서대로 접근
				
				
				// 7. rs.get[Type]("컬럼명") 메소드를 이용해 컬럼 값 가져오기
				//     [Type]은 컬럼 값의 Java 버전 자료형
				
				String empName = rs.getString("EMP_NAME");
				int salary = rs.getInt("SALARY");
				String deptCode = rs.getString("DEPT_CODE");
				
				System.out.printf("이름 : %s  / 급여 : %d / 부서코드 : %s \n"
								, empName, salary, deptCode );
			}
			
			
			
		}catch(Exception e) {
			// SQLException : DB 관련 예외의 최상위 부모
			// Exception : 모든 예외의 최상위 부모
			e.printStackTrace();
			
		}finally { // 예외가 발생 하든 말든, 사용한 자원을 반환
			
			// 8. 사용한 JDBC 객체 자원 반환
			
			// 8-1) 사용한 객체의 역순으로 close()를 수행한다 
			// 8-2) close() 전 null 여부 검사 수행
			
			try {
				
				if(rs != null) {  rs.close();  }
				
				if(stmt != null) {  stmt.close();  }
				
				if(conn != null) {  conn.close();  }
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
