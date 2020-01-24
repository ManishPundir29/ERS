import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class MainClassForERS {
	
	public static Scanner sc=new Scanner(System.in);
	
	public static  String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
	public static  String username = "empdb";
	public static  String password = "root";
	public static Connection con = getDatabaseConnection();
	public static TextIO textIO = TextIoFactory.getTextIO();
	public static  String sqlForGetUserDetail ="CALL `ers`.`getUserDetails_sp`(?, ?,?)";
	public static boolean flag=false;
	
	public static void main(String[] args) throws SQLException, IOException {
		textIO.getTextTerminal().println("Welcome To The Login Page...");
		loginForm();
	}

	private static void loginForm() throws SQLException, IOException {
		// login form
		
		BufferedReader standardInput  = new BufferedReader(new InputStreamReader(System.in));
	 
		
		LoginMaster loginmaster = new LoginMaster();
		
		login(loginmaster);
		
		
	}

	private static void login(LoginMaster loginmaster) throws SQLException, IOException {
		for(;;) {
			
			
			String user = textIO.newStringInputReader()
			        .withDefaultValue("admin/user")
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

	private static void homePage(String user,LoginMaster loginMaster) throws SQLException, IOException {

		textIO.getTextTerminal().println("=================================================================================");
		textIO.getTextTerminal().println("Welcome "+loginMaster.getRole()+", User ID "+loginMaster.getUserid());
		
		textIO.getTextTerminal().println("MENU=>");
		
		MainMenu mainMenu =textIO.newEnumInputReader(MainMenu.class).read("----------Main Menu------");
		switch (mainMenu) {
		case EMPLOYEE:
			goToEmployee(user, loginMaster);
			break;

		case DEPARTMENT:
			goToDepartment(user, loginMaster);
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

	private static void goToDepartment(String user, LoginMaster loginMaster) {
		
		DepartmentChoice empChoice =textIO.newEnumInputReader(DepartmentChoice.class).read("----------Department Form------");
		
	}

	private static void goToEmployee(String user, LoginMaster loginMaster) throws SQLException, IOException {
		//EmployeeChoice empChoice = extracted(user, loginMaster);
		EmployeeChoice empChoice =textIO.newEnumInputReader(EmployeeChoice.class).read("----------Employee Form------");
		switch (empChoice) {
		case ADD_EMPLOYEE:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForAdd(user,loginMaster);
			break;

		case EDIT_EMPLOYEE:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForEdit(user,loginMaster);
			break;

		case DELETE_EMPLOYEE:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForDelete(user,loginMaster);
			break;
		
	
		case VIEW_EMPLOYEES:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForView();
			goToEmployee(user, loginMaster);
			break;
	
		case BACK_TO_HOME_PAGE:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
			homePage(user, loginMaster);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().println("you have securly logged out");
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

		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery("select * from employees");  
		if(rs!=null) {
			textIO.getTextTerminal().println("Emp ID\tFirst Name\tLast Name\tDOB\t\tEmail\tDept ID");	
		}
		
		while(rs.next())  { 
			textIO.getTextTerminal().println(rs.getInt("empid")+"\t"+rs.getString("firstname")+"\t\t"+rs.getString("lastname")+"\t\t"+rs.getString("dob").substring(0,10)+"\t"+rs.getString("email")+"\t"+rs.getInt("department_id"));	
		}
		
		
	}

	private static void doYourTaskForDelete(String user, LoginMaster loginMaster) {
		// TODO Auto-generated method stub
		
	}

	private static void doYourTaskForEdit(String user, LoginMaster loginMaster) {
		// TODO Auto-generated method stub
		
	}

	private static int doYourTaskForAdd(String user, LoginMaster loginMaster) throws SQLException {
		
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
		emp.setDepartment(textIO.newIntInputReader()
		        .read("Department"));
		
		FormSubmitOrCancel empChoice =textIO.newEnumInputReader(FormSubmitOrCancel.class).read("-----------------------");
		switch (empChoice) {
		case SUBMIT:
			saveEmloyee(emp);
			doYourTaskForView();
			break;

		case CANCEL:
			doYourTaskForAdd(user,loginMaster);
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
		stmt.setInt(5,emp.getDepartment());//1 specifies the first parameter in the query  
		
		 i=stmt.executeUpdate();  
		System.out.println(i+" records inserted");
	}

	private static boolean checkUserIsValid(String userid, String password,LoginMaster loginMaster) throws SQLException {
		// make the database connection
	
		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery("select * from login_master where userid="+Integer.parseInt(userid)+" and password='"+password+"'");  
		
		while(rs.next())  {
			loginMaster.setUserid(rs.getInt(1));
			loginMaster.setPassword(rs.getString(2));
			loginMaster.setRole(rs.getString(3));
		textIO.getTextTerminal().printf("Thanks %s, you are logged in.\n",rs.getInt(1));  
		return true;
		}
		textIO.getTextTerminal().printf("Invalid Userid/Password.. Try again..\n");  
		return false;
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
