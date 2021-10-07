package edu.kh.scott.model.service;

import java.util.List;

import edu.kh.scott.model.dao.EmpDAO;
import edu.kh.scott.model.vo.Emp;

public class EmpService {

	// Service 클래스 
	// - 비즈니스 로직 처리(데이터 가공, 트랜잭션 제어)
	
	//필드
	private EmpDAO dao = new EmpDAO();
	
	
	// 1-2. 전체 사원 정보 조회 Service 메소드 생성
	public List<Emp> selectAll() {
		
		// (데이터 가공 처리가 필요 없는 메소드)
		
		// 1-3. DAO의 selectAll() 메소드 호출
		List<Emp> empList = dao.selectAll(); // DB 조회 결과가 담긴 List<Emp> 가 반환됨
		
		// 1-14. 조회 결과를 View로 반환
		return empList;
		
	}
	
	
	// 2-3. 사번으로 사원 정보를 조회하는 Service 메소드 생성
	public Emp selectOne(int input) {
							// -> 전달 받은 사번
		
		// (데이터 가공할게 없음)
		
		// 2-4. DAO에 selectOne() 메소드를 호출 + input 함께 전달
		Emp emp = dao.selectOne(input);
		
		// (데이터 가공할게 없음)
		
		// 2-15. DB 조회 결과를 View로 반환
		return emp;
		
	}
	
	
	// 7. 부서 번호를 입력 받아 일치하는 사원 정보 조회 Service 메소드 생성
	public List<Emp> selectDeptNo(int input){
		
		List<Emp> empList = dao.selectDeptNo(input);
		
		return empList;
	}
	
	
	// 3-4. 사원 정보 삽입 Service 메소드 생성
	public int insertEmp( Emp emp ) {
		
		// 3-5. 입력된 급여 + 200  데이터 가공 처리
		emp.setSal(   emp.getSal() + 200   );
		
		// 3-6. 가공된 Emp 객체를 DAO로 전달
		int result = dao.insertEmp(emp);
		
		// 3-16. DB 삽입 결과를 View에 반환
		return result;
	}
	
	
	
	
	
	
	
	
	
	
}
