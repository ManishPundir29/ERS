import java.sql.Date;

public class StatusReport {

	private int complianceid;
	private int statusrpid;
	private int empid;
	private String comment;
	private Date createdDate;
	private String department;
	
	public StatusReport() {}

	public int getComplianceid() {
		return complianceid;
	}

	public void setComplianceid(int complianceid) {
		this.complianceid = complianceid;
	}

	public int getStatusrpid() {
		return statusrpid;
	}

	public void setStatusrpid(int statusrpid) {
		this.statusrpid = statusrpid;
	}

	public int getEmpid() {
		return empid;
	}

	public void setEmpid(int empid) {
		this.empid = empid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "[COMPLIANCE ID : " + complianceid + "| STATUS REPORT ID : " + statusrpid + "| EMP ID : " + empid
				+ "| COMMENT : " + comment + "| CREATED DATE : " + createdDate + "| DEPARTMENT  : " + department + "]";
	}

	
	
	
}
