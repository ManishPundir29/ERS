import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.beryx.textio.TextIO;

public class HomePageForAdmin {

	public static void homePageForAdmin(LoginMaster loginMaster,TextIO textIO) throws SQLException, IOException, ParseException {
		
		//welome to admin home page
		textIO.getTextTerminal().printf("Welcome "+loginMaster.getRole()+", User ID "+loginMaster.getUserid()+"\n");
		
		
		
		MainMenu mainMenu =textIO.newEnumInputReader(MainMenu.class).read("-----------------------------\nMain Menu:");
		switch (mainMenu) {
		case EMPLOYEE:
			EmployeeForm.goToEmployeeFormPage( loginMaster, textIO );
			break;

		case DEPARTMENT:
			DepartmentForm.goToDepartment( loginMaster, textIO);
			break;
			
		case REGULATION_AND_LEGISLATION:
			RLForm.goToRL( loginMaster, textIO);
			break;
			
	//		Compliance Tracking
		case LOGOUT:
			textIO.getTextTerminal().println("You have successfully logged out!");
			MainClassForERS.loginForm();
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

}
