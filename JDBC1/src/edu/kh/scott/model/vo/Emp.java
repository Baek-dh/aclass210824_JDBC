package edu.kh.scott.model.vo;

import java.sql.Date;

public class Emp {

	// VO(Value Object, 값 저장용 객체)
	// -> DB와 연결해서 사용하는 경우
	//    DB에서 조회된 결과의 한 행의 정보를 기록하는 용도로 사용
	
	private int empNo;		// 사번
	private String eName;	// 이름
	private String job;		// 직급
	private int mgr;		// 매니저(직속 상사) 번호
	private Date hireDate;  // 입사일 (java.sql.Date)
	private int sal;		// 급여
	private int comm;		// 커미션(성과금, 보너스)
	private int deptNo;		// 부서 번호
	
	// 기본 생성자
	public Emp() { }
	
	// 매개 변수 있는 생성자
	public Emp(int empNo, String eName, String job, int mgr, Date hireDate, int sal, int comm, int deptNo) {
		super();
		this.empNo = empNo;
		this.eName = eName;
		this.job = job;
		this.mgr = mgr;
		this.hireDate = hireDate;
		this.sal = sal;
		this.comm = comm;
		this.deptNo = deptNo;
	}
	
	// 사원 정보 삽입에 사용하는 생성자
	public Emp(String eName, String job, int mgr, int sal, int comm, int deptNo) {
		super();
		this.eName = eName;
		this.job = job;
		this.mgr = mgr;
		this.sal = sal;
		this.comm = comm;
		this.deptNo = deptNo;
	}

	//getter / setter
	public int getEmpNo() {
		return empNo;
	}
	
	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}


	public String geteName() {
		return eName;
	}


	public void seteName(String eName) {
		this.eName = eName;
	}


	public String getJob() {
		return job;
	}


	public void setJob(String job) {
		this.job = job;
	}


	public int getMgr() {
		return mgr;
	}


	public void setMgr(int mgr) {
		this.mgr = mgr;
	}


	public Date getHireDate() {
		return hireDate;
	}


	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}


	public int getSal() {
		return sal;
	}


	public void setSal(int sal) {
		this.sal = sal;
	}


	public int getComm() {
		return comm;
	}


	public void setComm(int comm) {
		this.comm = comm;
	}


	public int getDeptNo() {
		return deptNo;
	}


	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}


	@Override
	public String toString() {
		return "Emp [empNo=" + empNo + ", eName=" + eName + ", job=" + job + ", mgr=" + mgr + ", hireDate=" + hireDate
				+ ", sal=" + sal + ", comm=" + comm + ", deptNo=" + deptNo + "]";
	}
	// alt + shift + s  ->  s
	
	
	/*
	// toString() 오버라이딩(상속 받은 부모 메소드를 자식이 재정의)
	@Override
	public String toString() { // 객체 필드 값을 모두 보여주는 용도
		
		return empNo + " / " + eName + " / "
	}
	*/

	
	
	
	
	
	
	
}
