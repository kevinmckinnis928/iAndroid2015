package org.wintrisstech.erik.iaroc;

import ioio.lib.api.exception.ConnectionLostException;

import java.util.List;

public interface RobotHelper {

	public abstract void findTheBeacon();

	public abstract void stop();

	public abstract void goDistance(int distance);

	public abstract void goDistanceFromWall(int distanceFromWall);

	public abstract void averageList();

	public abstract void raceInAStraightLine(int compassReading)
			throws ConnectionLostException;

	public abstract void addAverageCompassReading(int reading);

	public abstract int getCounter();

	public abstract void setCounter(int counter);

	public abstract List<Integer> getAverageCompassReading();

	public abstract float getAverageReading();

	public abstract void setAverageReading(float averageReading);

	public abstract void incrementCounter();

}