package edu.kh.scott.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.scott.model.vo.Emp;

public class EmpDAO {

	// DAO( Database Access Object, 데이터베이스 접근 객체 )
	// - 자바와 데이터베이스 연결을 위한 구문을 작성하는 클래스
	//   -> JDBC 코드가 작성되는 클래스
	// - SQL 작성, SQL 수행, 결과 반환
	
	
	
	// 필드
	// JDBC 객체 참조 변수를 선언 (java.sql)
	private Connection conn = null;	// DB 연결 정보를 담은 객체로, Java - DB 연결 통로 역할
	private Statement stmt = null; // Connection을 통해 SQL을 전달하고, 결과를 반환 받아오는 역할
	private ResultSet rs = null; // SELECT문의 결과를 저장하기위한 역할
	
	private PreparedStatement pstmt = null;
	// SQL이 담기면 바로 출발하는 것이 아닌
	// 준비가 완료된 후 DB에 SQL을 전달하고 결과를 반환 받아오는 역할의 객체
	// *** SQL 구문에 빈칸을 만들어두어 빈칸이 다 채워진 후
	//     별도의 SQL 전달 메소드를 수행한다.
	
	// - ? (위치 홀더) : SQL 구문에 들어갈 리터럴을 동적으로 작성하게 하는 부분
	// ex) select * from emp where empno = ?
	//     pstmt.setInt(1, 8000);
	
	// 장점 : SQL 구문에 자바 변수를 합칠 때 간단하다.
	//       setString() 메소드를 사용하면 문자열 양쪽에 '' 생략할 수 있다.
	
	// 단점 : Statement사용 구문보다 코드가 길어짐.
	
	
	// Connection 생성 시 사용하는 값을 미리 작성해둠
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	private String userName = "scott";
	private String password = "tiger";
	
	
	// 1-4. 전체 사원 정보 조회 DAO 메소드 생성
	public List<Emp> selectAll() {
		
		List<Emp> empList = null; // 조회 결과를 저장하고 반환하는 용도의 List 참조 변수
		
		try { // 서로 다른 프로그램(Java와 DB) 연결 시 문제가 발생할 가능성이 있다!
			
			// 1-5. Connection 얻어오기
			// 1) Oracle JDBC Driver 메모리에 로드하기
			Class.forName(driver);
			
			// 2) DriverManager를 이용하여 Connection 얻어오기
			conn = DriverManager.getConnection(url, userName, password);
			
			
			// 1-6. Connection을 이용해 Statement 객체 생성
			stmt = conn.createStatement();
			
			
			// 1-7. 수행하려는 SQL을 작성
			String sql = "SELECT * FROM EMP ORDER BY EMPNO"; // (절대 주의!) SQL 끝에 세미콜론 X
			
			
			// 1-8. Statement에 SQL을 담아 DB로 전달하여 실행 후 
			// 결과를 반환 받아와서 rs에 저장
			rs = stmt.executeQuery(sql);
			// executeQuery(sql) : select문 수행하고 ResultSet을 반환하는 메소드
			
			
			// DB와 관련된 동작이 끝난 후 ArrayList 객체를 생성한 이유
			// --> DB 관련 동작 중 예외가 발생하면 
			//     empList가 null로 반환되는 상황을 만들기 위해서
			empList = new ArrayList<Emp>();
			
			
			// 1-9. rs에 저장된 ResultSet을 한 행씩 접근하기
			while( rs.next() ) {
				// rs.next() : 커서를 다음 행으로 이동 했을 때 행이 존재하면 true
				
				// 중간 확인 테스트
				//System.out.println(  rs.getInt("EMPNO")  );
				
				// 1-10. Collection 중 List를 이용하여 조회 결과를 옮겨 담기.
				// -> try 구문 위에 List 참조변수 선언
				//    while(rs.next()) 위에 ArrayList 객체 생성
				
				
				// 1-11. 접근한 행의 컬럼 값을 얻어와 Emp 객체 생성
				int empNo = rs.getInt("EMPNO");
				String eName = rs.getString("ENAME");
				String job = rs.getString("JOB");
				int mgr = rs.getInt("MGR");
				Date hireDate = rs.getDate("HIREDATE"); // java.sql.Date
				int sal = rs.getInt("SAL");
				int comm = rs.getInt("COMM");
				int deptNo = rs.getInt("DEPTNO");
				
				Emp emp = new Emp(empNo, eName, job, mgr, hireDate, sal, comm, deptNo);
				
				
				// 1-12. 생성된 Emp객체를 empList에 추가
				empList.add(emp);
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally { // 예외가 발생 하든 말든 사용한 자원을 반환해야 한다!
			
			
			// 1-13. 사용한 JDBC 객체 자원 반환
			try {
				if(rs != null) 		rs.close();
				if(stmt != null) 	stmt.close();
				if(conn != null)	conn.close();
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		return empList; // 조회 결과를 Service로 반환
	}
	
	
	
	// 2-5. 사번으로 사원 정보를 조회하는 DAO 메소드 생성
	public Emp selectOne(int input) {
	
		// 2-6. 조회 결과를 저장할 변수 선언 
		// 왜? try 구문이 끝난 후 반환하기 위해서
		Emp emp = null;
		
		try {
			// 2-7. JDBC 드라이버 로드
			Class.forName(driver);
			
			// 2-8. DriverManager를 이용하여 Connection 얻어오기
			conn = DriverManager.getConnection(url, userName, password);
			
			// 2-9. SQL을 수행하고 결과를 받아올 객체 Statement  객체 생성
			stmt = conn.createStatement();
			
			// 2-10. 전달할 SQL 작성
			String sql = "SELECT * FROM EMP WHERE EMPNO = " + input;
			// -> 입력 받은 사번을 SQL의 WHERE절에 추가
			
			
			// 2-11. SQL을 수행하고 결과를 반환 받아와서 rs에 저장
			rs = stmt.executeQuery(sql);
			
			
			// 2-12. rs에 저장된 ResultSet에 한 행씩 접근하기
			// -> 현재 수행한 SQL은 0행 또는 1행의 결과만 반환을 함
			//    == 조회 결과가 있는지 1회만 검사하면 된다
			if( rs.next() ) {
				
				// while이 아닌 if를 작성한 이유
				// -> 조회 결과가 1행 밖에 없으므로
				//    무거운 반복문 코드를 작성할 필요가 없음.
				
				// 2-13. 현재 접근한 행에서 컬럼 값을 얻어와 변수에 저장 후
				//       객체 생성
				int empNo = rs.getInt("EMPNO");
				String eName = rs.getString("ENAME");
				String job = rs.getString("JOB");
				int mgr = rs.getInt("MGR");
				Date hireDate = rs.getDate("HIREDATE"); // java.sql.Date
				int sal = rs.getInt("SAL");
				int comm = rs.getInt("COMM");
				int deptNo = rs.getInt("DEPTNO");
				
				emp = new Emp(empNo, eName, job, mgr, hireDate, sal, comm, deptNo);
			}
			
			// emp 참조 변수 상태
			// 1) 입력 받은 사번과 일치하는 사원이 있어서 Emp 객체가 생성됨
			//    ->  emp != null
			
			// 2) 입력 받은 사번과 일치하는 사원이 없으면 Emp 객체가 생성되지 않음.
			//    ->  emp == null
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			
			// 2-14. 사용한 JDBC 객체 자원 반환
			try {
				if( rs != null )	rs.close();
				if( stmt != null )	stmt.close();
				if( conn != null )	conn.close();
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return emp; // 조회 결과를 반환
	
	}
	
	
	// 7. 부서 번호를 입력 받아 일치하는 사원 정보 조회 DAO 메소드 생성
	public List<Emp> selectDeptNo(int input){
		
		List<Emp> empList = null;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			
			stmt = conn.createStatement();
			
			String sql = "SELECT * FROM EMP WHERE DEPTNO = " + input;
			
			rs = stmt.executeQuery(sql);
			
			empList = new ArrayList<Emp>();
			
			while( rs.next() ) {
				
				int empNo = rs.getInt("EMPNO");
				String eName = rs.getString("ENAME");
				String job = rs.getString("JOB");
				int mgr = rs.getInt("MGR");
				Date hireDate = rs.getDate("HIREDATE"); // java.sql.Date
				int sal = rs.getInt("SAL");
				int comm = rs.getInt("COMM");
				int deptNo = rs.getInt("DEPTNO");
				
				Emp emp = new Emp(empNo, eName, job, mgr, hireDate, sal, comm, deptNo);
				
				empList.add(emp);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			
			
		}finally {
			
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return empList;
	}
	

	// 3-7. 사원 정보 삽입 DAO 메소드 생성
	public int insertEmp(Emp emp) {
		
		// (매우 중요!)
		// INSERT, UPDATE, DELETE와 같은 DML 구문 수행 시
		// 수행 결과로 성공한 행의 개수가 반환된다!!!!!
		// ==> 정수형 변수 int로 DML 수행 결과를 저장하면 된다!
		
		// 3-8. SQL 수행 결과를 저장할 변수 선언
		int result = 0;
		
		try {
			
			// 3-9. Connection 얻어오기
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			
			// 3-10. SQL 작성 (위치홀더(?) 사용)
			String sql = "INSERT INTO EMP VALUES( SEQ_EMPNO.NEXTVAL, ?, ?, ?, SYSDATE, ?, ?, ? )";                  
			
			// 3-11. Connection을 이용하여 PreparedStatement 생성하기
			pstmt = conn.prepareStatement(sql);
			
			// 3-12. 위치홀더에 들어갈 알맞은 값 세팅
			pstmt.setString(1, emp.geteName() );
			pstmt.setString(2, emp.getJob() );
			pstmt.setInt(3, emp.getMgr());
			pstmt.setInt(4, emp.getSal());
			pstmt.setInt(5, emp.getComm());
			pstmt.setInt(6, emp.getDeptNo());
			
			// 3-13. pstmt 세팅이 완료되었으면 DB로 전달하여 수행 후 결과를 반환 받음.
			result = pstmt.executeUpdate();
			// executeUpdate() : INSERT, UPDATE, DELETE 구문을 수행하고 성공한 행의 개수를 반환
			
			
			// 3-14. 트랜잭션 제어처리
			if (result > 0) { // DML 구문 수행 성공
				conn.commit(); // 트랜잭션 commit
			}else {
				conn.rollback();
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			
			// 3-15. 사용한 JDBC 객체 자원 반환
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null)  conn.close();
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result; // 삽입 결과를 Service로 반환
	}
	
	
	
	
	
	
	
	
	
}
