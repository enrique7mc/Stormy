package com.chais.stormy.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Enrique on 13/04/2015.
 */
public class Day {
	private long mTime;
	private String mSummary;
	private double mTemperatureMax;
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

	public double getTemperatureMax() {
		return mTemperatureMax;
	}

	public void setTemperatureMax(double temperatureMax) {
		mTemperatureMax = temperatureMax;
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

	private Day(JSONObject data) throws JSONException {
		this.mTime = data.getLong("time");
		this.mSummary = data.getString("summary");
		this.mTemperatureMax = data.getDouble("temperatureMax");
		this.mIcon = data.getString("icon");
	}

	public static Day[] getDailyForecast(String jsonData) throws JSONException {
		JSONObject forecast = new JSONObject(jsonData);
		String timezone = forecast.getString("timezone");

		JSONObject hourly = forecast.getJSONObject("daily");
		JSONArray data = hourly.getJSONArray("data");
		Day[] days = new Day[data.length()];
		for (int i = 0; i < days.length; i++) {
			JSONObject jsonHour = data.getJSONObject(i);
			days[i] = new Day(jsonHour);
			days[i].mTimezone = timezone;
		}

		return days;
	}
}
