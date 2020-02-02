
public class Department {

	private long department_id;
	private String department_nm;
	public Department() {
		
	}
	public long getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(long department_id) {
		this.department_id = department_id;
	}
	public String getDepartment_nm() {
		return department_nm;
	}
	public void setDepartment_nm(String department_nm) {
		this.department_nm = department_nm;
	}
	@Override
	public String toString() {
		return "[DEPARTMENT ID : " + department_id + "| DEPARTMENT NAME : " + department_nm + "]";
	}
	
}
