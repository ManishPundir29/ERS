import java.text.SimpleDateFormat;
import java.util.Date;

import org.beryx.textio.TextIO;

import java.text.ParseException;
public class Example{
   public static boolean validateJavaDate(String strDate, TextIO textIO)
   {
	/* Check if date is 'null' */
	if (strDate.trim().equals(""))
	{
	    return false;
	}
	/* Date is not 'null' */
	else
	{
	    /*
	     * Set preferred date format,
	     * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
	    SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
	    sdfrmt.setLenient(false);
	    /* Create Date object
	     * parse the string into date 
             */
	    try
	    {
	        Date javaDate = sdfrmt.parse(strDate); 
	      // textIO.getTextTerminal().println(strDate+" is valid date format");
	    }
	    /* Date format is invalid */
	    catch (ParseException e)
	    {
	    	 textIO.getTextTerminal().println(strDate+" is Invalid Date format");
	        return false;
	    }
	    /* Return true if date format is valid */
	    return true;
	}
   }
   
   public static void main(String[] args) {
	//validateJavaDate("1995-09-09");
}
  
}
