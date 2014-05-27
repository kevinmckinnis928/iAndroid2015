package org.wintrisstech.erik.iaroc;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

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
	private boolean firstPass = true;;
	private int commandAzimuth;
	//private int initialHeading;
	private RobotHelper api;

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
		
		this.api = new RobotHelperImpl(create);
	}

	public void initialize() throws ConnectionLostException {
		
		dashboard.log("iAndroid2014 happy version 140509A");
		
		//initialHeading = readCompass();
	}
	
	/**
	 * This method is called repeatedly
	 * 
	 * @throws ConnectionLostException
	 */
	
	public void loop() throws ConnectionLostException {
		
		SystemClock.sleep(100);
		
		int compassReading = readCompass();
		
		dashboard.log(compassReading + "");
		
		if(api.getCounter() < RobotHelperConstants.NUMBER_OF_READINGS_TO_AVERAGE) {
			
			dashboard.log("counter: " + api.getCounter());
			
			api.addAverageCompassReading(readCompass());
			
			api.incrementCounter();
		}
		
		if(api.getCounter() == RobotHelperConstants.NUMBER_OF_READINGS_TO_AVERAGE) {
			
			api.averageList();
			
			api.incrementCounter();
		} else {
			
			api.raceInAStraightLine(readCompass());
		}
		
		
		
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
}