package backenddev;



public class Car extends Vehicle{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int limit = 5;
	public Car(){
		super();
		this.name = "Car";
		numOfPassengers = setPassengerNumber(limit);
		for(int i = 0;i<numOfPassengers;i++){
			passengers.add(new Person());
		}
	}
}