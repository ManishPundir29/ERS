import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.util.Properties;

public class LoadJdbcSettingFromPropertiesFile {
	 String result = "";
		 InputStream inputStream;
	public  String getPropValues() throws IOException {
		
		try
		{
			// Create Properties object.
			Properties prop = new Properties();
			String propFileName = "jdbc.properties";
			
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			// Load jdbc related properties in above file. 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			
			// Get each property value.
			String dbDriverClass = prop.getProperty("db.driver.class");
			
			String dbConnUrl = prop.getProperty("db.conn.url");
			
			String dbUserName = prop.getProperty("db.username");
			
			String dbPassword = prop.getProperty("db.password");
			
			if(!"".equals(dbDriverClass) && !"".equals(dbConnUrl))
			{
				/* Register jdbc driver class. */
				Class.forName(dbDriverClass);
				
				// Get database connection object.
				Connection dbConn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);
				
				// Get dtabase meta data.
				DatabaseMetaData dbMetaData = dbConn.getMetaData();
				
				// Get database name.
				String dbName = dbMetaData.getDatabaseProductName();
				
				// Get database version.
				String dbVersion = dbMetaData.getDatabaseProductVersion();
				
				System.out.println("Database Name : " + dbName);
				
				System.out.println("Database Version : " + dbVersion);
			}
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		new LoadJdbcSettingFromPropertiesFile().getPropValues();
	}

}
