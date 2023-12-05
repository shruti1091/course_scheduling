package course_scheduling_mvc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.StringTokenizer; 

public class PGWithPrefModel 
{
	public void schedPGWithPref(String[] timeSlots, int[][] rooms, String[][] sched,HashSet<String> course)
	{
		try
		{
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursepref", "root", "harishruti");
		
			String sql = "select * from pgcoursepref_info where PreferredLectureTimes!=''";
		
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			ConflictTableModel ct=new ConflictTableModel();
			while(resultSet.next())
			{
				String courseId = resultSet.getString("CourseNumber");
				int enroll = resultSet.getInt("ExpectedEnrollment");
				String slots = resultSet.getString("PreferredLectureTimes");
				StringTokenizer pref = new StringTokenizer(slots,",");  
			     while (pref.hasMoreTokens()) {
			    	 int skipToNextToken=0;
			        int slot=0;
			        String token=pref.nextToken();
			        slot=findSlot(timeSlots,token);
					int room=0;
					room=findRoom(rooms,enroll);
					if(room==-1) ct.addToConflictTable(courseId,"room with required capacity not found");
					else if(slot==-1) ct.addToConflictTable(courseId,"slot was not found in depatment DB");
					else if(!course.contains(courseId))	ct.addToConflictTable(courseId,"course not found in department DB");
					else if(course.contains(courseId))
					{
				
						if(room!=-1 && slot!=-1)
						{
							if(!sched[slot][room].equals("")) {
								ct.addToConflictTable(courseId,"some other course is scheduled in the  pref : "+token);
								skipToNextToken=1;
								
							}
							else if(sched[slot][room].equals("")) {
								skipToNextToken=0;
								sched[slot][room]=courseId;
								
							}
						
						}
						if(skipToNextToken==0)break;
						
					}
			     }  
				
				
			}		
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	public int findRoom(int[][] rooms,int enroll)
	{
		for(int i=0;i<rooms.length;i++)
		{
			if(enroll<rooms[i][1])
			{
				return i;
			}
		}
		return -1;
		
	}
	public int findSlot(String[] timeSlots, String pref)
	{
		for(int i=0;i<timeSlots.length;i++)
		{
			if(pref.equals(timeSlots[i])) return i;
		}
		return -1;
		
	}
}
