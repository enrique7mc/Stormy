package com.chais.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chais.stormy.R;
import com.chais.stormy.adapters.DayAdapter;
import com.chais.stormy.weather.Day;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DailyForecastActivity extends ListActivity {
	private Day[] mDays;
	@InjectView(R.id.dailyLocationLabel) TextView mLocationLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_forecast);
		ButterKnife.inject(this);

		Intent intent = getIntent();
		Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
		String timezone = intent.getStringExtra(MainActivity.TIMEZONE);
		mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

		DayAdapter adapter = new DayAdapter(this, mDays);
		setListAdapter(adapter);

		mLocationLabel.setText(timezone);
	}
}
