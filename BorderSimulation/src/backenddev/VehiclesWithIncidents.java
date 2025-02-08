package backenddev;

import java.util.ArrayList;

public final class VehiclesWithIncidents {
	public static ArrayList<Vehicle> line = new ArrayList<Vehicle>();
	public static synchronized void addVehicle(Vehicle v) {
		line.add(v);
	}
}
