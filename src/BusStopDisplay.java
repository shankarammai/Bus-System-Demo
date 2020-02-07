import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BusStopDisplay implements BusInfo{
	public String Id,name;
	public List<Route>routes;
	public HashMap<Integer,List<Time>>AllExpectedBusesData;
	public List<ExpectedBus> expectedBuses=new ArrayList<>();

	public BusStopDisplay() {

	}
	public BusStopDisplay(String Id, String name) {
		this.Id=Id;
		this.name=name;
		this.routes =new ArrayList<>();
		this.AllExpectedBusesData=new HashMap<>();
		this.expectedBuses=new ArrayList<>();

	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	public HashMap<Integer, List<Time>> getAllExpectedBusesData() {
		return AllExpectedBusesData;
	}
	public void setAllExpectedBusesData(HashMap<Integer, List<Time>> allExpectedBusesData) {
		AllExpectedBusesData = allExpectedBusesData;
	}
	public void setExpectedBuses(List<ExpectedBus> expectedBuses) {
		this.expectedBuses = expectedBuses;
	}



	public BusStopDisplay create (String stopInfo, String rsInfo,String ttInfo) throws IOException {
		List<Route> allRoutes=RoutesAndStopInfoParser.getRoutes(rsInfo, ttInfo);

		TimetableParser timetableOfRoutes=new TimetableParser();
		HashMap<Integer,List<Time>> allExpectedBusesData =timetableOfRoutes.getTimetable(ttInfo);

		String[] busStopDetails=RoutesAndStopInfoParser.getBusStopInfo(stopInfo);

		BusStopDisplay newDisplay=new BusStopDisplay(busStopDetails[0],busStopDetails[1]);
		newDisplay.setRoutes(allRoutes);
		newDisplay.setAllExpectedBusesData(allExpectedBusesData);
		newDisplay.addScheduledToExpected();

		return newDisplay;

	}
	public List<ExpectedBus> getExpectedBuses(){
		return this.expectedBuses;

	}
	public void addScheduledToExpected() {
		for (Route routeName: this.getCallingRoutes()) {
			List <Time>routeAllTimes=routeName.getSchedule();
			for (Time time :routeAllTimes) {
				ExpectedBus expectedBus=new ExpectedBus(
						routeName.getRouteId(),
						routeAllTimes.indexOf(time)+1,
						routeName.getDestination(),
						BusStatus.onTime,
						time,
						0);
				this.expectedBuses.add(expectedBus);

			}

		}
		this.expectedBuses.sort((bus1,bus2)-> {
			if (bus1.getTime().after(bus2.getTime())) {
				return +1;
			}	
			else if (bus2.getTime().after(bus1.getTime())) {
				return -1;
			}
			else {
				return 0;
			}
		});
	}

	public List<Route> getCallingRoutes() {
		return Collections.unmodifiableList(this.routes);
	}
	public List<Time> getDepartureTimes(int routeId) {
		List<Time>schedule=this.AllExpectedBusesData.get(routeId);
		return Collections.unmodifiableList(schedule);
	}

	public Time getTimeOfNextBus(int routeNo, Time time) {
		List<Time> Times=this.AllExpectedBusesData.get(routeNo);
		Time nextDeparture=Times.get(0);
		for (Time nextTime:Times) {
			if(nextTime.after(time)){
				nextDeparture=nextTime;
				break;
			}
		}
		return nextDeparture;

	}
	public void display( Time time) {
		Iterator<ExpectedBus>allExpectedBuses= this.getExpectedBuses().iterator();
		while(allExpectedBuses.hasNext()) {
			ExpectedBus currentexpectedbus=allExpectedBuses.next();

			if ((currentexpectedbus.busStatus==BusStatus.cancelled) && (currentexpectedbus.time.before(time))) {
				allExpectedBuses.remove();
			}
			int tolorance=currentexpectedbus.time.getMinutes()+3;
			Time tolorancetime =new Time(currentexpectedbus.time.getHours(),tolorance,00);
			if(tolorancetime.before(time)) {
				allExpectedBuses.remove();
				this.expectedBuses.remove(currentexpectedbus);
			}
		}
		if(this.expectedBuses.size()<10) {
			this.expectedBuses.clear();
			this.addScheduledToExpected();

		}
		int firstExpectedBusIndex=0;
		for (ExpectedBus bus:this.expectedBuses) {

			if (bus.getTime().after(time)) {
				firstExpectedBusIndex=this.expectedBuses.indexOf(bus);
				break;
			}
		}
		System.out.println("|----------------------------------------------------------------------|");
		System.out.println("|  Number          Destination            Due at                Status |");
		System.out.println("|----------------------------------------------------------------------|");
		int i;
		for(i=0;i<10;i++) {
			ExpectedBus nextBus ;
			try {
				nextBus=this.expectedBuses.get(i+firstExpectedBusIndex);
				List<String>expectedBusDetail=new ArrayList<>();
				expectedBusDetail.add(Integer.toString(nextBus.getRouteId()));
				expectedBusDetail.add(nextBus.getDestination());
				expectedBusDetail.add(nextBus.getTime().toString());
				if(nextBus.getBusStatus()==BusStatus.delayed) {
					expectedBusDetail.add(nextBus.getBusStatus().toString()+" by "+nextBus.getDelay()+" min");
				}
				else {expectedBusDetail.add(nextBus.getBusStatus().toString());}
				System.out.format("%3s%30s%15s%20s\n", expectedBusDetail.get(0),expectedBusDetail.get(1),expectedBusDetail.get(2),expectedBusDetail.get(3));

			}

			catch(IndexOutOfBoundsException e) {
				break;}
		}

		for (int j=0;j<(10-i);j++) {
			System.out.format("%3s%30s%15s%20s\n",this.expectedBuses.get(j).getRouteId(),
					this.expectedBuses.get(j).getDestination(),
					this.expectedBuses.get(j).getTime(),
					this.expectedBuses.get(j).getBusStatus()  );
		}
	}

	@Override
	public void stimulate() {
		for(ExpectedBus buses:this.getExpectedBuses()) {
			int random = new Random().nextInt(BusStatus.values().length);
			BusStatus status=BusStatus.values()[random];
			if (status==(BusStatus.delayed)) {
				buses.update();

			}

		}

	}

	public static void main(String[] args) throws IOException, InterruptedException {
		BusStopDisplay display=new BusStopDisplay();
		BusStopDisplay d=display.create("stop_info.csv", "routes.csv", "timetable.csv");
		while(true) {
			LocalTime nowTime=java.time.LocalTime.now();
			@SuppressWarnings("deprecation")
			Time currentTime=new Time(nowTime.getHour(),nowTime.getMinute(),nowTime.getSecond());
			d.display(currentTime);
			d.stimulate();
			TimeUnit.SECONDS.sleep(15);
		}
	}
}