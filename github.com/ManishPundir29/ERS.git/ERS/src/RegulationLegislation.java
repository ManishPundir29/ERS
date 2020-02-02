import java.sql.Date;

public class RegulationLegislation {
	
	private int rlId;
	private String rlType;
	private String rlDetails;
	private Date creationDate;
	private String department;
	public RegulationLegislation() {}
	public String getRlType() {
		return rlType;
	}
	public void setRlType(String rlType) {
		this.rlType = rlType;
	}
	public String getRlDetails() {
		return rlDetails;
	}
	public void setRlDetails(String rlDetails) {
		this.rlDetails = rlDetails;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Override
	public String toString() {
		return " [RL Id : "+ rlId +"| RL Type : " + rlType + "| RL Details : " + rlDetails + "| Creation Date : " + creationDate
				+ "| Department : " + department + "]";
	}
	public int getRlId() {
		return rlId;
	}
	public void setRlId(int i) {
		this.rlId = i;
	}
	
}
