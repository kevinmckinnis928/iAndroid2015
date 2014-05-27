package org.wintrisstech.erik.iaroc;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

import java.util.ArrayList;
import java.util.List;

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
	private int initialHeading;
	private static final int FASTWHEEL = 200;
	private static final int SLOWWHEEL = FASTWHEEL - 10;
	private int counter = 0;
	private List<Integer> averageCompassReading = new ArrayList<Integer>();
	private float averageReading = 50;

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
	}

	public void initialize() throws ConnectionLostException {
		dashboard.log("iAndroid2014 happy version 140509A");
		 initialHeading = readCompass();
		 //driveDirect(100, 100);
	}
	public void averageList(){
		for (int i = 0; i < 5; i++) {
			averageReading += averageCompassReading.get(i);
		}
		averageReading = averageReading/5;
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
		if (compassReading == averageReading) {
			driveDirect(FASTWHEEL, FASTWHEEL);
		}
		else if (compassReading > averageReading) {
			driveDirect(FASTWHEEL, SLOWWHEEL);
		}
		else if (compassReading < averageReading) {
			driveDirect(SLOWWHEEL, FASTWHEEL);
		}
		/*if(counter < 5) {
			dashboard.log("counter: " + counter);
			averageCompassReading.add(readCompass());
			counter++;
		}
		if(counter == 5) {
			//averageList();
			raceInAStraightLine(readCompass());
		}
		*/
		
		
	}

	private void raceInAStraightLine(int compassReading) throws ConnectionLostException {
		if (compassReading == averageReading) {
			driveDirect(FASTWHEEL, FASTWHEEL);
		}
		else if (compassReading > averageReading) {
			driveDirect(FASTWHEEL, SLOWWHEEL);
		}
		else if (compassReading < averageReading) {
			driveDirect(SLOWWHEEL, FASTWHEEL);
		}
	}

	public void turn(int commandAngle) throws ConnectionLostException // Doesn't
																		// work
																		// for
																		// turns
																		// through
																		// 360
	{
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