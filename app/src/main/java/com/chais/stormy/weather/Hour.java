package com.chais.stormy.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Enrique on 13/04/2015.
 */
public class Hour {
	private long mTime;
	private String mSummary;
	private double mTemperature;
	private String mIcon;
	private String mTimezone;

	public long getTime() {
		return mTime;
	}

	public void setTime(long time) {
		mTime = time;
	}

	public String getSummary() {
		return mSummary;
	}

	public void setSummary(String summary) {
		mSummary = summary;
	}

	public double getTemperature() {
		return mTemperature;
	}

	public void setTemperature(double temperature) {
		mTemperature = temperature;
	}

	public String getIcon() {
		return mIcon;
	}

	public void setIcon(String icon) {
		mIcon = icon;
	}

	public String getTimezone() {
		return mTimezone;
	}

	public void setTimezone(String timezone) {
		mTimezone = timezone;
	}

	private Hour(JSONObject data) throws JSONException {
		this.mTime = data.getLong("time");
		this.mSummary = data.getString("summary");
		this.mTemperature = data.getDouble("temperature");
		this.mIcon = data.getString("icon");
	}

	public static Hour[] getHourlyForecast(String jsonData) throws JSONException {
		JSONObject forecast = new JSONObject(jsonData);
		String timezone = forecast.getString("timezone");

		JSONObject hourly = forecast.getJSONObject("hourly");
		JSONArray data = hourly.getJSONArray("data");
		Hour[] hours = new Hour[data.length()];
		for (int i = 0; i < hours.length; i++) {
			JSONObject jsonHour = data.getJSONObject(i);
			hours[i] = new Hour(jsonHour);
			hours[i].mTimezone = timezone;
		}

		return hours;
	}
}
