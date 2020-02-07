import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TimetableParser {
	
	public TimetableParser() {

	}

	public static HashMap<Integer,List<Time>> getTimetable(String ttInfo) throws IOException {
		HashMap<Integer,List<Time>>expectedBuses=new HashMap<>(); // creating empty hashmmap
		BufferedReader timetableFile =new BufferedReader(new FileReader(ttInfo)); //to read from file
		String line = timetableFile.readLine(); //reading line
		while(line != null) {   
			List<Time> timeForThisRoute=new ArrayList<>(); // new arraylist
			List<String> lineParts=Arrays.asList(line.split(",")); //seperated line by ,
			// get all the list items except first one first one is the routeid 
			int routeId=Integer.parseInt(lineParts.get(0));  //string to integer
			int counter=0;
			for (String part:lineParts) {
				if (counter==0) {
					counter+=1;
					continue;}
				else {
					String hourAndTime[]=part.split(":"); //seperating time 
					@SuppressWarnings("deprecation")
					Time timeobj=new Time(Integer.parseInt(hourAndTime[0]),Integer.parseInt(hourAndTime[1]),00); //creating time obj
					timeForThisRoute.add(timeobj);}
			}
			expectedBuses.put(routeId,timeForThisRoute);  // adding route and its time
			line=timetableFile.readLine();

		}
		timetableFile.close();
		return expectedBuses;
	}
}
