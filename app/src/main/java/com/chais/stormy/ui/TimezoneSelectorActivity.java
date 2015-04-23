package com.chais.stormy.ui;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.chais.stormy.R;

public class TimezoneSelectorActivity extends ListActivity {
	private String[] timezones = {"Mexico City", "New York", "Paris", "London"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timezone_selector);

		ArrayAdapter<String> adapter =
				new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timezones);

		setListAdapter(adapter);
	}

}
