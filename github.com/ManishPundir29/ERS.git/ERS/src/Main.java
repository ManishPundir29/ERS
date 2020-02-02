import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class Main {
public static void main(String[] args) {
	//get all compliance details
			String runFunction = "{?= call getAllRL_func()}";

	        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "empdb", "root");
	             Statement statement = conn.createStatement();
	             CallableStatement cs = conn.prepareCall(runFunction);
	        ) {

	            // We must be inside a transaction for cursors to work.
	            conn.setAutoCommit(false);

	            // register output
	            
	            cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	            // run function
	            cs.execute();
	            
	            String cursorName = cs.getString(1);

	            // get refcursor and convert it to ResultSet
	            ResultSet resultSet = (ResultSet) cs.getObject(1);
	            while (resultSet.next()) {
	                System.out.println(resultSet.getInt("complianceid"));
	                System.out.println(resultSet.getInt("department_id"));
	                System.out.println(resultSet.getString("department_nm"));
	                System.out.println(resultSet.getString("rltype"));
	                System.out.println(resultSet.getString("details"));
	                System.out.println(resultSet.getDate("createdate"));
	                System.out.println(resultSet.getInt("empcount"));
	                System.out.println(resultSet.getInt("statuscount"));
	            }

	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
}
}
