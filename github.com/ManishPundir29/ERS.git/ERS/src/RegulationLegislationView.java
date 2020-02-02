import java.sql.Date;

public class RegulationLegislationView {
	
	private int rlId;
	private String rlType;
	private String description;
	private Date creationDate;
	private String department;
	private String status;
	public RegulationLegislationView() {}
	public int getRlId() {
		return rlId;
	}
	public void setRlId(int rlId) {
		this.rlId = rlId;
	}
	public String getRlType() {
		return rlType;
	}
	public void setRlType(String rlType) {
		this.rlType = rlType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "[RL Id  : " + rlId + "| RL Type  : " + rlType + "| Description  : "
				+ description + "| Creation Date  : " + creationDate + "| Department  : " + department + "| Status  : "
				+ status + "]";
	}
	
	
	
}
