import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.beryx.textio.TextIO;

public class ComplianceTracking {

	public static void goToCT(LoginMaster loginMaster, TextIO textIO) throws SQLException, IOException, ParseException {
		//

		RLTrackingChoiceForAdmin empChoice = textIO.newEnumInputReader(RLTrackingChoiceForAdmin.class)
				.read("\nCOMPLIANCE TRACKING MENU:");
		switch (empChoice) {

		
		case VIEW_RL:
			viewCT(loginMaster, textIO);
			goToCT(loginMaster, textIO);
			break;
			
		case UPDATE_RL:
			updateCommentsOnRegulation(loginMaster, textIO);
			goToCT(loginMaster, textIO);
			break;

		case BACK_TO_MAIN_MENU:
			HomePageForAdmin.homePageForAdmin(loginMaster, textIO);
			break;

		case LOGOUT:
			textIO.getTextTerminal().printf("\nYOU HAVE SUCESSFULLY LOGGED OUT!\n----------------------------");
	//		MainClassForERS.loginForm();
			break;

		case EXIT:
			textIO.getTextTerminal().dispose();
			break;

		default:
			break;
		}

	}

	private static void updateCommentsOnRegulation(LoginMaster loginMaster, TextIO textIO) {
		// select the regulation assigned to them and then update the comment 


		String SQL_INSERT="select c.complianceid,c.rltype,c.details as description,c.createdate,d.department_nm,s.comments,s.empid from statusreport s\r\n" + 
				" left join \r\n" + 
				" COMPLIANCE c on (c.complianceid = s.complianceid)\r\n" + 
				" left join \r\n" + 
				" department d on (c.department_id = d.department_id) where empid="+loginMaster.getUserid()+" order by s.empid ";
		
		List<Integer> list = new ArrayList<>();
		int empid = 0;
		try (Connection conn = DriverManager.getConnection(MainClassForERS.getPropertyValue("db.url"),MainClassForERS.getPropertyValue("db.username"),MainClassForERS.getPropertyValue("db.password"));
				Statement statement = conn.createStatement();
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			textIO.getTextTerminal().printf("\nCOMPLIANCE LIST\n--------------\n");

			while (resultSet.next()) {
				if(!list.contains(resultSet.getInt("empid")))
				textIO.getTextTerminal().println("EMPLOYEE ID :: "+resultSet.getInt("empid"));
				RegulationLegislationView obj = new RegulationLegislationView();
				obj.setRlId(resultSet.getInt("complianceid"));
				obj.setRlType(resultSet.getString("RLTYPE"));
				obj.setDescription(resultSet.getString("description"));
				obj.setCreationDate(resultSet.getDate("CREATEDATE"));
				obj.setDepartment(resultSet.getString("department_nm"));
				obj.setStatus(resultSet.getString("comments"));
				textIO.getTextTerminal().println(obj.toString());
				
				list.add(resultSet.getInt("empid"));
			}
			
			
			
			boolean flag = true;
			do {
				long choose = textIO.newIntInputReader().read("---------------------------\nSELECT THE EMPLOYEE ID:");
				
				for (Integer emp : list) {
					if (emp == (int)choose) {
						empid = emp;
						flag = false;
					}
				}
				if(flag==true)
				textIO.getTextTerminal().printf("\nWRONG CHOICE. CHOOSE AGAIN...");

			} while (flag);
			
			//get the status report of selected empid
			RLForm.updatecommentanddatebyempid(empid, textIO, loginMaster);
		} catch (SQLException e) {
			System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("\nSomething went wrong while getting details of all COMPLIANCE.. Try again..\n");
			// return false;
		} catch (Exception e) {
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("\nSomething went wrong while getting details of all COMPLIANCE.. Try again..\n");
			// return false;
		}
	}

	private static void viewCT(LoginMaster loginMaster,TextIO textIO) {
			  String SQL_INSERT="select c.complianceid,c.rltype,c.details as description,c.createdate,d.department_nm,s.comments,s.empid from statusreport s\r\n"
			  + " left join \r\n" +
			  " COMPLIANCE c on (c.complianceid = s.complianceid)\r\n" + " left join \r\n"
			  + " department d on (c.department_id = d.department_id) where empid="+loginMaster.getUserid()+" order by s.empid ";
			  
			  List<Integer> list = new ArrayList<>();
			  
			  try (Connection conn =
			  DriverManager.getConnection(MainClassForERS.getPropertyValue("db.url"),MainClassForERS.getPropertyValue("db.username"),MainClassForERS.getPropertyValue("db.password"));
		      Statement statement = conn.createStatement();
			  PreparedStatement  preparedStatement = conn.prepareStatement(SQL_INSERT)) {
			  
			  ResultSet resultSet = preparedStatement.executeQuery();
			  
			  textIO.getTextTerminal().println("\nCOMPLIANCE TRACKING DETAILS\n--------------------\n");
			  
			  while (resultSet.next()) {
				  if(!list.contains(resultSet.getInt("empid")))
			  textIO.getTextTerminal().println("EMPLOYEE ID :: "+resultSet.getInt("empid")); 
			  RegulationLegislationView obj = new RegulationLegislationView();
			  obj.setRlId(resultSet.getInt("complianceid"));
			  obj.setRlType(resultSet.getString("RLTYPE"));
			  obj.setDescription(resultSet.getString("description"));
			  obj.setCreationDate(resultSet.getDate("CREATEDATE"));
			  obj.setDepartment(resultSet.getString("department_nm"));
			  obj.setStatus(resultSet.getString("comments"));
			  textIO.getTextTerminal().println(obj.toString());
			  
			  list.add(resultSet.getInt("empid")); 
			  } 
			  textIO.getTextTerminal().println(""); 
			  } catch (SQLException e) {
			  System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			  e.printStackTrace(); 
			  textIO.getTextTerminal().printf("Something went wrong while getting details of all COMPLIANCE.. Try again..\n"
			  ); 
			  // return false; } catch (Exception e) { e.printStackTrace();
			  textIO.getTextTerminal().printf("Something went wrong while getting details of all COMPLIANCE.. Try again..\n"
			  ); // return false; }
			 
		
	}

}
}
