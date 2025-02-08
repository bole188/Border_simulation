package backenddev;
import java.util.*;

public final class PossibilityCalculator{
    private PossibilityCalculator() {
        throw new AssertionError("PossibilityCalculator should not be instantiated.");
    }

    public static boolean calculate(int percentage){
		Random random = new Random();
		int genNumber = random.nextInt(100);
		if(percentage > genNumber+1) return true;
		return false;
	}

}
