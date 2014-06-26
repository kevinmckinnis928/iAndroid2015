package org.wintrisstech.erik.iaroc;

import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.api.IOIO;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.wintrisstech.sensors.UltraSonicSensors;

import android.os.SystemClock;

public class RobotHelperImpl {

	private int counter;
	private final List<Integer> firstFiveCompassReading;
	private float averageReading;
	private final Dashboard dashboard;
	private final IRobotCreateInterface create;
	private boolean firstPass = true;
	private int commandAzimuth;
	private int initialHeading;
	
	public RobotHelperImpl(
			Dashboard dashboard, 
			IRobotCreateInterface create) {
		
		firstFiveCompassReading = new ArrayList<Integer>();
		counter = 0;
		this.dashboard = dashboard;
		this.create = create;
	}
	public void setInitialHeading(int initialHeading){
		this.initialHeading = initialHeading;
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
	public void basicturn(int rightWheel, int leftWheel) throws ConnectionLostException{
		create.driveDirect(rightWheel, leftWheel);
		SystemClock.sleep(250);
	}
	
	
	public void turn(int commandAngle) throws ConnectionLostException {
		// Doesn't work for turns through 360
		int startAzimuth = 0;
		if (firstPass) {
			startAzimuth += readCompass();
			commandAzimuth = (startAzimuth + commandAngle) % 360;
			dashboard.log("commandaz = " + commandAzimuth + " startaz = "
					+ startAzimuth);
			firstPass = false;
		}
		int currentAzimuth = readCompass();
		dashboard.log("now = " + currentAzimuth);
		if (currentAzimuth >= commandAzimuth) {
			create.driveDirect(0,0);
			firstPass = true;
			dashboard.log("finalaz = " + readCompass());
		}
	}
	
	//BUMP SENSOR CHECK
	public void bumpCheckStraight() throws ConnectionLostException, InterruptedException{
		create.readSensors(create.SENSORS_BUMPS_AND_WHEEL_DROPS);
		if (create.isBumpLeft() && create.isBumpRight()) {
			dashboard.log("BOTH BUMPERS");
			create.driveDirect(-100, -100);
			Thread.sleep(1000);
			basicturn(100,-100);//turn left
		}
		if (create.isBumpRight() && !create.isBumpLeft()) {
			dashboard.log("RIGHT BUMPER");
			create.driveDirect(-100, -100);
			Thread.sleep(1000);
			basicturn(100,-100);//turn left
		}
		
		if(create.isBumpLeft() && !create.isBumpRight()) {
			dashboard.log("LEFT BUMPER");
			create.driveDirect(-100, -100);
			Thread.sleep(1000);
			basicturn(-100,100);
		}
	}
	
	public void bumpCheckGoldRush() throws ConnectionLostException, InterruptedException{
		create.readSensors(create.SENSORS_BUMPS_AND_WHEEL_DROPS);
		if (create.isBumpRight() || create.isBumpLeft()) {//front
			dashboard.log("BOTH BUMPERS");
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
	 
	 public int readCompass() {
			return (int) (dashboard.getAzimuth() + 360) % 360;

		}
	 /*public void filteredReading() throws ConnectionLostException
	 {int count = 0;
	 	int rightAverage = 0;
	 	int leftAverage = 0;
		 for (int i = 0; i < 5 ; i++) {

			 		}
	 }*/
}

