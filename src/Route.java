import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Route {
	public int routeId;
	public String destination;
	public String origin;
	public List<Time>schedule; 

	public Route(int routeId,String destination,String origin) {
		this.routeId=routeId;
		this.destination=destination;
		this.origin=origin;
		this.schedule=new ArrayList<>();
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public List<Time> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<Time> schedule) {
		this.schedule = schedule;
	}
}
