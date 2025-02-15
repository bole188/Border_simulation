package backenddev;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Terminals extends Thread{
	public Terminals(){}
	protected String pathToBinFile;
	public static String pathToTxtFile;
	protected ArrayList<Person> peopleWithInvalidDoc = new ArrayList<Person>();
	protected static final Object lock = new Object();
	
	public boolean checkDocuments(Vehicle v) {
		int i = 1;
		ArrayList<Person> secondPassengerList = new ArrayList<Person>(v.passengers);
		while(i<=secondPassengerList.size())
		{
			if(PossibilityCalculator.calculate(3)) 
			{
				if(peopleWithInvalidDoc.isEmpty()) {
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddss");
			        String formattedDateTime = currentDateTime.format(formatter);
					pathToBinFile = formattedDateTime + ".ser";
					peopleWithInvalidDoc.add(v.passengers.get(i-1));
					try{
						FileHandler fileHandler = new FileHandler("error.log");
			            SimpleFormatter simpleFormatter = new SimpleFormatter();
			            fileHandler.setFormatter(simpleFormatter);
			            ExceptionHandler.logger.addHandler(fileHandler);
			            fileHandler.close();
						FileOutputStream fos = new FileOutputStream(pathToBinFile,true);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(v.passengers.get(i-1));
						oos.close();
					}catch(IOException e) {
						ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
					}
				}
				else {
					peopleWithInvalidDoc.add(v.passengers.get(i-1));
					try{
						FileHandler fileHandler = new FileHandler("error.log");
			            SimpleFormatter simpleFormatter = new SimpleFormatter();
			            fileHandler.setFormatter(simpleFormatter);
			            ExceptionHandler.logger.addHandler(fileHandler);
			            fileHandler.close();
						FileOutputStream fos = new FileOutputStream(pathToBinFile,true);
						AppObjectOutputStream aoos = new AppObjectOutputStream(fos);
						aoos.writeObject(v.passengers.get(i-1));
						aoos.close();
					}catch(IOException e) {
						ExceptionHandler.logger.log(Level.SEVERE,e.fillInStackTrace().toString(), e);
					}
				}
				if(i==1) 
				{
					VehiclesWithIncidents.addVehicle(v);
					return false;
				}
			secondPassengerList.remove(i-1);
			}
			else i++;
		}
		v.setPassengersNumber(secondPassengerList.size());
		v.setPassengers(secondPassengerList);
		return true;
	}
}
