package course_scheduling_mvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ConflictTableModel {

	public void addToConflictTable(String courseId, String errorMessage) {
		try
		{
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursepref", "root", "harishruti");
		
			String sql = "insert into conflictTable values (?,?)";
			PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, courseId);
            pstmt.setString(2, errorMessage);
            pstmt.executeUpdate();
			
	
			
		connection.close();
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
