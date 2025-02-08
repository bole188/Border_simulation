package backenddev;

import java.io.Serializable;
import java.util.Random;

public class Person implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	public Person(){
		Random random = new Random();
		id = random.nextInt(90000) + 10000;
	}
	public int GetId(){
		return id;
	}
}
