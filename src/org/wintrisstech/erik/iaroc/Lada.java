package org.wintrisstech.erik.iaroc;

/**************************************************************************
 * Happy version...ultrasonics working...Version 140427A...mods by Vic
 **************************************************************************/
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import org.wintrisstech.sensors.UltraSonicSensors;
import android.os.SystemClock;
import android.provider.Settings.System;

/**
 * A Lada is an implementation of the IRobotCreateInterface, inspired by Vic's
 * awesome API. It is entirely event driven.
 * 
 * @author Erik
 */
public class Lada extends IRobotCreateAdapter {
	private final Dashboard dashboard;
	public UltraSonicSensors sonar;

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
		// song(0, new int[]{58, 10});
	}

	public void initialize() throws ConnectionLostException {
		dashboard.log("iAndroid2014 happy version 140427A");
	}

	/**
	 * This method is called repeatedly
	 * 
	 * @throws ConnectionLostException
	 */
	public void loop() throws ConnectionLostException {
		go(100,100);
		go(1000);
		stop();
		SystemClock.sleep(5000);
	}

	public void go(int leftWheelSpeed, int rightWheelSpeed)
			throws ConnectionLostException {
		driveDirect(rightWheelSpeed, leftWheelSpeed);
	}

	public void stop() throws ConnectionLostException {
		driveDirect(0, 0);
	}

	public void go(int distanceToTravel) throws ConnectionLostException {
		int totalDistance = 0;
		while (distanceToTravel > totalDistance) {
			readSensors(SENSORS_DISTANCE);
			totalDistance += getDistance();
			dashboard.log(totalDistance + "");
		}
	}
}
