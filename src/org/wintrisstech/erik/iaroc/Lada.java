package org.wintrisstech.erik.iaroc;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

import java.util.Random;

import org.wintrisstech.sensors.UltraSonicSensors;

import android.os.SystemClock;

/**
 * A Lada is an implementation of the IRobotCreateInterface, inspired by Vic's
 * awesome API. It is entirely event driven.
 * 
 * @author Erik
 */
public class Lada extends IRobotCreateAdapter {
	private final Dashboard dashboard;
	public UltraSonicSensors sonar;
	private boolean firstPass = true;
	private int commandAzimuth;
	// private int initialHeading;
	private RobotHelperImpl api;
	private int counter = 1;
	private int initialHeading;
	public static final boolean LEFT = false;
	public static final boolean RIGHT = true;
	/**
	 * Constructs a Lada, an amazing machine!
	 * 
	 * @param ioio
	 *            the IOIO instance that the Lada can use to communicate with
	 *            other peripherals such as sensors
	 * @param create
	 *            an implementation of an iRobot
	 * @param dashboard
	 *            the Dashboard instance that is connected to the Lada
	 * @throws ConnectionLostException
	 */
	public Lada(IOIO ioio, IRobotCreateInterface create, Dashboard dashboard)
			throws ConnectionLostException {

		super(create);

		sonar = new UltraSonicSensors(ioio);

		this.dashboard = dashboard;

		this.api = new RobotHelperImpl(dashboard, this);
	}
	
public void raceInAStraightLine(int compassReading) throws ConnectionLostException, InterruptedException {
		if(compassReading > 180){
			compassReading = compassReading - 360;
		}
		if(Math.abs(compassReading - initialHeading) < 5){
			driveDirect(450,500);
			dashboard.log("Within Normal Range = " + compassReading);
		}
		else if (compassReading > initialHeading) {//turn right
			dashboard.log("Right turn Reading = " + compassReading);
			driveDirect(400,500);
			SystemClock.sleep(500);
			driveDirect(500,500);
		}
		else if (compassReading < initialHeading) {//turn left
			
			dashboard.log("Left turn Reading = " + compassReading);
			driveDirect(500,400);
			SystemClock.sleep(500);
			driveDirect(500,500);
			
		}
		api.bumpCheckStraight();
		
//		if (compassReading == initialHeading) {
//			dashboard.log("Equal Reading = " + compassReading);
//			driveDirect(400,400);
//		}
//		if (compassReading > initialHeading) {//turn right
//			dashboard.log("Right turn Reading = " + compassReading);
//			driveDirect(500,400);
//			//driveDirect(0, 0);
//			SystemClock.sleep(25);
//		}
//		if (compassReading < initialHeading) {//turn left
//			
//			dashboard.log("Left turn Reading = " + compassReading);
//			driveDirect(400,500);
//			//driveDirect(0, 0);
//			SystemClock.sleep(25);
//		}
		//bumpCheckStraight();
		
	}

	public void initialize() throws ConnectionLostException {

		dashboard.log("iAndroid2014 happy version 140509A");

		  initialHeading = readCompass();
		  if(initialHeading > 180){
			  initialHeading = initialHeading - 360;
		  }
		  dashboard.log("initialHeading = " + initialHeading );
		  
	}

	/**
	 * This method is called repeatedly
	 * 
	 * @throws ConnectionLostException
	 * @throws InterruptedException 
	 */

	public void loop() throws ConnectionLostException, InterruptedException {
		//dragRace();
		maze(RIGHT);
		
		//sonar.read();
	//dashboard.log("FRONT DISTANCE " + sonar.getFrontDistance());
	//dashboard.log("LEFT DISTANCE " +sonar.getLeftDistance());
	//dashboard.log("RIGHT DISTANCE " + sonar.getRightDistance());
		

	}
	public void forward() throws ConnectionLostException{
		driveDirect(500,500);
	}
	public void dragRace() throws ConnectionLostException, InterruptedException {

		int compassReading = readCompass();

		dashboard.log(compassReading + "....");
		
		raceInAStraightLine(compassReading);
//		sonar.read();
//		dashboard.log(" right " + sonar.getRightDistance() + " left " + sonar.getLeftDistance());
//		if(sonar.getRightDistance() > 100 || sonar.getLeftDistance() > 100 ||
//				sonar.getRightDistance()+sonar.getLeftDistance() < 60)
//		{
//		}
//		else
//		{
//			if(sonar.getRightDistance() < 20) {
//			api.setAverageReading(api.getAverageReading() - 100);
//			dashboard.log("turn left" + sonar.getRightDistance());
//			} 
//			else if(sonar.getLeftDistance() < 20){
//				api.setAverageReading(api.getAverageReading() + 100);
//				dashboard.log("turn right" + sonar.getLeftDistance());
//			}
//
////		}
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
			driveDirect(0, 0);
			firstPass = true;
			dashboard.log("finalaz = " + readCompass());
		}
	}

	public int readCompass() {
		return (int) (dashboard.getAzimuth() + 360) % 360;

	}
	
	public void maze(boolean wall) throws ConnectionLostException
	{
		if(wall == RIGHT){
			bumpCheckMaze(LEFT);
			driveDirect(200, 500);
		}
		if(wall == LEFT){
			bumpCheckMaze(RIGHT);
			driveDirect(500, 200);
		}
	}
	public void bumpCheckMaze(boolean direction) throws ConnectionLostException{
		readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
		if(direction == RIGHT){
			if(isBumpLeft() || isBumpRight()) {
				
			driveDirect(-200,-500);
			SystemClock.sleep(100);
			turnAngle(45, RIGHT);
			}
		}
		if(direction == LEFT){
			if(isBumpLeft() || isBumpRight()) {
				
			driveDirect(-500,-200);
			SystemClock.sleep(100);
			turnAngle(45, LEFT);
			}
		}

	}
	public void turnAngle(int angle,boolean direction) throws ConnectionLostException{
		double angleSoFar = 0;
		
		if (direction == true){
			driveDirect(-100, 100);
		}
		else if (direction == false) {
			driveDirect(100, -100);	
		}
		while(angleSoFar < angle){
			readSensors(SENSORS_ANGLE);
			angleSoFar += Math.abs(getAngle());
			
		}
		driveDirect(0, 0);
	}
}