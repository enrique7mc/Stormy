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
public class Hour implements Parcelable{
	private long mTime;
	private String mSummary;
	private double mTemperature;
	private String mIcon;
	private String mTimezone;
	private static final SimpleDateFormat formatter =
			new SimpleDateFormat("h a", Locale.getDefault());

	public Hour() {
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

	public int getTemperature() {
		return (int)Math.round(mTemperature);
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
		formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
	}

	private Hour(JSONObject data) throws JSONException {
		this.mTime = data.getLong("time");
		this.mSummary = data.getString("summary");
		this.mTemperature = Forecast.FahrenheitToCelsius(data.getDouble("temperature"));
		this.mIcon = data.getString("icon");
	}

	public static Hour[] getHourlyForecast(String jsonData) throws JSONException {
		JSONObject forecast = new JSONObject(jsonData);
		String timezone = Forecast.formatTimezone(forecast.getString("timezone"));

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

	public int getIconId() {
		return Forecast.getIconId(mIcon);
	}

	public String getHour() {
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
		dest.writeDouble(mTemperature);
		dest.writeString(mIcon);
		dest.writeString(mTimezone);
	}

	private Hour(Parcel in) {
		mTime = in.readLong();
		mSummary = in.readString();
		mTemperature = in.readDouble();
		mIcon = in.readString();
		mTimezone = in.readString();
	}

	public static final Creator<Hour> CREATOR = new Creator<Hour>() {
		@Override
		public Hour createFromParcel(Parcel source) {
			return new Hour(source);
		}

		@Override
		public Hour[] newArray(int size) {
			return new Hour[size];
		}
	};
}
