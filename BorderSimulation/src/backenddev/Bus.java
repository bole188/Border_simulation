package backenddev;

import java.util.ArrayList;


public class Bus extends Vehicle{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int limit = 52;
	public ArrayList<Integer> items = new ArrayList<Integer>();
	public ArrayList<Integer> getItems() {
		return items;
	}
	public void updateItems(ArrayList<Integer> listOfItems) {
		items = listOfItems;
	}
	private void setLuggageSpace(){
		int i = 0;
		while(i<numOfPassengers)
		{
			boolean setLuggage = PossibilityCalculator.calculate(70);
			if(!setLuggage) items.add(0);
			else 
			{
				boolean setIllegal = PossibilityCalculator.calculate(10);
				if(setIllegal) items.add(2);
				else items.add(1);
			}
			i++;
		}
	}
	public Bus(){
		super();
		this.name = "Bus";
		numOfPassengers = setPassengerNumber(limit);
		for(int i = 0;i<numOfPassengers;i++){
			passengers.add(new Person());
		}
		setLuggageSpace();
	}
}
