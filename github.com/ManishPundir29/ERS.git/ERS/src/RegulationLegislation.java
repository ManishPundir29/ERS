import java.util.Date;

public class RegulationLegislation {

	private String rlType;
	private String rlDetails;
	private String creationDate;
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
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
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
		return "RegulationLegislation [rlType=" + rlType + ", rlDetails=" + rlDetails + ", creationDate=" + creationDate
				+ ", department=" + department + "]";
	}
	
}
