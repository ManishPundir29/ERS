import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import oracle.jdbc.OracleTypes;

public class MainClassForERS {
	private static Properties prop;
	  static{
	        InputStream is = null;
	        try {
	            prop = new Properties();
	            is = MainClassForERS.class.getResourceAsStream("jdbc.properties");
	            prop.load(is);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	public static Connection con = getDatabaseConnection();
	public static TextIO textIO = TextIoFactory.getTextIO();
	public static boolean flag = false;

    
  
    public static String getPropertyValue(String key){
        return prop.getProperty(key);
    }
	

	public static void main(String[] args) throws SQLException, IOException, ParseException {
		// make color of text white
	
		loginForm();
	}

	static void loginForm() throws SQLException, IOException, ParseException {

		textIO.getTextTerminal().printf("\nWelcome To The Login Page\n-------------------------\n");
		LoginMaster loginmaster = new LoginMaster();

		LoginChoice loginChoice= textIO.newEnumInputReader(LoginChoice.class).read("\nWHAT YOU WANT TO DO:");
		switch (loginChoice) {
		case LOGIN:
			login(loginmaster);
			break;

		case EXIT:
			textIO.getTextTerminal().dispose();
			break;
			
		default:
			break;
		}
		

	}
	private static void login(LoginMaster loginmaster) throws SQLException, IOException, ParseException {
		//textIO.getTextTerminal().println("*************************************************************");
		textIO.getTextTerminal().println("-------------------------");
		textIO.getTextTerminal().println("   Login As Admin/User   ");
		textIO.getTextTerminal().println("-------------------------");
		for (;;) {

			String user = textIO.newStringInputReader().withDefaultValue("ADMIN/USER").read("User ID");
			String password = textIO.newStringInputReader().withMinLength(6).withInputMasking(true).read("Password");

			flag = checkUserIsValid(user, password, loginmaster);
			if (flag == true) {
				if (loginmaster.getRole().equalsIgnoreCase("Admin")) {
					HomePageForAdmin.homePageForAdmin(loginmaster, textIO);
				} else
					homePage(loginmaster);
				break;
			}
		}
	}

	static void homePage(LoginMaster loginMaster) throws SQLException, IOException, ParseException {

		textIO.getTextTerminal().println("Welcome " + loginMaster.getRole() + ", User ID " + loginMaster.getUserid());

		MainMenuForUser mainMenu = textIO.newEnumInputReader(MainMenuForUser.class).read("-----------------------------\nMain Menu:");
		switch (mainMenu) {
		case EMPLOYEES_MENU:
			goToEmployee(loginMaster);
			break;

		/*
		 * case DEPARTMENT: goToDepartment( loginMaster); break;
		 */
		case REGULATION_AND_LEGISLATION_MENU:
			goToRL(loginMaster);
			break;
			
			
		case COMPLIANCE_TRACKING:
			ComplianceTracking.goToCT( loginMaster, textIO);
			break;

		case LOGOUT:
			textIO.getTextTerminal().print("\nYOU HAVE SUCESSFULLY LOGGED OUT!!\n--------------------------------");
			loginForm();
			break;

		case EXIT:
			textIO.getTextTerminal().dispose();
			break;

		default:
			break;
		}

		// EmployeeChoice empChoice = extracted(user, loginMaster);

		// textIO.getTextTerminal().println("You Selected:"+empChoice);

	}

	static void goToRL(LoginMaster loginMaster) throws SQLException, IOException, ParseException {
		//

		RLChoiceForUser empChoice = textIO.newEnumInputReader(RLChoiceForUser.class)
				.read("\nRegulation/Legislation Menu:");
		switch (empChoice) {

		/*
		 * case CREATE_NEW_RL: createRL(loginMaster); goToRL(loginMaster); break;
		 */

		case VIEW_RL:
			viewRL(loginMaster, textIO);
			goToRL(loginMaster);
			break;
			
		/*
		 * case UPDATE_RL: updateComment(loginMaster, textIO); goToRL(loginMaster);
		 * break;
		 */

		case BACK_TO_MAIN_MENU:
			textIO.getTextTerminal().printf("\nGO BACK TO MAIN MENU!\n--------------------------");
			homePage(loginMaster);
			break;

		case LOGOUT:
			textIO.getTextTerminal().printf("\nYOU HAVE SUCESSFULLY LOGGED OUT!\n----------------------------");
			loginForm();
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
		try (Connection conn = DriverManager.getConnection(getPropertyValue("db.url"), getPropertyValue("db.username"), getPropertyValue("db.password"));
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



	static void viewRL(LoginMaster loginMaster,TextIO textIO) {
	
		String SQL_INSERT="select distinct c.complianceid,c.rltype,c.details as description,c.createdate,d.department_nm,s.comments from statusreport s\r\n" + 
				" left join \r\n" + 
				" COMPLIANCE c on (c.complianceid = s.complianceid)\r\n" + 
				" left join \r\n" + 
				" department d on (c.department_id = d.department_id) where empid=?";
		
		try (Connection conn = DriverManager.getConnection(getPropertyValue("db.url"), getPropertyValue("db.username"), getPropertyValue("db.password"));
				Statement statement = conn.createStatement();
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			preparedStatement.setInt(1, loginMaster.getUserid());
			
			ResultSet resultSet = preparedStatement.executeQuery();

			textIO.getTextTerminal().printf("\nREGULATIONS ASSIGNED TO EMPLOYEES:  \n-------------------------------------");
			textIO.getTextTerminal().printf("\nRegulations assigned to EMPLOYEE ID :: "+loginMaster.getUserid()+"\n--------------------------------------------\n");
			while (resultSet.next()) {
				//textIO.getTextTerminal().println("Regulation/Legislation Type :: "+resultSet.getString("RLTYPE"));	
				//textIO.getTextTerminal().println("Regulation/Legislation Details :: "+resultSet.getString("description"));	
				RegulationLegislationView obj = new RegulationLegislationView();
				obj.setRlId(resultSet.getInt("complianceid"));
				obj.setRlType(resultSet.getString("RLTYPE"));
				obj.setDescription(resultSet.getString("description"));
				obj.setCreationDate(resultSet.getDate("CREATEDATE"));
				obj.setDepartment(resultSet.getString("department_nm"));
				obj.setStatus(resultSet.getString("comments"));
				textIO.getTextTerminal().println(obj.toString());
			}
			gettheStatusReport(loginMaster.getUserid());
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

	private static void gettheStatusReport(int userid) {
		String SQL_INSERT="select s.*,d.department_nm\r\n" + 
				"from statusreport s\r\n" + 
				"left join\r\n" + 
				"department d on (s.department_id = d.department_id) where empid=? order by s.statusrptid";
		
		try (Connection conn = DriverManager.getConnection(getPropertyValue("db.url"), getPropertyValue("db.username"), getPropertyValue("db.password"));
				Statement statement = conn.createStatement();
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			preparedStatement.setInt(1,userid);
			
			ResultSet resultSet = preparedStatement.executeQuery();

			//textIO.getTextTerminal().printf("\nCOMPLIANCE TRACKING\n---------------------");
			textIO.getTextTerminal().printf("\nPREVIOUSLY CREATED STATUS REPORT\n--------------------------------\n");
			while (resultSet.next()) {
				StatusReport obj = new StatusReport();
				obj.setComplianceid(resultSet.getInt("complianceid"));
				obj.setStatusrpid(resultSet.getInt("statusrptid"));
				obj.setEmpid(resultSet.getInt("empid"));
				obj.setComment(resultSet.getString("comments"));
				obj.setCreatedDate(resultSet.getDate("CREATEDATE"));
				obj.setDepartment(resultSet.getString("department_nm"));
				textIO.getTextTerminal().println(obj.toString());
			}
			
		
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

	/*
	 * private static void viewRL(LoginMaster loginMaster) {
	 * 
	 * // TODO Auto-generated method stub String SQL_INSERT =
	 * "select * from COMPLIANCE";
	 * 
	 * try (Connection conn =
	 * DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "empdb",
	 * "root"); Statement statement = conn.createStatement(); PreparedStatement
	 * preparedStatement = conn.prepareStatement(SQL_INSERT)) {
	 * 
	 * ResultSet resultSet = preparedStatement.executeQuery();
	 * 
	 * textIO.getTextTerminal().println("Heres the COMPLIANCE list..........");
	 * 
	 * while (resultSet.next()) {
	 * 
	 * 
	 * long id = resultSet.getLong("DEPARTMENT_ID"); String name =
	 * resultSet.getString("DEPARTMENT_NM"); Str
	 * 
	 * 
	 * RegulationLegislation obj = new RegulationLegislation();
	 * obj.setRlType(resultSet.getString("RLTYPE"));
	 * obj.setRlDetails(resultSet.getString("DETAILS"));
	 * obj.setCreationDate(resultSet.getDate("CREATEDATE"));
	 * obj.setDepartment(String.valueOf(resultSet.getInt("DEPARTMENT_ID")));
	 * textIO.getTextTerminal().println(obj.toString());
	 * 
	 * } textIO.getTextTerminal().println("Heres the COMPLIANCE list.........."); }
	 * catch (SQLException e) { System.err.format("SQL State: %s\t%s",
	 * e.getSQLState(), e.getMessage()); e.printStackTrace();
	 * textIO.getTextTerminal()
	 * .printf("Something went wrong while getting details of all COMPLIANCE.. Try again..\n"
	 * ); // return false; } catch (Exception e) { e.printStackTrace();
	 * textIO.getTextTerminal()
	 * .printf("Something went wrong while getting details of all COMPLIANCE.. Try again..\n"
	 * ); // return false; } }
	 */

	private static void goToEmployee(LoginMaster loginMaster) throws SQLException, IOException, ParseException {
		// EmployeeChoice empChoice = extracted(user, loginMaster);
		EmployeeChoiceForUser empChoice = textIO.newEnumInputReader(EmployeeChoiceForUser.class)
				.read("\nEmployee Menu:");
		switch (empChoice) {
		
		case VIEW_EMPLOYEES:
			textIO.getTextTerminal().printf("\nDISPLAYING EMPLOYEES LIST:\n--------------------------\n");
			displayEmployee();
			textIO.getTextTerminal().printf("\nBACK TO EMPLOYEES MENU:\n--------------------------\n");
			goToEmployee(loginMaster);
			break;

			case BACK_TO_MAIN_MENU:
				textIO.getTextTerminal().printf("\nBACK TO MAIN MENU:\n---------------------\n");
				homePage(loginMaster);
				break;
	
			case LOGOUT:
				textIO.getTextTerminal().println("\nYOU HAVE SUCESSFULLY LOGGED OUT!!\n-------------------------------\n");
				loginForm();
				break;
	
			case EXIT:
				textIO.getTextTerminal().dispose();
				break;

		default:
			textIO.getTextTerminal().println("Invalid Input.. Try again");
			homePage(loginMaster);
			break;
		}

	}

	private static void displayEmployee() throws SQLException {
		// show emp details

		String runSP = "{ call getAllEmp_sp (?) }";

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
				Statement statement = conn.createStatement();
				CallableStatement callableStatement = conn.prepareCall(runSP)) {

			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

			callableStatement.execute();

			ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
			while (resultSet.next()) {
				Employee obj = new Employee();
				obj.setEmpid(resultSet.getInt("empid"));
				obj.setFirstname(resultSet.getString("firstname"));
				obj.setLastname(resultSet.getString("lastname"));
				obj.setDob(resultSet.getDate("dob"));
				obj.setEmail(resultSet.getString("email"));
				obj.setDepartment(resultSet.getString("DEPARTMENT_NM"));
				// System.out.println(resultSet.getMetaData().getColumnName(6));
				textIO.getTextTerminal().println(obj.toString());
			}

			// ResultSetMetaData rsmd = rs.getMetaData();
			// String name = rsmd.getColumnName(1);

		} catch (SQLException e) {
			System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("Something went wrong while getting details of all employees.. Try again..\n");
			// return false;
		} catch (Exception e) {
			e.printStackTrace();
			textIO.getTextTerminal().printf("Invalid Userid/Password.. Try again..\n");
			// return false;
		}

	}

	private static void editGivenEmployee() {
		Employee emp = new Employee();
		// make the database connection
		emp.setEmpid(textIO.newIntInputReader().read("Employee ID"));

		textIO.getTextTerminal().printf("Fetching the employee detail of empid:" + emp.getEmpid());

	}

	// check for names
	// validate first name
	public static boolean firstName(String firstName, TextIO io) {
		String regexUserName = "^[A-Za-z\\s]+$";
		if (firstName.trim().matches(regexUserName))
			return true;
		else
			io.getTextTerminal().println("Invalid First Name.. Enter Again..");
		return false;
	}

	// validate last name
	public static boolean lastName(String lastName, TextIO io) {
		String regexUserName = "^[A-Za-z\\s]+$";
		if (lastName.trim().matches(regexUserName))
			return true;
		else
			io.getTextTerminal().println("Invalid Last Name.. Enter Again..");
		return false;
	}

	/*
	 * private static int addEmployee(String user, LoginMaster loginMaster) throws
	 * SQLException, IOException, ParseException {
	 * 
	 * int i=-1;
	 * 
	 * Employee emp = new Employee(); // make the database connection
	 * 
	 * do { emp.setFirstname(textIO.newStringInputReader() .read("First Name"));
	 * }while(!firstName(emp.getFirstname().trim(),textIO));
	 * 
	 * do { emp.setLastname( textIO.newStringInputReader() .read("Last Name"));
	 * }while(!lastName(emp.getLastname().trim(),textIO));
	 * 
	 * 
	 * String date=null; do{ date=
	 * textIO.newStringInputReader().withDefaultValue("(yyyy-MM-dd)") .read("DOB");
	 * }while(!Example.validateJavaDate(date,textIO) ||
	 * (!AgeClass.calculateAge(date,LocalDate.now(),textIO)) );
	 * 
	 * 
	 * emp.setDob(convertStringIntoDate(date));
	 * 
	 * 
	 * do { emp.setEmail(textIO.newStringInputReader() .read("Email"));
	 * }while(!CheckEmailValidation.isValid(emp.getEmail().trim()));
	 * 
	 * //get the existed department name from datatabase and show them and here than
	 * employee will choose department from the options... long deptId=
	 * getAllDepartname();
	 * 
	 * 
	 * 
	 * emp.setDepartment(textIO.newStringInputReader() .read("Department"));
	 * 
	 * 
	 * FormSubmitOrCancel empChoice
	 * =textIO.newEnumInputReader(FormSubmitOrCancel.class).read(
	 * "-----------------------"); switch (empChoice) { case SUBMIT:
	 * saveEmloyee(emp,(int)deptId, textIO);
	 * textIO.getTextTerminal().printf("Sucessfully added a new emplyee..");
	 * addEmployee(user,loginMaster); break;
	 * 
	 * case CANCEL: addEmployee(user,loginMaster); break;
	 * 
	 * case GO_BACK_TO_HOME_PAGE: homePage( loginMaster); break;
	 * 
	 * case LOGOUT:
	 * textIO.getTextTerminal().println("You have successfully logged out!");
	 * loginForm(); break;
	 * 
	 * case EXIT: textIO.dispose(); break;
	 * 
	 * default: break; }
	 * 
	 * 
	 * //textIO.getTextTerminal().printf("Invalid Userid/Password.. Try again..\n");
	 * return i; }
	 */

	static Date convertStringIntoDate(String date1) {
		// String startDate="12-31-2014";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date;
		try {
			date = sdf1.parse(date1);
			java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
			return sqlStartDate;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	static long getAllDepartname() {
		long i = 0;
		List<Department> list = new ArrayList<>();

		String SQL_INSERT = "select * from department";

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
				Statement statement = conn.createStatement();
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			textIO.getTextTerminal().printf("\nAVAILABLE DEPARTMENTS:\n---------------------\n");

			while (resultSet.next()) {

				long id = resultSet.getLong("DEPARTMENT_ID");
				String name = resultSet.getString("DEPARTMENT_NM");

				Department obj = new Department();
				obj.setDepartment_id(id);
				obj.setDepartment_nm(name);
				list.add(obj);
				// textIO.getTextTerminal().println(obj.toString());

			}

			int index = 0;

			for (Department department : list) {
				textIO.getTextTerminal().println(++index + " : " + department.getDepartment_nm());
			}

			boolean flag = true;
			do {
				long choose = textIO.newIntInputReader().read("\nChoose Department Name:");
				index = 0;
				for (Department department : list) {
					if (++index == choose) {
						flag = false;
						return department.getDepartment_id();
					}
				}
				textIO.getTextTerminal().printf("\nWrong Options Choose Again\n");

			} while (flag);

		} catch (SQLException e) {
			System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("\nSomething went wrong while getting details of all department.. Try again..\n");
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("\nSomething went wrong while getting details of all department.. Try again..\n");
			return 0;
		}
		return 0;

	}

	static void saveEmloyee(Employee emp, int deptId, TextIO textIO2) throws SQLException {
		int i;

	
		PreparedStatement stmt = con.prepareStatement(
				"insert into employees (empid,firstname,lastname,dob,email,department_id) values (EMPLOYEES_SEQ.NEXTVAL,?,?,?,?,?)");
		stmt.setString(1, emp.getFirstname());
		stmt.setString(2, emp.getLastname());
		stmt.setDate(3, emp.getDob());
		// stmt.setString(3,emp.getDob().toString());
		stmt.setString(4, emp.getEmail());
		stmt.setInt(5, deptId);// 1 specifies the first parameter in the query

		i = stmt.executeUpdate();
		//textIO.getTextTerminal().printf(i + " records inserted");
	}

	private static boolean checkUserIsValid(String userid, String password, LoginMaster loginMaster)
			throws SQLException {
		// make the database connection

		// Statement stmt=con.createStatement();
		// sql="select * from login_master where userid=? and password=?";

		String runSP = "{ call getUserDetails_sp(?,?,?) }";

		try (Connection conn = DriverManager.getConnection(getPropertyValue("db.url"), getPropertyValue("db.username"), getPropertyValue("db.password"));
				Statement statement = conn.createStatement();
				CallableStatement callableStatement = conn.prepareCall(runSP)) {

			callableStatement.registerOutParameter(1, Types.BIGINT);
			callableStatement.registerOutParameter(2, Types.VARCHAR);
			callableStatement.registerOutParameter(3, Types.VARCHAR);

			callableStatement.setInt(1, Integer.parseInt(userid));
			// run it
			callableStatement.executeUpdate();
			loginMaster.setUserid(callableStatement.getInt(1));
			loginMaster.setPassword(callableStatement.getString(2));
			loginMaster.setRole(callableStatement.getString(3));
			System.out.println(loginMaster);

			if (loginMaster.getPassword().equals(password)) {
				textIO.getTextTerminal().println("\nLOGGED IN SUCESSFULLY!\n-----------------------");
				return true;
			} else {
				textIO.getTextTerminal().printf("\nInvalid Userid/Password.. Try again..\n------------------------\n");
				return false;
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
			textIO.getTextTerminal().printf("\nInvalid Userid/Password.. Try again..\n------------------------\n");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			textIO.getTextTerminal().printf("\nInvalid Userid/Password.. Try again..\n------------------------\n");
			return false;
		}

	}

	public static Connection getDatabaseConnection() {
		Connection conn = null;
		try {
			Class.forName(getPropertyValue("db.driver"));
			conn = DriverManager.getConnection(getPropertyValue("db.url"), getPropertyValue("db.username"),  getPropertyValue("db.password"));
		} catch (Exception e) {
			e.printStackTrace();
			return conn;
		}
		return conn;
	}

}
