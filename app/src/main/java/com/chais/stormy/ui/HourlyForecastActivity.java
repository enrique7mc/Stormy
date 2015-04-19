package com.chais.stormy.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.chais.stormy.R;
import com.chais.stormy.adapters.HourAdapter;
import com.chais.stormy.weather.Hour;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HourlyForecastActivity extends ActionBarActivity {

	private Hour[] mHours;
	@InjectView(R.id.recyclerView) RecyclerView mRecyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hourly_forecast);
		ButterKnife.inject(this);

		Intent intent = getIntent();
		Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
		mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

		HourAdapter adapter = new HourAdapter(mHours);
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setHasFixedSize(true);
	}
}
