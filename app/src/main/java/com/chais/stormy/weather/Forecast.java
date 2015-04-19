package com.chais.stormy.weather;

import com.chais.stormy.R;

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

	public static double FahrenheitToCelsius(double tempFahrenheit) {
		return (tempFahrenheit - 32) * 5 / 9;
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

	public static int getIconId(String icon){
		switch (icon) {
			case "clear-day":
				return R.drawable.clear_day;
			case "clear-night":
				return R.drawable.clear_night;
			case "rain":
				return R.drawable.rain;
			case "snow":
				return R.drawable.snow;
			case "sleet":
				return R.drawable.sleet;
			case "wind":
				return R.drawable.wind;
			case "fog":
				return R.drawable.fog;
			case "cloudy":
				return R.drawable.cloudy;
			case "partly-cloudy-day":
				return R.drawable.partly_cloudy;
			case "partly-cloudy-night":
				return R.drawable.cloudy_night;
			default:
				return R.drawable.clear_day;
		}
	}

	public static String formatTimezone(String timezone) {
		return timezone.substring(timezone.indexOf("/") + 1)
					   .replace('_', ' ');
	}
}
