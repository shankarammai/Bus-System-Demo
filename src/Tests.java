import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Time;

import org.junit.Test;

public class Tests {
	BusStopDisplay display=new BusStopDisplay();
	@Test
	public void testNextTimeOfBus() throws IOException {
		BusStopDisplay d=display.create("stop_info.csv", "routes.csv", "timetable.csv");
		Time time =new Time(8,04,00);
		Time nextTime=d.getTimeOfNextBus(15, time);
		assertEquals(nextTime.toString(),"08:19:00");
	}

	@Test
	public void testNextTimeOfBus2() throws IOException {
		BusStopDisplay d=display.create("stop_info.csv", "routes.csv", "timetable.csv");
		Time time =new Time(05,00,00);
		Time nextTime=d.getTimeOfNextBus(33, time);
		assertEquals(nextTime.toString(),"07:09:00");

	}
	@Test
	public void testGetCallingRoutes() throws IOException {
		BusStopDisplay d=display.create("stop_info.csv", "routes.csv", "timetable.csv");
		assertEquals(d.getCallingRoutes().get(0).getRouteId(),3);
	}

	@Test
	public void testAddScheduledToExpected() throws IOException {
		BusStopDisplay d=display.create("stop_info.csv", "routes.csv", "timetable.csv");
		assertEquals(d.getCallingRoutes().get(0).getRouteId(),3);
	}
	@Test
	public void testGetDepartureTimes() throws IOException {
		BusStopDisplay d=display.create("stop_info.csv", "routes.csv", "timetable.csv");
		assertEquals(d.getDepartureTimes(3).toString(),"[08:11:00, 09:11:00, 10:11:00, 11:11:00, 12:11:00, 13:11:00, 14:11:00, 15:11:00, 16:11:00, 17:11:00, 18:11:00, 19:11:00, 20:11:00, 21:11:00]");
	}

	@Test
	public void testGetDepartureTimes2() throws IOException {
		BusStopDisplay d=display.create("stop_info.csv", "routes.csv", "timetable.csv");
		assertEquals(d.getDepartureTimes(21).toString(),"[08:19:00, 09:19:00, 10:19:00, 11:19:00, 12:19:00, 13:19:00, 14:19:00, 15:19:00, 16:19:00, 17:19:00, 18:19:00, 19:19:00, 20:19:00, 21:19:00]");
	}


}
