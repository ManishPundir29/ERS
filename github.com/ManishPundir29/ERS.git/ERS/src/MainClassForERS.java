import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import oracle.jdbc.OracleTypes;
import oracle.net.aso.r;

public class MainClassForERS {
	
	public static Scanner sc=new Scanner(System.in);
	
	public static  String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
	public static  String username = "empdb";
	public static  String password = "root";
	public static Connection con = getDatabaseConnection();
	public static TextIO textIO = TextIoFactory.getTextIO();
	public static  String sqlForGetUserDetail ="CALL `ers`.`getUserDetails_sp`(?, ?,?)";
	public static boolean flag=false;
	
	public static void main(String[] args) throws SQLException, IOException, ParseException {
		textIO.getTextTerminal().println("Welcome To The Login Page...");
		loginForm();
	}

	private static void loginForm() throws SQLException, IOException, ParseException {
		// login form
		
		BufferedReader standardInput  = new BufferedReader(new InputStreamReader(System.in));
	 
		
		LoginMaster loginmaster = new LoginMaster();
		
		login(loginmaster);
		
		
	}

	private static void login(LoginMaster loginmaster) throws SQLException, IOException, ParseException {
		for(;;) {
			
			
			String user = textIO.newStringInputReader()
			        .withDefaultValue("ADMIN/USER")
			        .read("User ID");
			String password = textIO.newStringInputReader()
			        .withMinLength(6)
			        .withInputMasking(true)
			        .read("Password");
			

			/*
			 * int age = textIO.newIntInputReader() .withMinVal(13) .read("Age"); 
			 * Month  month = textIO.newEnumInputReader(Month.class).read("What month were you born in?"); 
			 * textIO.getTextTerminal().
			 * printf("User %s is %d years old, was born in %s and has the password %s.\n",
			 * user, age, month, password);
			 */
		    flag=     checkUserIsValid(user,password,loginmaster);
		   if(flag==true) {
			 homePage(user,loginmaster);
			 break;
		   }
		}
	}

	private static void homePage(String user,LoginMaster loginMaster) throws SQLException, IOException, ParseException {

		textIO.getTextTerminal().print("=================================================================================");
		textIO.getTextTerminal().println("Welcome "+loginMaster.getRole()+", User ID "+loginMaster.getUserid());
		
		textIO.getTextTerminal().print("MENU=>");
		
		MainMenu mainMenu =textIO.newEnumInputReader(MainMenu.class).read("----------Main Menu------");
		switch (mainMenu) {
		case EMPLOYEE:
			goToEmployee(user, loginMaster);
			break;

		case DEPARTMENT:
			goToDepartment(user, loginMaster);
			break;
			
		case REGULATION_AND_LEGISLATION:
			goToRL( loginMaster);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().println("You have successfully logged out!");
			loginForm();
			break;
			
		case EXIT:
			textIO.getTextTerminal().dispose();
			break;
			
		default:
			break;
		}
		
		
		//EmployeeChoice empChoice = extracted(user, loginMaster);
		
	//	textIO.getTextTerminal().println("You Selected:"+empChoice);
	
		
	}

	private static void goToRL(LoginMaster loginMaster) throws SQLException, IOException, ParseException {
		//
		
		
		
		RLChoose empChoice =textIO.newEnumInputReader(RLChoose.class).read("---------- Regulation/Legislation ------");
		switch (empChoice) {
		
		case CREATE_NEW_RL:
			createRL(loginMaster);
			goToRL(loginMaster);
			break;
			
		case VIEW_RL:
			viewRL(loginMaster);
			goToRL(loginMaster);
			break;
		
		case LOGOUT:
			textIO.getTextTerminal().println("You have successfully logged out!");
			loginForm();
			break;

		case EXIT:
			textIO.getTextTerminal().dispose();
			break;
			
		default:
			break;
		}
		
	}

	private static void viewRL(LoginMaster loginMaster) {
		
		// TODO Auto-generated method stub
		String SQL_INSERT = "select * from COMPLIANCE";
		
		 try (Connection conn = DriverManager.getConnection(
				 "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
	             Statement statement = conn.createStatement();
				 PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			 ResultSet resultSet = preparedStatement.executeQuery();
			 
			 textIO.getTextTerminal().println("Heres the COMPLIANCE list..........");
			 
			 while (resultSet.next()) {

				/*
				 * long id = resultSet.getLong("DEPARTMENT_ID"); String name =
				 * resultSet.getString("DEPARTMENT_NM"); Str
				 */

	                RegulationLegislation obj = new RegulationLegislation();
	                obj.setRlType(resultSet.getString("RLTYPE"));
	                obj.setRlDetails(resultSet.getString("DETAILS"));
	                obj.setCreationDate(resultSet.getDate("CREATEDATE").toString());
	                obj.setDepartment(String.valueOf(resultSet.getInt("DEPARTMENT_ID")));
	                textIO.getTextTerminal().println(obj.toString());
	                
	            }
	            
	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Something went wrong while getting details of all COMPLIANCE.. Try again..\n");  
				//return false;
	        } catch (Exception e) {
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Something went wrong while getting details of all COMPLIANCE.. Try again..\n");  
				//return false;
	        }	
	}

	private static void createRL(LoginMaster loginMaster) throws ParseException {
		//  Admin should allow creation	of a new Regulation/Legislation document.
		RegulationLegislation regulationLegislation = new RegulationLegislation();
		
		regulationLegislation.setRlType(textIO.newStringInputReader().read("RL Type"));
		regulationLegislation.setRlDetails(textIO.newStringInputReader().read("RL Details"));
		regulationLegislation.setCreationDate(textIO.newStringInputReader().read("Creation Date (yyyy/MM/dd)"));
		regulationLegislation.setDepartment(textIO.newStringInputReader().read("Department"));
		
		
		//save into the COMPLIANCE
				String SQL_INSERT = "insert into COMPLIANCE values (COMPLIANCE_SEQ.NEXTVAL,?,?,sysdate,?)";
				
				 try (Connection conn = DriverManager.getConnection(
						 "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
			             Statement statement = conn.createStatement();
						 PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

					 	preparedStatement.setString(1, regulationLegislation.getRlType());
					 	preparedStatement.setString(2, regulationLegislation.getRlDetails());
					 	//preparedStatement.setDate(3, new Da);
					 	preparedStatement.setInt(3, 101);

			            int row = preparedStatement.executeUpdate();

			            // rows affected
			            System.out.println(row); //1
				
			            if(row==1) {
			            	textIO.getTextTerminal().printf("New Regulation/Legislation has been created sucessfully..\n"); 
			            }
			            else {
			            	
			 	            textIO.getTextTerminal().printf("Problem occured while adding Regulation/Legislation.. Try again..\n");  
			 				//return false;
			            }
			         
			            
			        } catch (SQLException e) {
			            System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			            e.printStackTrace();
			            textIO.getTextTerminal().printf("Problem occured while adding Regulation/Legislation.. Try again..\n");  
						//return false;
			        } catch (Exception e) {
			            e.printStackTrace();
			            textIO.getTextTerminal().printf("Problem occured while adding Regulation/Legislation.. Try again..\n");  
						//return false;
			        }
		
	}

	private static void goToDepartment(String user, LoginMaster loginMaster) throws SQLException, IOException, ParseException {
		
		DepartmentChoice empChoice =textIO.newEnumInputReader(DepartmentChoice.class).read("----------Department Form------");
		switch (empChoice) {
		case ADD_DEPARTMENT:
			AddDepartment(user, loginMaster);
			goToDepartment(user, loginMaster);
			break;
			
		case VIEW_DEPARTMENT:
			viewDepartment(user, loginMaster);
			goToDepartment(user, loginMaster);
			break;
			
		case BACK_TO_HOME_PAGE:
			textIO.getTextTerminal().println("Back To Home Page....");
			homePage(user, loginMaster);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().println("You have successfully logged out!");
			loginForm();
			break;
			
		case EXIT:
			textIO.getTextTerminal().dispose();
			break;
			
		default:
			break;
			
		}
		
	}

	private static void viewDepartment(String user, LoginMaster loginMaster) {
		
		//admin can view the department not the user
		//get the list from database
		String SQL_INSERT = "select * from department";
		
		 try (Connection conn = DriverManager.getConnection(
				 "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
	             Statement statement = conn.createStatement();
				 PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			 ResultSet resultSet = preparedStatement.executeQuery();
			 
			 textIO.getTextTerminal().println("Heres the department list..........");
			 
			 while (resultSet.next()) {

	                long id = resultSet.getLong("DEPARTMENT_ID");
	                String name = resultSet.getString("DEPARTMENT_NM");

	                Department obj = new Department();
	                obj.setDepartment_id(id);
	                obj.setDepartment_nm(name);

	                textIO.getTextTerminal().println(obj.toString());
	                
	            }
	            
	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Something went wrong while getting details of all department.. Try again..\n");  
				//return false;
	        } catch (Exception e) {
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Something went wrong while getting details of all department.. Try again..\n");  
				//return false;
	        }
		
	}

	private static void AddDepartment(String user, LoginMaster loginMaster) {
		
		//add here new department 
		
		Department department = new Department();
		
		department.setDepartment_nm(textIO.newStringInputReader().
				read("Department Name"));
		
		
		//save into database
		String SQL_INSERT = "insert into department values (DEPARTMENT_SEQ.NEXTVAL,?)";
		
		 try (Connection conn = DriverManager.getConnection(
				 "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
	             Statement statement = conn.createStatement();
				 PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			 	preparedStatement.setString(1, department.getDepartment_nm());

	            int row = preparedStatement.executeUpdate();

	            // rows affected
	            System.out.println(row); //1
		
	            if(row==1) {
	            	textIO.getTextTerminal().printf("Department has been saved sucessfully..\n"); 
	            }
	            else {
	            	
	 	            textIO.getTextTerminal().printf("Problem occured while adding department name.. Try again..\n");  
	 				//return false;
	            }
	         
	            
	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Something went wrong while adding department.. Try again..\n");  
				//return false;
	        } catch (Exception e) {
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Something went wrong while adding department.. Try again..\n");  
				//return false;
	        }
		
		
		
	}

	private static void goToEmployee(String user, LoginMaster loginMaster) throws SQLException, IOException, ParseException {
		//EmployeeChoice empChoice = extracted(user, loginMaster);
		EmployeeChoice empChoice =textIO.newEnumInputReader(EmployeeChoice.class).read("----------Employee Form------");
		switch (empChoice) {
		case ADD_EMPLOYEE:
			textIO.getTextTerminal().println("Add New Emplyee Here....");
			doYourTaskForAdd(user,loginMaster);
			break;

		case EDIT_EMPLOYEE:
			textIO.getTextTerminal().println("Edit/Update Existed Emplyee Here....");
			doYourTaskForEdit(user,loginMaster);
			break;

		case DELETE_EMPLOYEE:
			textIO.getTextTerminal().println("Delete Employee Data Here.");
			doYourTaskForDelete(user,loginMaster);
			break;
		
	
		case VIEW_EMPLOYEES:
			textIO.getTextTerminal().println("Show all Employee Details here");
			doYourTaskForView();
			goToEmployee(user, loginMaster);
			break;
	
		case BACK_TO_HOME_PAGE:
			textIO.getTextTerminal().println("Back To Home Page....");
			homePage(user, loginMaster);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().println("You have successfully logged out!");
			loginForm();
			break;
			
		case EXIT:
			textIO.getTextTerminal().dispose();
			break;
			
		default:
			textIO.getTextTerminal().println("Invalid Input.. Try again");
			homePage(user, loginMaster);
			break;
		}
		
	}

	private static void doYourTaskForView() throws SQLException {
		//show emp details

		String runSP = "{ call getAllEmp_sp (?) }";
		
		 try (Connection conn = DriverManager.getConnection(
				 "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
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
	                obj.setDob(resultSet.getString("dob").substring(0,11));
	                obj.setEmail(resultSet.getString("email"));
	               obj.setDepartment(resultSet.getString("DEPARTMENT_NM"));
	             //   System.out.println(resultSet.getMetaData().getColumnName(6));
	               textIO.getTextTerminal().println(obj.toString());
	            }

	           // ResultSetMetaData rsmd = rs.getMetaData();
	        //    String name = rsmd.getColumnName(1);
	         
	            
	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Something went wrong while getting details of all employees.. Try again..\n");  
				//return false;
	        } catch (Exception e) {
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Invalid Userid/Password.. Try again..\n");  
				//return false;
	        }
		
		
	}

	private static void doYourTaskForDelete(String user, LoginMaster loginMaster) {
		// TODO Auto-generated method stub
		
	}

	private static void doYourTaskForEdit(String user, LoginMaster loginMaster) throws SQLException, IOException, ParseException {

		doYourTaskForView();
		ChooseForEdit empChoice =textIO.newEnumInputReader(ChooseForEdit.class).read("----------Choose HERE------");
		switch (empChoice) {
		case EMPID:
			editGivenEmployee();
			break;

		case GO_BACK_TO_HOME_PAGE:
			textIO.getTextTerminal().println("Back To Home Page....");
			homePage(user, loginMaster);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().println("You have successfully logged out!");
			loginForm();
			break;
			
		case EXIT:
			textIO.getTextTerminal().dispose();
			break;
			
		default:
			textIO.getTextTerminal().println("Invalid Input.. Try again");
			homePage(user, loginMaster);
			break;
		}
		
	}

	private static void editGivenEmployee() {
		Employee emp = new Employee();
		// make the database connection
		emp.setEmpid(textIO.newIntInputReader()
		        .read("Employee ID"));
		
		textIO.getTextTerminal().printf("Fetching the employee detail of empid:"+emp.getEmpid());
		
	}

	private static int doYourTaskForAdd(String user, LoginMaster loginMaster) throws SQLException, IOException, ParseException {
		
		int i=-1;
		
		Employee emp = new Employee();
		// make the database connection
		emp.setFirstname(textIO.newStringInputReader()
		        .read("First Name"));
		
		emp.setLastname( textIO.newStringInputReader()
		        .read("Last Name"));
		
		emp.setDob(textIO.newStringInputReader()
		        .read("DOB"));
		emp.setEmail(textIO.newStringInputReader()
		        .read("Email"));
		emp.setDepartment(textIO.newStringInputReader()
		        .read("Department"));
		
		FormSubmitOrCancel empChoice =textIO.newEnumInputReader(FormSubmitOrCancel.class).read("-----------------------");
		switch (empChoice) {
		case SUBMIT:
			saveEmloyee(emp);
			textIO.getTextTerminal().printf("Sucessfully added a new emplyee..");
			doYourTaskForAdd(user,loginMaster);
			break;

		case CANCEL:
			doYourTaskForAdd(user,loginMaster);
			break;
			
		case GO_BACK_TO_HOME_PAGE:
			homePage(user, loginMaster);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().println("You have successfully logged out!");
			loginForm();
			break;
			
		case EXIT:
			textIO.dispose();
			break;
			
		default:
			break;
		}
		
	
		//textIO.getTextTerminal().printf("Invalid Userid/Password.. Try again..\n");  
		return i;
	}

	private static void saveEmloyee(Employee emp) throws SQLException {
		int i;
		//Statement stmt=con.createStatement();  
		//ResultSet rs=stmt.executeQuery("insert into employees (firstname,lastname,dob,email,department_id) values (?,?,?,?,?)");  
		PreparedStatement stmt=con.prepareStatement("insert into employees (empid,firstname,lastname,dob,email,department_id) values (EMPLOYEES_SEQ.NEXTVAL,?,?,?,?,?)");  
		stmt.setString(1,emp.getFirstname());
		stmt.setString(2, emp.getLastname());
		stmt.setString(3, emp.getDob());
		stmt.setString(4, emp.getEmail());
	//	stmt.setString(5,emp.getDepartment());//1 specifies the first parameter in the query  
		
		 i=stmt.executeUpdate();  
		 textIO.getTextTerminal().printf(i+" records inserted");
	}

	private static boolean checkUserIsValid(String userid, String password,LoginMaster loginMaster) throws SQLException {
		// make the database connection
	
		//Statement stmt=con.createStatement();  
		//sql="select * from login_master where userid=? and password=?";
		
		String runSP = "{ call getUserDetails_sp(?,?,?) }";
		
		 try (Connection conn = DriverManager.getConnection(
				 "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
	             Statement statement = conn.createStatement();
	             CallableStatement callableStatement = conn.prepareCall(runSP)) {

			 	
			 callableStatement.registerOutParameter(1, Types.BIGINT);
	            callableStatement.registerOutParameter(2,Types.VARCHAR);
	            callableStatement.registerOutParameter(3,Types.VARCHAR);

	            callableStatement.setInt(1, Integer.parseInt(userid));
	            // run it
	          callableStatement.executeUpdate();
	         loginMaster.setUserid(callableStatement.getInt(1));
	          loginMaster.setPassword(callableStatement.getString(2));
	           loginMaster.setRole(callableStatement.getString(3));
	          System.out.println(loginMaster);
	          
	          if(loginMaster.getPassword().equals(password))
	        		{
	        	  textIO.getTextTerminal().println("Logged in sucessfully...");  
	        	  return true;
	          }
	          else {
	        	  textIO.getTextTerminal().printf("Invalid Userid/Password.. Try again..\n");  
					return false;
	          }
	         
	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Invalid Userid/Password.. Try again..\n");  
				return false;
	        } catch (Exception e) {
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("Invalid Userid/Password.. Try again..\n");  
				return false;
	        }
	
	}
	
	public static Connection getDatabaseConnection() {
		Connection conn =null;
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver");  
		conn=DriverManager.getConnection( jdbcUrl,username,password);  
		}catch (Exception e) {
			e.printStackTrace();
			return conn;
		}
		return conn;
	}

}
