package org.wintrisstech.erik.iaroc;

import ioio.lib.api.exception.ConnectionLostException;

import java.util.ArrayList;
import java.util.List;

public class RobotHelperImpl implements RobotHelper {

	private int counter;
	private final List<Integer> firstFiveCompassReading;
	private float averageReading;
	//private final Dashboard dashboard;
	private final IRobotCreateInterface create;
	
	public RobotHelperImpl(
			//Dashboard dashboard, 
			IRobotCreateInterface create) {
		
		firstFiveCompassReading = new ArrayList<Integer>();
		counter = 0;
		//this.dashboard = dashboard;
		this.create = create;
	}
	
	@Override
	public void findTheBeacon() {
		//TODO Create method
	}
	
	@Override
	public void stop() {
		//TODO Create method
	}
	
	@Override
	public void goDistance(int distance) {
		//TODO Create method
	}
	
	@Override
	public void goDistanceFromWall(int distanceFromWall) {
		//TODO Create method
	}
	
	@Override
	public void averageList() {
		
		for (int i = 0; i < RobotHelperConstants.NUMBER_OF_READINGS_TO_AVERAGE; i++) {
			
			averageReading += firstFiveCompassReading.get(i);
		}
		
		averageReading = averageReading / RobotHelperConstants.NUMBER_OF_READINGS_TO_AVERAGE;
	}
	
	@Override
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
	
	@Override
	public void addAverageCompassReading(int reading) {
		
		this.firstFiveCompassReading.add(reading);
	}
	
	@Override
	public int getCounter() {
		
		return counter;
	}
	@Override
	public void setCounter(int counter) {
		
		this.counter = counter;
	}
	
	@Override
	public List<Integer> getAverageCompassReading() {
		
		return firstFiveCompassReading;
	}
	
	@Override
	public float getAverageReading() {
		
		return averageReading;
	}
	
	@Override
	public void setAverageReading(float averageReading) {
		
		this.averageReading = averageReading;
	}

	@Override
	public void incrementCounter() {
		
		counter++;
	}

	
}
