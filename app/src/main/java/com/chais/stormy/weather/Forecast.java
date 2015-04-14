package com.chais.stormy.weather;

import org.json.JSONException;

/**
 * Created by Enrique on 13/04/2015.
 */
public class Forecast {
	private Current mCurrent;
	private Hour[] mHourlyForecast;
	private Day[] mDailyForecast;

	public Forecast(String jsonData) throws JSONException {
		mCurrent = new Current(jsonData);
		mHourlyForecast = Hour.getHourlyForecast(jsonData);
		mDailyForecast = Day.getDailyForecast(jsonData);
	}

	public Current getCurrent() {
		return mCurrent;
	}

	public void setCurrent(Current current) {
		mCurrent = current;
	}

	public Hour[] getHourlyForecast() {
		return mHourlyForecast;
	}

	public void setHourlyForecast(Hour[] hourlyForecast) {
		mHourlyForecast = hourlyForecast;
	}

	public Day[] getDailyForecast() {
		return mDailyForecast;
	}

	public void setDailyForecast(Day[] dailyForecast) {
		mDailyForecast = dailyForecast;
	}
}
