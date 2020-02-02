import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class AgeClass {
	public static boolean calculateAge(String birthDat,LocalDate currentDate,TextIO textio ) {
			    // validate inputs ...
		
		
		try {
			LocalDate birthDate  = LocalDate.parse(birthDat);
			
		 int  age= Period.between(birthDate, currentDate).getYears();
		 System.out.println("age"+age);
		 if(age<24) {
			 textio.getTextTerminal().println("Employee Age should be 24 or older.. ");
			 System.out.println("true");
			 return false;
		 }
			}
		  catch (Exception e) {
			  textio.getTextTerminal().println("Invalid Input. Try Again...");
	        return false;
	    }
		return true;
}
	
	public static void main(String[] args) {
		System.out.println("Main Result:"+calculateAge("1996-09-09", LocalDate.now(),TextIoFactory.getTextIO()));
	}
	
}