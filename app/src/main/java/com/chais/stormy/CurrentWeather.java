package com.chais.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Enrique on 07/04/2015.
 */
public class CurrentWeather {
	private String mIcon;
	private long mTime;
	private double mTemperature;
	private double mHumidity;
	private double mPrecipChance;
	private String mSummary;
	private String mTimeZone;
	private static final SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");

	public CurrentWeather() {
		formatter.setTimeZone(TimeZone.getDefault());
	}

	public String getTimeZone() {
		return mTimeZone;
	}

	public void setTimeZone(String timeZone) {
		mTimeZone = timeZone;
		formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
	}

	public String getIcon() {
		return mIcon;
	}

	public void setIcon(String icon) {
		mIcon = icon;
	}

	public long getTime() {
		return mTime;
	}

	public void setTime(long time) {
		mTime = time;
	}

	public int getTemperature() {
		return (int)Math.round(mTemperature);
	}

	public void setTemperature(double temperature) {
		mTemperature = temperature;
	}

	public double getHumidity() {
		return mHumidity;
	}

	public void setHumidity(double humidity) {
		mHumidity = humidity;
	}

	public int getPrecipChance() {
		return (int)Math.round(mPrecipChance * 100);
	}

	public void setPrecipChance(double precipChance) {
		mPrecipChance = precipChance;
	}

	public String getSummary() {
		return mSummary;
	}

	public void setSummary(String summary) {
		mSummary = summary;
	}

	public String getFormattedTime() {
		return formatter.format(new Date(mTime * 1000));
	}

	public int getIconId(){
		switch (mIcon) {
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

	public static double FahrenheitToCelsius(double tempFahrenheit) {
		return (tempFahrenheit - 32) * 5 / 9;
	}
}
