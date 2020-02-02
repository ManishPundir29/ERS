import java.sql.Date;

public class Employee {

	private int empid;
	private String firstname;
	private String lastname;
	private Date dob;
	private String email;
	private String department;
	
	public Employee() {}
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "[First Name : " + firstname + "| Last Name : " + lastname + "| Dob : " + dob + "| Email : " + email
				+ "| Department : " + department + "]";
	}

	public String displayEmployeeObject() {
		return "[EMP ID : "+ empid + "| First Name : " + firstname + "| Last Name : " + lastname + "| Dob : " + dob + "| Email : " + email
				+ "| Department : " + department + "]";
	}
	
	public int getEmpid() {
		return empid;
	}

	public void setEmpid(int empid) {
		this.empid = empid;
	}
	
}
