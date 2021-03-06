import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.beryx.textio.TextIO;

public class RLForm {

	
	static void goToRL(LoginMaster loginMaster,TextIO textIO) throws SQLException, IOException, ParseException {
		//

		RLChoiceForAdmin empChoice = textIO.newEnumInputReader(RLChoiceForAdmin.class)
				.read("\nRegulation/Legislation MENU:");
		switch (empChoice) {

		
		case CREATE_NEW_RL: 
			createRL(loginMaster,textIO);
			goToRL(loginMaster, textIO); break;
		 

		case VIEW_RL:
			viewRL(loginMaster, textIO);
			goToRL(loginMaster, textIO);
			break;
			
		case UPDATE_RL:
			updateComment(loginMaster, textIO);
			goToRL(loginMaster, textIO);
			break;

		case BACK_TO_MAIN_MENU:
			HomePageForAdmin.homePageForAdmin(loginMaster, textIO);
			break;

		case LOGOUT:
			textIO.getTextTerminal().printf("\nYOU HAVE SUCESSFULLY LOGGED OUT!\n----------------------------");
			MainClassForERS.loginForm();
			break;

		case EXIT:
			textIO.getTextTerminal().dispose();
			break;

		default:
			break;
		}

	}

	private static void updateComment(LoginMaster loginMaster, TextIO textIO) {
		// select the regulation assigned to them and then update the comment 


		String SQL_INSERT="select c.complianceid,c.rltype,c.details as description,c.createdate,d.department_nm,s.comments,s.empid from statusreport s\r\n" + 
				" left join \r\n" + 
				" COMPLIANCE c on (c.complianceid = s.complianceid)\r\n" + 
				" left join \r\n" + 
				" department d on (c.department_id = d.department_id) order by s.empid ";
		
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
			updatecommentanddatebyempid(empid, textIO, loginMaster);
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

	private static void updatecommentstatusreportid(int empid,int id, TextIO textIO,LoginMaster loginMaster) throws SQLException, IOException, ParseException {
		
		StatusReportCommentChoose reportCommentChoose = textIO.newEnumInputReader(StatusReportCommentChoose.class).read("\nUPDATE COMMENTS AND DATE HERE:");
		switch (reportCommentChoose) {
		case UPDATE_COMMENT:
			updateStatus(id,loginMaster, textIO);
			goToRL(loginMaster, textIO);
		//	updatecommentanddatebyempid(empid, textIO, loginMaster);		
			break;
			
		case UPDATE_DATE:
			updateDate(id,loginMaster, textIO);
			goToRL(loginMaster, textIO);
			//updatecommentanddatebyempid(empid, textIO, loginMaster);
			break;

		case BACK_TO_MAIN_MENU:
			MainClassForERS.homePage(loginMaster);
			break;

		case LOGOUT:
			textIO.getTextTerminal().printf("\nYOU HAVE SUCESSFULLY LOGGED OUT!\n----------------------------");
			MainClassForERS.loginForm();
			break;

		case EXIT:
			textIO.getTextTerminal().dispose();
			break;
		default:
			break;
		}
		
		
		
	}

	private static void updateDate(int id, LoginMaster loginMaster, TextIO textIO) {
		String str=null;
		do{
			str= textIO.newStringInputReader().withDefaultValue("(yyyy-MM-dd)")
		        .read("Created Date");
		}while(!Example.validateJavaDate(str,textIO)) ;
		
		
		java.sql.Date date1 = MainClassForERS.convertStringIntoDate(str);
			
		System.out.println(date1+"  :: "+id);
	
		// TODO Auto-generated method stub
				String SQL_UPDATE="update statusreport set createdate=? where statusrptid=?";
				
				
				try (Connection conn = DriverManager.getConnection(MainClassForERS.getPropertyValue("db.url"),MainClassForERS.getPropertyValue("db.username"),MainClassForERS.getPropertyValue("db.password"));
						Statement statement = conn.createStatement();
						PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE)) {
					
					 preparedStatement.setDate(1,date1);
			            preparedStatement.setInt(2,id);
			            

			            int row = preparedStatement.executeUpdate();

			            if(row==1) {
			            	textIO.getTextTerminal().printf("\nCREATED DATE HAS BEEN UPDATED SUCESSFULLY\n---------------------------------------"); 
			            }
			            else {
			 	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");  
			 				//return false;
			            }
			            // rows affected
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

	private static void updateStatus(int id, LoginMaster loginMaster, TextIO textIO) {

		String status="";
		
			status = textIO.newStringInputReader()
			        .read("Comment");
			
	
		// TODO Auto-generated method stub
				String SQL_UPDATE="update statusreport set comments=? where statusrptid=?;";
				
				
				try (Connection conn = DriverManager.getConnection(MainClassForERS.getPropertyValue("db.url"),MainClassForERS.getPropertyValue("db.username"),MainClassForERS.getPropertyValue("db.password"));
						Statement statement = conn.createStatement();
						PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE)) {
					
					 preparedStatement.setString(1,status);
			            preparedStatement.setInt(2,id);
			            

			            int row = preparedStatement.executeUpdate();

			            if(row==1) {
			            	textIO.getTextTerminal().printf("\nCOMMENT HAS BEEN UPDATED SUCESSFULLY\n--------------------------------"); 
			            }
			            else {
			 	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");  
			 				//return false;
			            }
			            // rows affected
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

	private static void createRL(LoginMaster loginMaster,TextIO textIO) throws ParseException {
		// Admin should allow creation of a new Regulation/Legislation document.
		RegulationLegislation regulationLegislation = new RegulationLegislation();

		regulationLegislation.setRlType(textIO.newStringInputReader().read("RL Type"));

		// mandatoty field
		do {
			regulationLegislation.setRlDetails(textIO.newStringInputReader().read("RL Details"));
		} while (regulationLegislation.getRlDetails().isEmpty());
		// mandatoty field
		String date=null;
		do{
		date = textIO.newStringInputReader().read("Creation Date (eg. yyyy/MM/dd)");
		}while(!Example.validateJavaDate(date,textIO));
		
		
		regulationLegislation.setCreationDate(MainClassForERS.convertStringIntoDate(date));
		
		//get the existed department name from datatabase and show them and here than employee will choose department from the options...
		int deptId= (int)MainClassForERS.getAllDepartname();
		//regulationLegislation.setDepartment(textIO.newStringInputReader().read("Department"));

		// save into the COMPLIANCE
		String SQL_INSERT = "insert into COMPLIANCE values (COMPLIANCE_SEQ.NEXTVAL,?,?,sysdate,?)";

		try (Connection conn = DriverManager.getConnection(MainClassForERS.getPropertyValue("db.url"),MainClassForERS.getPropertyValue("db.username"),MainClassForERS.getPropertyValue("db.password"));
				Statement statement = conn.createStatement();
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			preparedStatement.setString(1, regulationLegislation.getRlType());
			preparedStatement.setString(2, regulationLegislation.getRlDetails());
			preparedStatement.setDate(3, regulationLegislation.getCreationDate());
			preparedStatement.setInt(3, deptId);

			int row = preparedStatement.executeUpdate();

			// rows affected
			System.out.println(row); // 1

			if (row == 1) {
				textIO.getTextTerminal().printf("\nNew Regulation/Legislation has been created sucessfully\n--------------------------------------------------\n");
			} else {

				textIO.getTextTerminal().printf("\nProblem occured while adding Regulation/Legislation.. Try again..\n");
				// return false;
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
			textIO.getTextTerminal().printf("\nProblem occured while adding Regulation/Legislation.. Try again..\n");
			// return false;
		} catch (Exception e) {
			e.printStackTrace();
			textIO.getTextTerminal().printf("\nProblem occured while adding Regulation/Legislation.. Try again..\n");
			// return false;
		}

	}

	static void viewRL(LoginMaster loginMaster,TextIO textIO) {

		
		String runFunction = "{?= call getAllRL_func()}";

        try (Connection conn = DriverManager.getConnection(MainClassForERS.getPropertyValue("db.url"),MainClassForERS.getPropertyValue("db.username"),MainClassForERS.getPropertyValue("db.password"));
             Statement statement = conn.createStatement();
             CallableStatement cs = conn.prepareCall(runFunction);
        ) {

            // We must be inside a transaction for cursors to work.
            conn.setAutoCommit(false);

            // register output
            
            cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

            // run function
            cs.execute();
            // get refcursor and convert it to ResultSet
            ResultSet resultSet = (ResultSet) cs.getObject(1);
            
            textIO.getTextTerminal().println("\nCOMPLIANCE DETAILS\n--------------------\n");
            while (resultSet.next()) {
            	ComplianceDetails complianceDetails = new ComplianceDetails();
                complianceDetails.setCompliance_id(resultSet.getInt("complianceid"));
                complianceDetails.setDepartment_id(resultSet.getInt("department_id"));
                complianceDetails.setDepartment_nm(resultSet.getString("department_nm"));
                complianceDetails.setRltype(resultSet.getString("rltype"));
                complianceDetails.setDetails(resultSet.getString("details"));
                complianceDetails.setCreateddate(resultSet.getDate("createdate"));
                complianceDetails.setEmpcount(resultSet.getInt("empcount"));
                complianceDetails.setStatuscount(resultSet.getInt("statuscount"));
                textIO.getTextTerminal().println(complianceDetails.toString());
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            textIO.getTextTerminal()
			.printf("\nSomething went wrong while getting details of all COMPLIANCE.. Try again..\n");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            textIO.getTextTerminal()
			.printf("\nSomething went wrong while getting details of all COMPLIANCE.. Try again..\n");
        }
		
		
		

	}
	
	public static void updatecommentanddatebyempid(int empid,TextIO textIO,LoginMaster loginMaster) {
		String SQL_INSERT=" select s.*,d.department_nm from statusreport s left join compliance c on (c.complianceid = s.complianceid) \r\n" + 
				" left join\r\n" + 
				" department d on (d.department_id = s.department_id) where s.empid=?";
		
		List<Integer> list = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(MainClassForERS.getPropertyValue("db.url"),MainClassForERS.getPropertyValue("db.username"),MainClassForERS.getPropertyValue("db.password"));
				Statement statement = conn.createStatement();
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {
			
			preparedStatement.setInt(1, empid);

			ResultSet resultSet = preparedStatement.executeQuery();
			list.clear();
			textIO.getTextTerminal().println("\nSTATUS REPORT OF EMPLOYEE ID :: "+empid+"\n------------------------------------");
			while (resultSet.next()) {
				StatusReport obj = new StatusReport();
				obj.setComplianceid(resultSet.getInt("complianceid"));
				obj.setStatusrpid(resultSet.getInt("statusrptid"));
				obj.setCreatedDate(resultSet.getDate("CREATEDATE"));
				obj.setDepartment(resultSet.getString("department_nm"));
				obj.setComment(resultSet.getString("comments"));
				obj.setEmpid(resultSet.getInt("empid"));
				textIO.getTextTerminal().println(obj.toString());
				list.add(resultSet.getInt("statusrptid"));
			}
			
			int id = 0;
			boolean flag = true;
			do {
				long choose = textIO.newIntInputReader().read("\nSELECT THE STATUS REPORT ID TO UPDATE THE COMMENTS AND DATE:");
				
				for (Integer emp : list) {
					if (emp == (int)choose) {
						id = emp;
						flag = false;
					}
				}
				if(flag==true)
				textIO.getTextTerminal().printf("\nWRONG CHOICE. CHOOSE AGAIN...");

			} while (flag);
			
			//get the status report of selected empid
			updatecommentstatusreportid(empid,id,textIO,loginMaster);
			
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
	
}
