import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
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

import oracle.jdbc.OracleTypes;

public class EmployeeForm {

	public static void	goToEmployeeFormPage(LoginMaster loginMaster,TextIO textIO) throws SQLException, IOException, ParseException{
		//EmployeeChoice empChoice = extracted(user, loginMaster);
		EmployeeChoice empChoice =textIO.newEnumInputReader(EmployeeChoice.class).read("---------------------\nEMPLOYEE MENU:");
		switch (empChoice) {
		case ADD_EMPLOYEE:
			textIO.getTextTerminal().printf("\nADD NEW EMPLOYEE HERE\n----------------------\n");
			addEmployee(loginMaster,textIO);
			break;

		case EDIT_EMPLOYEE:
			textIO.getTextTerminal().printf("\nEDIT/UPDATE EXISTED EMPLOYEE HERE\n---------------------------------\n");
			editOrUpdateEmployee(loginMaster,textIO);
			break;

		case DELETE_EMPLOYEE:
			textIO.getTextTerminal().printf("\nDELETE EXISTED EMPLOYEES FROM HERE\n----------------------------------\n");
			deleteSelectedEmployee(loginMaster,textIO);
			break;
		
	
		case VIEW_EMPLOYEES:
			textIO.getTextTerminal().printf("\nCHECK EMPLOYEES DETAILS FROM HERE\n--------------------------------\n");
			displayEmployee(textIO);
			goToEmployeeFormPage(loginMaster,textIO);
			break;
	
		case BACK_TO_MAIN_MENU:
			textIO.getTextTerminal().printf("\nBACK TO MAIN MENU\n-----------------\n");
			HomePageForAdmin.homePageForAdmin(loginMaster,textIO);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().print("\nYOU HAVE SUCESSFULLY LOGGED OUT!\n--------------------------------");
			MainClassForERS.loginForm();
			break;
			
		case EXIT:
			textIO.getTextTerminal().dispose();
			break;
			
		default:
			textIO.getTextTerminal().println("\nENTERED INPUT IS INCORRECT. TRY AGAIN\n");
			HomePageForAdmin.homePageForAdmin(loginMaster,textIO);
			break;
		}
		
	}

	

	private static void deleteSelectedEmployee(LoginMaster loginMaster,TextIO textIO) throws SQLException, IOException, ParseException {
		//first select the employee you want to delete


		
		int empid=0;
		//paste here the code from edit/update
		// show the all employee and ask user to select the employee whom he wants to update
				
		String runSP = "{ call getAllEmp_sp (?) }";
		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
				Statement statement = conn.createStatement();
				CallableStatement callableStatement = conn.prepareCall(runSP)) {

			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

			callableStatement.execute();
			List<Employee> list = new ArrayList<>();
			
			ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
			while (resultSet.next()) {
				Employee obj = new Employee();
				obj.setEmpid(resultSet.getInt("empid"));
				obj.setFirstname(resultSet.getString("firstname"));
				obj.setLastname(resultSet.getString("lastname"));
				obj.setDob(resultSet.getDate("dob"));
				obj.setEmail(resultSet.getString("email"));
				obj.setDepartment(resultSet.getString("DEPARTMENT_NM"));
				list.add(obj);
				 
			}
			int index = 0;
			
			for (Employee emp : list) {
				textIO.getTextTerminal().println(++index + " : " + emp.displayEmployeeObject());
			}

			boolean flag = true;
			do {
				long choose = textIO.newIntInputReader().read("\nSELECT THE EMPLOYEE YOU WANT TO DELETE:");
				index = 0;
				for (Employee emp : list) {
					if (++index == choose) {
						flag = false;
						 empid = emp.getEmpid();
					}
				}
				if(flag==true)
				textIO.getTextTerminal().printf("\nWRONG CHOICE. CHOOSE AGAIN\n---------------------");

			} while (flag);
			System.out.println(empid);
			Character c =new Character('\n');
			
			while(true){
				 if(c == 'n' || c == 'N'){ 
				      break;
				     }
			
			   c = textIO.newCharInputReader().read("\nDO YOU WANT TO CONTINUE Y OR N:");

			   if(c == 'Y' || c == 'y'){ 
			      break;
			     }//else continue to loop on any string ;-)

			}
			
			
			 if(c == 'Y' || c == 'y')
			deleteGivenEmployee(empid,textIO,loginMaster);
			
			chooseoptionsfordeleteemployee(textIO, loginMaster);
		
			
		} catch (SQLException e) {
			System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("\nSOMETHING WENT WRONG... TRY AGAIN\n");
			
		} catch (Exception e) {
			e.printStackTrace();
			textIO.getTextTerminal().printf("\nINVALID INPUT. TRY AGAIN\n");
			
		}
		
	
	}

	private static void chooseoptionsfordeleteemployee(TextIO textIO,LoginMaster loginMaster) throws SQLException, ParseException, IOException {
		//after update the details ask user if he wants to update again the same employee or other employee		
		ChooseForDelete empChoice = textIO.newEnumInputReader(ChooseForDelete.class).read("\n----------------------");
		
		switch (empChoice) {
		case VIEW_EMPLOYEES:
			textIO.getTextTerminal().printf("\nEMPLOYEES LIST\n--------------\n");
			displayEmployee(textIO);
			textIO.getTextTerminal().print("\nDELETE EXISTED EMPLOYEES FROM HERE\n------------------------------");
			deleteSelectedEmployee(loginMaster, textIO);
			break;
			
		case DELETE_OTHER_EMPLOYEE:
			textIO.getTextTerminal().printf("\nDELETE EXISTED EMPLOYEES FROM HERE\n------------------------------");
			deleteSelectedEmployee(loginMaster, textIO);
			break;

		case GO_BACK_TO_EMPLOYEE_MENU:
			EmployeeForm.goToEmployeeFormPage( loginMaster, textIO );
			break;
			
		case GO_BACK_TO_HOME_MENU:
			textIO.getTextTerminal().print("\nBACK TO MAIN MENU\n-----------------\n");
			HomePageForAdmin.homePageForAdmin(loginMaster, textIO);
			break;

		case LOGOUT:
			textIO.getTextTerminal().print("\nYOU HAVE SUCESSFULLY LOGGED OUT!\n-------------------------------");
			MainClassForERS.loginForm();
			break;

		case EXIT:
			textIO.getTextTerminal().dispose();
			break;

		default:
			textIO.getTextTerminal().print("\nINVALID INPUT. TRY AGAIN\n");
			MainClassForERS.loginForm();
			break;
		}	}



	private static void deleteGivenEmployee(int empid, TextIO textIO, LoginMaster loginMaster) {
		
		//before deleting the employee , first delete the login master employee user details
		String SQL_DELETE = "DELETE FROM login_master WHERE userid=?";


        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_DELETE)) {

            preparedStatement.setInt(1, empid);
            int row=0;
			try {
			             row = preparedStatement.executeUpdate();
			}catch (Exception e) {
			e.printStackTrace();
			}
			finally {
				System.out.println(row);
			}
            if(row==1) {
            	textIO.getTextTerminal().printf("\nUSER LOGIN CREDENTIALS HAS BEEN REMOVED SUCESSFULLY"); 
            }
            else {
 	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");  
 				//return false;
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n"); 
        } catch (Exception e) {
            e.printStackTrace();
            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");
        }
		
		
		  SQL_DELETE = "DELETE FROM EMPLOYEES WHERE empid=?";


	        try (Connection conn = DriverManager.getConnection(
	                "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
	             PreparedStatement preparedStatement = conn.prepareStatement(SQL_DELETE)) {

	            preparedStatement.setInt(1, empid);

	            int row = preparedStatement.executeUpdate();

	            if(row==1) {
	            	textIO.getTextTerminal().printf("\nEMPLOYEE HAS BEEN DELETED SUCESSFULLY\n----------------------------------"); 
	            }
	            else {
	 	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");  
	 				//return false;
	            }

	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n"); 
	        } catch (Exception e) {
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");
	        }
	        
	        
	        

		
	}



	private static void editOrUpdateEmployee(LoginMaster loginMaster,TextIO textIO)
			throws SQLException, IOException, ParseException {

		
		int empid=0;
		//paste here the code from edit/update
		// show the all employee and ask user to select the employee whom he wants to update
				
		String runSP = "{ call getAllEmp_sp (?) }";
		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
				Statement statement = conn.createStatement();
				CallableStatement callableStatement = conn.prepareCall(runSP)) {

			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

			callableStatement.execute();
			//textIO.getTextTerminal().println("*************************************************");
		//	textIO.getTextTerminal().println("EMP ID|FIRST NAME|LAST NAME|DOB|EMAIL|DEPARTMENT");
			 
			List<Employee> list = new ArrayList<>();
			
			ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
			while (resultSet.next()) {
				Employee obj = new Employee();
				obj.setEmpid(resultSet.getInt("empid"));
				obj.setFirstname(resultSet.getString("firstname"));
				obj.setLastname(resultSet.getString("lastname"));
				obj.setDob(resultSet.getDate("dob"));
				obj.setEmail(resultSet.getString("email"));
				obj.setDepartment(resultSet.getString("DEPARTMENT_NM"));
				list.add(obj);
				//textIO.getTextTerminal().println(obj.displayEmployeeObject());
				 
			}
			//textIO.getTextTerminal().println("**************************************************");
			int index = 0;
			
			for (Employee emp : list) {
				textIO.getTextTerminal().println(++index + " : " + emp.displayEmployeeObject());
			}

			boolean flag = true;
			do {
				long choose = textIO.newIntInputReader().read("\nSELECT THE EMPLOYEE WHOSE DETAILS YOU WANT TO UPDATE:");
				index = 0;
				for (Employee emp : list) {
					if (++index == choose) {
						flag = false;
						 empid = emp.getEmpid();
					}
				}
				if(flag==true)
				textIO.getTextTerminal().printf("\nWRONG CHOICE. CHOOSE AGAIN");

			} while (flag);
			
			editGivenEmployee(empid,textIO,loginMaster);
			
		} catch (SQLException e) {
			System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("\nSOMETHING WENT WRONG... TRY AGAIN\n");
			
		} catch (Exception e) {
			e.printStackTrace();
			textIO.getTextTerminal().printf("\nINVALID INPUT. TRY AGAIN\n");
			
		}
		


//after update the details ask user if he wants to update again the same employee or other employee		
ChooseForEdit empChoice = textIO.newEnumInputReader(ChooseForEdit.class).read("\nEDIT/UPDATE THE EMPLOYEES\n-----------------------------");
		
		switch (empChoice) {
		case UPDATE_AGAIN:
			editGivenEmployee(empid, textIO, loginMaster);
			break;
			
		case UPDATE_OTHER_EMPLOYEE:
			editOrUpdateEmployee(loginMaster, textIO);
			break;

		case GO_BACK_TO_EMPLOYEE_PAGE:
			EmployeeForm.goToEmployeeFormPage( loginMaster, textIO );
			break;
			
		case GO_BACK_TO_HOME_PAGE:
			textIO.getTextTerminal().print("\nBACK TO MAIN MENU\n--------------------");
			HomePageForAdmin.homePageForAdmin(loginMaster, textIO);
			break;

		case LOGOUT:
			textIO.getTextTerminal().printf("\nYOU HAVE SUCESSFULLY LOGGED OUT!\n------------------------");
			MainClassForERS.loginForm();
			break;

		case EXIT:
			textIO.getTextTerminal().dispose();
			break;

		default:
			textIO.getTextTerminal().print("\nINVALID INPUT. TRY AGAIN..\n");
			MainClassForERS.loginForm();
			break;
		}

	}

	



	private static void editGivenEmployee(int empid, TextIO textIO,LoginMaster loginMaster) {
		// get the selected employee and ask to user which fields he wants to update
		
		String SQL_INSERT = "select * from employees where empid=?";

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
				Statement statement = conn.createStatement();
				PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			 preparedStatement.setInt(1, empid);
			
			ResultSet resultSet = preparedStatement.executeQuery();

			textIO.getTextTerminal().printf("\nSELECTED EMPLOYEE DETAIL HERE\n------------------------\n");
			Employee obj = new Employee();
			while (resultSet.next()) {
				
				obj.setEmpid(resultSet.getInt("empid"));
				obj.setFirstname(resultSet.getString("firstname"));
				obj.setLastname(resultSet.getString("lastname"));
				obj.setDob(resultSet.getDate("dob"));
				obj.setEmail(resultSet.getString("email"));
				obj.setDepartment(resultSet.getString("DEPARTMENT_ID"));
				//textIO.getTextTerminal().println(obj.displayEmployeeObject());
				 textIO.getTextTerminal().println(obj.toString());

			}
			
			
			EmployeeFieldChoice empChoice =textIO.newEnumInputReader(EmployeeFieldChoice.class).read("---------------------------------------\nSELECT WHICH FIELD YOU WANT TO UPDATE:");
			switch (empChoice) {
			case FIRST_NAME:
				//textIO.getTextTerminal().printf("\nSELECTED EMPLOYEE DETAIL HERE\n------------------------\n");
				obj.setFirstname(getEmployeeFirstName(textIO));					
				updateEmployee(obj, textIO);
				break;

			case LAST_NAME:
				obj.setLastname(getEmployeeLastName(textIO));		
				updateEmployee(obj, textIO);
				break;

			case DOB:
				obj.setDob(getEmployeeDob(textIO));		
				updateEmployee(obj,textIO);
				break;
			
		
			case EMAIL:
				obj.setEmail(getEmployeeEmail(textIO));		
				updateEmployee(obj,textIO);
				break;
				
			case BACK_TO_EMPLOYEE_MENU:
				goToEmployeeFormPage(loginMaster, textIO);
				break;
				
		
			case BACK_TO_MAIN_MENU:
				textIO.getTextTerminal().printf("\nBACK TO MAIN MENU\n---------------------");
				HomePageForAdmin.homePageForAdmin(loginMaster,textIO);
				break;
				
			case LOGOUT:
				textIO.getTextTerminal().printf("\nYOU HAVE SUCESSFULLY LOGGED OUT!\n-----------------------------");
				MainClassForERS.loginForm();
				break;
				
			case EXIT:
				textIO.getTextTerminal().dispose();
				break;
				
			default:
				textIO.getTextTerminal().println("SOMETHING WENT WRONG. TRY AGAIN..");
				break;
			}
			
			
		

		} catch (SQLException e) {
			System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");
			//return 0;
		} catch (Exception e) {
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");
			//return 0;
		}
		
	}



	private static String getEmployeeLastName(TextIO textIO) {
		String name=null;
		do {
			name= textIO.newStringInputReader()
			        .read("Last Name");
			}while(!MainClassForERS.lastName(name.trim(),textIO));
		
		return name;
	}



	private static Date getEmployeeDob(TextIO textIO) {
		String date=null;
		do{
			date= textIO.newStringInputReader().withDefaultValue("(eg. yyyy-MM-dd)")
		        .read("Date of Birth");
		}while(!Example.validateJavaDate(date,textIO) || (!AgeClass.calculateAge(date,LocalDate.now(),textIO)) );
		
		return MainClassForERS.convertStringIntoDate(date);
	}



	private static String getEmployeeEmail(TextIO textIO) {
		String email=null;
		do {
			
				
			email=textIO.newStringInputReader()
			        .read("Email");
			}while(!CheckEmailValidation.isValid(email.trim()));
			
		return email;
	}



	private static String getEmployeeFirstName(TextIO textIO) {
		String name=null;
		do {
			name = textIO.newStringInputReader()
			        .read("First Name");
			}while(!MainClassForERS.firstName(name.trim(),textIO));
		return name;
	}



	private static void updateEmployee(Employee obj,TextIO textIO) {
		// TODO Auto-generated method stub
		  String SQL_UPDATE = "UPDATE EMPLOYEES SET firstname=?,lastname=?,dob=?,email=? WHERE empid=?";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE)) {

            preparedStatement.setString(1,obj.getFirstname());
            preparedStatement.setString(2,obj.getLastname());
            preparedStatement.setDate(3,obj.getDob());
            preparedStatement.setString(4,obj.getEmail());
            preparedStatement.setInt(5,obj.getEmpid());

            int row = preparedStatement.executeUpdate();

            if(row==1) {
            	textIO.getTextTerminal().printf("\nEMPLOYEE HAS BEEN UPDATED SUCESSFULLY\n-------------------------------------"); 
            }
            else {
 	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");  
 				//return false;
            }
            // rows affected

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");  
        } catch (Exception e) {
            e.printStackTrace();
            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN..\n");  
        }
	}



	private static void displayEmployee(TextIO textIO) throws SQLException {
		// show emp details

		String runSP = "{ call getAllEmp_sp (?) }";

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
				Statement statement = conn.createStatement();
				CallableStatement callableStatement = conn.prepareCall(runSP)) {

			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

			callableStatement.execute();
			 
			List<Employee> list = new ArrayList<>();
			
			ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
			while (resultSet.next()) {
				Employee obj = new Employee();
				obj.setEmpid(resultSet.getInt("empid"));
				obj.setFirstname(resultSet.getString("firstname"));
				obj.setLastname(resultSet.getString("lastname"));
				obj.setDob(resultSet.getDate("dob"));
				obj.setEmail(resultSet.getString("email"));
				obj.setDepartment(resultSet.getString("DEPARTMENT_NM"));
				list.add(obj);
				textIO.getTextTerminal().println(obj.displayEmployeeObject());
				 
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
			textIO.getTextTerminal()
					.printf("\nSOMETHING WRONG.. Try again..\n");
			// return false;
		} catch (Exception e) {
			e.printStackTrace();
			textIO.getTextTerminal().printf("\nSOMETHING WRONG..\n");
			// return false;
		}

	}


	private static void addEmployee(LoginMaster loginMaster,TextIO textIO) throws SQLException, IOException, ParseException {
		Employee emp = new Employee();
		// make the database connection
		
		do {
		emp.setFirstname(textIO.newStringInputReader()
		        .read("\nFirst Name"));
		}while(!MainClassForERS.firstName(emp.getFirstname().trim(),textIO));
		
		do {
		emp.setLastname( textIO.newStringInputReader()
		        .read("Last Name"));
		}while(!MainClassForERS.lastName(emp.getLastname().trim(),textIO));
		
		
		String date=null;
		do{
			date= textIO.newStringInputReader().withDefaultValue("(eg. yyyy-MM-dd)")
		        .read("Date of Birth");
		}while(!Example.validateJavaDate(date,textIO) || (!AgeClass.calculateAge(date,LocalDate.now(),textIO)) );
		
		
		emp.setDob(MainClassForERS.convertStringIntoDate(date));
		
		
		do {
		emp.setEmail(textIO.newStringInputReader()
		        .read("Email"));
		}while(!CheckEmailValidation.isValid(emp.getEmail().trim()));
		
		//get the existed department name from datatabase and show them and here than employee will choose department from the options...
		long deptId= MainClassForERS.getAllDepartname();
		
		
		/*
		 * emp.setDepartment(textIO.newStringInputReader() .read("Department"));
		 */
		
		FormSubmitOrCancel empChoice =textIO.newEnumInputReader(FormSubmitOrCancel.class).read("-------------------------");
		switch (empChoice) {
		case SUBMIT:
			MainClassForERS.saveEmloyee(emp,(int)deptId,textIO);
			textIO.getTextTerminal().printf("\nADDED NEW EMPLOYEE\n------------------\n");
			displayEmployee(textIO);
			goToEmployeeFormPage(loginMaster, textIO);
			break;

			case CANCEL:
			textIO.getTextTerminal().printf("\nADD NEW EMPLOYEE HERE\n-----------------------");
			addEmployee(loginMaster,textIO);
			break;
			
		case GO_BACK_TO_HOME_PAGE:
			HomePageForAdmin.homePageForAdmin(loginMaster,textIO);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().print("\nYOU HAVE SUCESSFULLY LOGGED OUT!\n-------------------------------");
			MainClassForERS.loginForm();
			break;
			
		case EXIT:
			textIO.dispose();
			break;
			
		default:
			break;
		}
		
	}
}
