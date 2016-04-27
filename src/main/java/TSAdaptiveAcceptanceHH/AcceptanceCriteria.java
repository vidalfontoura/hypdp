package TSAdaptiveAcceptanceHH;

import java.util.Random;

public class AcceptanceCriteria {

	private Random rng;
	private int percent;
	private int percentchange = 5;
	private int maximumpercent = 60;

	public AcceptanceCriteria(Random r) {
		rng = r;
		percent = 0;
	}//end constructor

	public boolean accept(double delta) {
		return AcceptAdaptive(delta);
	}//end method accept
	
	public void increasePercent() {
		if (percent <= maximumpercent-percentchange) {
			percent += percentchange;
			//System.out.println("I " + percent);
		}
		
	}
	
	public void decreasePercent() {
		if (percent >= percentchange) {
			percent -= percentchange;
			//System.out.println("D " + percent);
		}
	}

	private boolean AcceptAdaptive(double delta) {
		if (delta < 0) {
			int select = rng.nextInt(100) + 1;
			if (select <= percent) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}//end method AcceptNonWorsening

}//end class
