import java.sql.Date;

public class ComplianceDetails {

	private int compliance_id;
	private int department_id;
	private String department_nm;
	private String rltype;
	private String details;
	private Date createddate;
	private int empcount;
	private int statuscount;
	
	public ComplianceDetails() {
		// TODO Auto-generated constructor stub
	}

	public int getCompliance_id() {
		return compliance_id;
	}

	public void setCompliance_id(int compliance_id) {
		this.compliance_id = compliance_id;
	}

	public int getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}

	public String getDepartment_nm() {
		return department_nm;
	}

	public void setDepartment_nm(String department_nm) {
		this.department_nm = department_nm;
	}

	public String getRltype() {
		return rltype;
	}

	public void setRltype(String rltype) {
		this.rltype = rltype;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public int getEmpcount() {
		return empcount;
	}

	public void setEmpcount(int empcount) {
		this.empcount = empcount;
	}

	public int getStatuscount() {
		return statuscount;
	}

	public void setStatuscount(int statuscount) {
		this.statuscount = statuscount;
	}

	@Override
	public String toString() {
		return "[Compliance Id  : " + compliance_id + "| Department Id  : " + department_id
				+ "| Department Name  : " + department_nm + "| RL Type  : " + rltype + "| Details  : " + details
				+ "| Created Date  : " + createddate + "| Emp Count  : " + empcount + "| Status Count  : " + statuscount
				+ "]";
	}
	
	
}
