import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import org.beryx.textio.TextIO;

public class DepartmentForm {
	static void goToDepartment( LoginMaster loginMaster,TextIO textIO) throws SQLException, IOException, ParseException {
		
		DepartmentChoice empChoice =textIO.newEnumInputReader(DepartmentChoice.class).read("--------------------\nDEPARTMENT MENU:");
		switch (empChoice) {
		case ADD_DEPARTMENT:
			AddDepartment( loginMaster,textIO);
			goToDepartment( loginMaster,textIO);
			break;
			
		case VIEW_DEPARTMENT:
			viewDepartment( loginMaster,textIO);
			goToDepartment( loginMaster,textIO);
			break;
			
		case BACK_TO_HOME_PAGE:
			textIO.getTextTerminal().printf("\nBACK TO MAIN MENU\n-----------------\n");
			HomePageForAdmin.homePageForAdmin(loginMaster,textIO);
			break;
			
		case LOGOUT:
			textIO.getTextTerminal().printf("\nYOU HAVE SUCESSFULLY LOGGED OUT!------------------------------\n");
			MainClassForERS.loginForm();
			break;
			
		case EXIT:
			textIO.getTextTerminal().dispose();
			break;
			
		default:
			break;
			
		}
		
	}
	
private static void viewDepartment(LoginMaster loginMaster,TextIO textIO) {
		
		//admin can view the department not the user
		//get the list from database
		String SQL_INSERT = "select * from department";
		
		 try (Connection conn = DriverManager.getConnection(
				 "jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
	             Statement statement = conn.createStatement();
				 PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

			 ResultSet resultSet = preparedStatement.executeQuery();
			 
			 textIO.getTextTerminal().printf("\nDISPLAYING DEPARTMENT LIST:\n-----------------------");
			 
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
	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN.\n");  
				//return false;
	        } catch (Exception e) {
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN.\n");  
				//return false;
	        }
		
	}

	private static void AddDepartment( LoginMaster loginMaster,TextIO textIO) {
		
		//add here new department 
		textIO.getTextTerminal().printf("--------------------\nADD NEW DEPARTMENT:\n"); 
		Department department = new Department();
		
		do {
		department.setDepartment_nm(textIO.newStringInputReader().
				read("Department Name"));
		}while(!MainClassForERS.firstName(department.getDepartment_nm(), textIO));
		
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
	            	textIO.getTextTerminal().printf("\nDEPARTMENT HAS BEEN SAVED SUCESSFULLY\n-----------------------------"); 
	            }
	            else {
	            	
	 	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN.\n");  
	 				//return false;
	            }
	         
	            
	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\t%s", e.getSQLState(), e.getMessage());
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN.\n");  
				//return false;
	        } catch (Exception e) {
	            e.printStackTrace();
	            textIO.getTextTerminal().printf("\nSOMETHING WENT WRONG. TRY AGAIN.\n");  
				//return false;
	        }
		
		
		
	}
}
