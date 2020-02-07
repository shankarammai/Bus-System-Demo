import java.sql.Time;
import java.util.Random;

public class ExpectedBus implements Observer{
	public int routeId;
	public int journeyNumber;
	public String destination;
	public BusStatus busStatus;
	public Time time;
	public int delay;

	public ExpectedBus(int routeId,int journeyNumber, String destination, BusStatus busStatus,Time time,int delay) {
		this.routeId=routeId;
		this.journeyNumber=journeyNumber;
		this.destination=destination;
		this.busStatus=busStatus;
		this.time=time;
		this.delay=delay;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getJourneyNumber() {
		return journeyNumber;
	}

	public void setJourneyNumber(int journeyNumber) {
		this.journeyNumber = journeyNumber;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public BusStatus getBusStatus() {
		return busStatus;
	}

	public void setBusStatus(BusStatus busStatus) {
		this.busStatus = busStatus;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	public void update() {
		int randommin=new Random().nextInt(20 - 2);
		this.setBusStatus(BusStatus.delayed);
		this.setDelay(randommin);
		
	}
}
