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
	
	public static  String jdbcUrl = "jdbc:mysql://localhost:3306/empdb";
	public static  String username = "root";
	public static  String password = "root";
	public static Connection con = getDatabaseConnection();
	public static TextIO textIO = TextIoFactory.getTextIO();
	public static  String sqlForGetUserDetail ="CALL `empdb`.`getUserDetails_sp`(?, ?,?)";
	public static boolean flag=false;
	
	public static void main(String[] args) throws SQLException, IOException {
		textIO.getTextTerminal().println("Welcome To The Login Page...");
		loginForm();
	}

	private static void loginForm() throws SQLException, IOException {
		// login form
		
		BufferedReader standardInput
	    = new BufferedReader(new InputStreamReader(System.in));
	 
		
		LoginMaster loginmaster = new LoginMaster();
		
		
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

	private static void homePage(String user,LoginMaster loginMaster) throws SQLException {
		TextIO homepage = TextIoFactory.getTextIO();
		homepage.getTextTerminal().println("Welcome "+loginMaster.getRole()+", User ID "+loginMaster.getUserid());
		
		EmployeeChoice empChoice =textIO.newEnumInputReader(EmployeeChoice.class).read("----------Employee Form------");
		switch (empChoice) {
		case ADD:
			homepage.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForAdd();
			break;

		case EDIT:
			homepage.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForEdit();
			break;

		case DELETE:
			homepage.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForDelete();
			break;
		
		case VIEW:
			homepage.getTextTerminal().println("You Selected:"+empChoice);
			doYourTaskForView();
			break;
	
		default:
			homepage.getTextTerminal().println("You Selected:"+empChoice);
			break;
		}
		
		homepage.getTextTerminal().println("You Selected:"+empChoice);
	
		
	}

	private static void doYourTaskForView() throws SQLException {
		//show emp details

		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery("select * from employees");  
		
		while(rs.next())  {
		new DBTablePrinter().printTable(con, "employees");  

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
		return false;
	}
	
	public static Connection getDatabaseConnection() {
		Connection conn =null;
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");  
		conn=DriverManager.getConnection( jdbcUrl,username,password);  
		}catch (Exception e) {
			e.printStackTrace();
			return conn;
		}
		return conn;
	}

}
