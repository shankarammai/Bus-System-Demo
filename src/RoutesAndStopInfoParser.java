import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoutesAndStopInfoParser {
	public RoutesAndStopInfoParser() {

	}

	public static List<Route> getRoutes(String rsInfo,String ttInfo) throws IOException {
		List<Route> routes=new ArrayList<>();
		BufferedReader file =new BufferedReader(new FileReader(rsInfo));
		int counter=0;
		String line=file.readLine();
		while( line !=null) {
			String lineparts[]=line.split(",");
			Route route=new Route(Integer.parseInt(lineparts[0]),lineparts[1],lineparts[2]);
			HashMap<Integer,List<Time>>allRoutesInfo=TimetableParser.getTimetable(ttInfo);
			List<Time> scheduleOfRoute= allRoutesInfo.get(route.getRouteId());
			route.setSchedule(scheduleOfRoute);
			routes.add(route);
			line=file.readLine();
			counter=+1;	
		}
		file.close();
		return routes;

	}
	public static String[] getBusStopInfo(String stopInfo) throws IOException{
		BufferedReader file =new BufferedReader(new FileReader(stopInfo));
		String line=file.readLine();
		String[] lineparts = null;
		while( line !=null) {
			lineparts=line.split(",");
			line=file.readLine();
		}
		return lineparts;
	}
}
