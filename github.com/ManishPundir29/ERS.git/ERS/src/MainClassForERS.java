import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
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
	
	public static  String jdbcUrl = "jdbc:mysql://localhost:3306/ers";
	public static  String username = "root";
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
			doYourTaskForAdd();
			break;

		case EDIT_EMPLOYEE:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForEdit();
			break;

		case DELETE_EMPLOYEE:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForDelete();
			break;
		
	
		case VIEW_EMPLOYEES:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForView();
			break;
	
		case BACK_TO_HOME_PAGE:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
			homePage(user, loginMaster);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().println("You Selected:"+empChoice);
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

	private static void doYourTaskForDelete() {
		// TODO Auto-generated method stub
		
	}

	private static void doYourTaskForEdit() {
		// TODO Auto-generated method stub
		
	}

	private static void doYourTaskForAdd() {
		// TODO Auto-generated method stub
		
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
		Class.forName("com.mysql.jdbc.Driver");  
		conn=DriverManager.getConnection( jdbcUrl,username,password);  
		}catch (Exception e) {
			e.printStackTrace();
			return conn;
		}
		return conn;
	}

}
