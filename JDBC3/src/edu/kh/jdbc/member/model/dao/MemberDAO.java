package edu.kh.jdbc.member.model.dao;

//JDBCTemplate 클래스 static import 추가
import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import edu.kh.jdbc.member.model.vo.Member;

public class MemberDAO {

	// 필드
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private Properties prop = null;

	// 기본 생성자 (== 객체 생성 메소드)
	public MemberDAO() {
		// 왜 기본생성자를 만들어서 코드를 작성했는가?
		// 1. xml 파일을 읽어 오는 코드의 중복을 막기 위해
		// 2. 클래스 바로 아래에 작성될 수 있는 코드는 필드, 메소드 선언 밖에 안되기 때문에
		
		try {
			// 회원 관련 SQL 작성 되어있는 xml 파일을 읽어와 prop에 저장
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("member-sql.xml"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/** 로그인 DAO
	 * @param memberId
	 * @param memberPw
	 * @param conn
	 * @return member (로그인된 회원 정보)
	 * @throws Exception
	 */
	public Member login(String memberId, String memberPw, Connection conn) throws Exception {
	
		Member member = null; // 결과 저장용 변수
		
		try {
			
			// SQL을 Properties 객체에서 얻어오기
			String sql = prop.getProperty("login");
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더에 알맞은 값 세팅
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPw);
			
			// SQL 수행 후 결과(ResultSet) 저장
			rs = pstmt.executeQuery();
			
			// 조회 결과가 있을 경우 조회된 컬럼 값을 얻어와 Member 객체 생성
			if(rs.next()) {
				int memberNo = rs.getInt("MEMBER_NO");
				String memberId2 = rs.getString("MEMBER_ID");
				String memberNm = rs.getString("MEMBER_NM");
				String phone = rs.getString("PHONE");
				
				member = new Member(memberNo, memberId2, memberNm, phone);
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}
		
		return member;
	}


	/** 아이디 중복 검사 DAO
	 * @param memberId
	 * @param conn
	 * @return result (중복이면 1, 아니면 0)
	 * @throws Exception
	 */
	public int idDupCheck(String memberId, Connection conn) throws Exception{
		
		int result = 0; // 결과 저장용 변수 선언
		
		try {
			
			// SQL 얻어오기
			String sql = prop.getProperty("idDupCheck");
				
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더에 알맞은 값 세팅
			pstmt.setString(1, memberId);
			
			// SQL 수행 후 결과(ResultSet) 반환 받기
			rs = pstmt.executeQuery();
			
			// 조회 결과가 있는 경우 컬럼 값을 결과 저장용 변수에 대입
			if( rs.next() )  {
				result = rs.getInt(1); 
								// 조회 결과 컬럼 순번
			}
			
		}finally {
			// 사용한 JDBC 객체 자원 반환
			close(rs);
			close(pstmt);
		}
		
		return result;
	}


	
	/** 회원 가입 DAO
	 * @param member
	 * @param conn
	 * @return result (가입 성공 1, 실패 0)
	 * @throws Exception
	 */
	public int signUp(Member member, Connection conn) throws Exception {

		int result = 0; // 결과 저장용 변수 선언
		
		try {
			// SQL 얻어오기
			String sql = prop.getProperty("signUp");
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 위치 홀더에 알맞은 값 세팅
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getMemberPw());
			pstmt.setString(3, member.getMemberNm());
			pstmt.setString(4, member.getPhone());
			
			// SQL 수행 후 결과(성공한 행의 개수) 반환 받기
			result = pstmt.executeUpdate();
			
		}finally {
			
			// 사용한 JDBC 객체 자원 반환
			close(pstmt);
			
		}
		
		return result;
	}

	
	
	
	
}
