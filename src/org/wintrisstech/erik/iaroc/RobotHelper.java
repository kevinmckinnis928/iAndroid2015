package org.wintrisstech.erik.iaroc;

import ioio.lib.api.exception.ConnectionLostException;

import java.util.ArrayList;
import java.util.List;

public class RobotHelper {

	private int counter;
	private final List<Integer> firstFiveCompassReading;
	private float averageReading;
	//private final Dashboard dashboard;
	private final IRobotCreateInterface create;
	
	public RobotHelper(
			//Dashboard dashboard, 
			IRobotCreateInterface create) {
		
		firstFiveCompassReading = new ArrayList<Integer>();
		counter = 0;
		//this.dashboard = dashboard;
		this.create = create;
	}
	
	public void averageList() {
		
		for (int i = 0; i < RobotHelperConstants.NUMBER_OF_READINGS_TO_AVERAGE; i++) {
			
			averageReading += firstFiveCompassReading.get(i);
		}
		
		averageReading = averageReading / RobotHelperConstants.NUMBER_OF_READINGS_TO_AVERAGE;
	}
	
	public void raceInAStraightLine(int compassReading) throws ConnectionLostException {
		
		if (compassReading == averageReading) {
			
			create.driveDirect(RobotHelperConstants.FASTWHEEL, RobotHelperConstants.FASTWHEEL);
		}
		else if (compassReading > averageReading) {
			
			create.driveDirect(RobotHelperConstants.FASTWHEEL, RobotHelperConstants.SLOWWHEEL);
		}
		else if (compassReading < averageReading) {
			
			create.driveDirect(RobotHelperConstants.SLOWWHEEL, RobotHelperConstants.FASTWHEEL);
		}
	}
	
	public void addAverageCompassReading(int reading) {
		
		this.firstFiveCompassReading.add(reading);
	}
	
	public int getCounter() {
		
		return counter;
	}
	public void setCounter(int counter) {
		
		this.counter = counter;
	}
	
	public List<Integer> getAverageCompassReading() {
		
		return firstFiveCompassReading;
	}
	
	public float getAverageReading() {
		
		return averageReading;
	}
	
	public void setAverageReading(float averageReading) {
		
		this.averageReading = averageReading;
	}

	public void incrementCounter() {
		
		counter++;
	}

	
}
