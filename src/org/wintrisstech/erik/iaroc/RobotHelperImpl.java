package org.wintrisstech.erik.iaroc;

import ioio.lib.api.exception.ConnectionLostException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.SystemClock;

public class RobotHelperImpl {

	private int counter;
	private final List<Integer> firstFiveCompassReading;
	private float averageReading;
	private final Dashboard dashboard;
	private final IRobotCreateInterface create;
	
	public RobotHelperImpl(
			Dashboard dashboard, 
			IRobotCreateInterface create) {
		
		firstFiveCompassReading = new ArrayList<Integer>();
		counter = 0;
		this.dashboard = dashboard;
		this.create = create;
	}
	public void spinAround(int spinTime) throws ConnectionLostException {
		spinTime = Math.abs(spinTime);
		create.driveDirect(400, -400);
		dashboard.log("spinTime " + spinTime);
		SystemClock.sleep(spinTime * 1000);
		dashboard.log("finished sleeping");
		create.driveDirect(100, 100);
	}
	public int getRandomNumber(int range){
		return new Random().nextInt(range);
	}
	
	
	public void findTheBeacon() {
		//TODO Create method
	}
	
	public void stop() {
		//TODO Create method
	}
	
	
	public void goDistance(int distance) {
		//TODO Create method
	}
	
	
	public void goDistanceFromWall(int distanceFromWall) {
		//TODO Create method
	}
	
	
	public void averageList() {
		
		for (int i = 0; i < RobotHelperConstants.NUMBER_OF_READINGS_TO_AVERAGE; i++) {
			
			averageReading += firstFiveCompassReading.get(i);
		}
		
		averageReading = averageReading / RobotHelperConstants.NUMBER_OF_READINGS_TO_AVERAGE;
	}
	
	
	public void raceInAStraightLine(int compassReading) throws ConnectionLostException, InterruptedException {
		
		
		
		if (compassReading == averageReading) {
			
			create.driveDirect(RobotHelperConstants.FASTWHEEL, RobotHelperConstants.FASTWHEEL);
		}
		else if (compassReading > averageReading) {
			
			create.driveDirect(RobotHelperConstants.FASTWHEEL, RobotHelperConstants.SLOWWHEEL);
		}
		else if (compassReading < averageReading) {
			
			create.driveDirect(RobotHelperConstants.SLOWWHEEL, RobotHelperConstants.FASTWHEEL);
		}
		
		bumpCheck();
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

	public void	spinTimer() throws ConnectionLostException, InterruptedException {
		int r = getRandomNumber(6) - 3;
		spinAround(r);
		
		
	}
	
	
	//BUMP SENSOR CHECK
	public void bumpCheck() throws ConnectionLostException, InterruptedException{
		create.readSensors(create.SENSORS_BUMPS_AND_WHEEL_DROPS);
		if (create.isBumpRight() && create.isBumpLeft()) {
			dashboard.log("BOTH BUMPERS");
			create.driveDirect(-100, -100);
			Thread.sleep(1000);
			spinTimer();
		}
		else if (create.isBumpRight()) {
			dashboard.log("RIGHT BUMPER");
			create.driveDirect(-100, -100);
			Thread.sleep(1000);
			spinTimer();
		}
		
		else if (create.isBumpLeft()) {
			dashboard.log("LEFT BUMPER");
			create.driveDirect(-100, -100);
			Thread.sleep(1000);
			spinTimer();
		}
	}

	//IR SENSOR CHECK
	 public void iRSensor() throws ConnectionLostException{
		 create.readSensors(create.SENSORS_INFRARED_BYTE);
		 int ir = create.getInfraredByte();
		 dashboard.log("IR SENSOR " + ir);
	 }
}

