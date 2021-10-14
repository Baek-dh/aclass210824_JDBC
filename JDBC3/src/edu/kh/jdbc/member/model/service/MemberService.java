package edu.kh.jdbc.member.model.service;

// JDBCTemplate 클래스 static import 추가
import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;

import edu.kh.jdbc.member.model.dao.MemberDAO;
import edu.kh.jdbc.member.model.vo.Member;
import edu.kh.jdbc.view.MainView;

public class MemberService {
	
	// 필드
	private MemberDAO dao = new MemberDAO();

	/** 로그인 Service
	 * @param memberId
	 * @param memberPw
	 * @return member (로그인된 회원 정보)
	 * @throws Exception
	 */
	public Member login(String memberId, String memberPw) throws Exception {

		Connection conn = getConnection(); // 커넥션 얻어오기
		
		// 로그인 DAO 메소드 호출 후 결과 반환 받기
		Member member = dao.login(memberId, memberPw, conn);
		
		close(conn); // 커넥션 반환
		
		return member;
	}

	
	/** 아이디 중복 검사 Service
	 * @param memberId
	 * @return result (중복되면 1, 아니면 0)
	 * @throws Exception
	 */
	public int idDupCheck(String memberId) throws Exception{
		
		Connection conn = getConnection(); // 커넥션 얻어오기
		
		int result = dao.idDupCheck(memberId, conn); // dao 메소드 호출 후 결과 반환 받기
		
		close(conn); // 커넥션 반환
		
		return result;
	}


	/** 회원 가입 Service
	 * @param member
	 * @return result (가입 성공 1, 실패 0)
	 * @throws Exception
	 */
	public int signUp(Member member) throws Exception {
		
		Connection conn = getConnection(); // 커넥션 얻어오기
		
		int result = dao.signUp(member, conn); // DAO 메소드 수행 후 결과 반환 받기
		
		// 트랜잭션 제어 처리
		if(result > 0)	commit(conn);
		else			rollback(conn);
		
		close(conn); // 커넥션 반환
		
		return result; // 결과 반환
	}


	/** 내 정보 수정 Service
	 * @param memberPw
	 * @param phone
	 * @return result (성공 1, 실패 0)
	 * @throws Exception
	 */
	public int updateMember(String memberPw, String phone) throws Exception{
		
		// 1. Connection 얻어오기
		Connection conn = getConnection();
		
		// 2. DAO 메소드 호출 후 결과 반환 받고
		int result = dao.updateMember(memberPw, phone, conn);
		
		// 3. 트랜잭션 제어 처리
		if(result > 0) {
			commit(conn);
			
			// * DB에 저장된 회원 정보는 수정되었지만,
			//   로그인 되어있는 회원의 정보는 수정 전 정보이다!
			// -> 로그인된 회원 정보의 수정도 필요하다
			
			MainView.loginMember.setPhone(phone);
			// 비밀번호는 로그인 정보를 저장하는 객체에 저장하지 않는다.
			
		}else {
			rollback(conn);
		}
		
		// 4. 사용한 Connection 객체 반환
		close(conn);
		
		// 5. 결과 리턴
		return result;
	}


	/** 회원 탈퇴 Service
	 * @param memberPw
	 * @return result(성공 1, 실패 0)
	 * @throws Exception
	 */
	public int deleteMember(String memberPw) throws Exception {
		
		Connection conn = getConnection();
		
		int result = dao.deleteMember(memberPw, conn);
		
		if(result > 0) {
			commit(conn);
		}else {
			rollback(conn);
		}

		close(conn);		
		
		return result;
	}

	
	
	
}
