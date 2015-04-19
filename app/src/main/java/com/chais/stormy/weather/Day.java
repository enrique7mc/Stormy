package com.chais.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Enrique on 13/04/2015.
 */
public class Day implements Parcelable{
	private long mTime;
	private String mSummary;
	private double mTemperatureMax;
	private String mIcon;
	private String mTimezone;
	private static final SimpleDateFormat formatter =
			new SimpleDateFormat("EEEE", Locale.getDefault());

	public Day() {
		formatter.setTimeZone(TimeZone.getDefault());
	}

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

	public int getTemperatureMax() {
		return (int)Math.round(mTemperatureMax);
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
		formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
	}

	private Day(JSONObject data) throws JSONException {
		this.mTime = data.getLong("time");
		this.mSummary = data.getString("summary");
		this.mTemperatureMax = Forecast.FahrenheitToCelsius(data.getDouble("temperatureMax"));
		this.mIcon = data.getString("icon");
	}

	public static Day[] getDailyForecast(String jsonData) throws JSONException {
		JSONObject forecast = new JSONObject(jsonData);
		String timezone = Forecast.formatTimezone(forecast.getString("timezone"));

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

	public int getIconId() {
		return Forecast.getIconId(mIcon);
	}

	public String getDayOfTheWeek() {
		return formatter.format(new Date(mTime * 1000));
	}

	@Override
	public int describeContents() {
		return 0; // not used
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mTime);
		dest.writeString(mSummary);
		dest.writeDouble(mTemperatureMax);
		dest.writeString(mIcon);
		dest.writeString(mTimezone);
	}

	private Day(Parcel in) {
		mTime = in.readLong();
		mSummary = in.readString();
		mTemperatureMax = in.readDouble();
		mIcon = in.readString();
		mTimezone = in.readString();
	}

	public static final Creator<Day> CREATOR = new Creator<Day>() {
		@Override
		public Day createFromParcel(Parcel source) {
			return new Day(source);
		}

		@Override
		public Day[] newArray(int size) {
			return new Day[size];
		}
	};
}
