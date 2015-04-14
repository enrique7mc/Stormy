package com.chais.stormy.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chais.stormy.R;
import com.chais.stormy.weather.Current;
import com.chais.stormy.weather.Forecast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity implements
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private Forecast mForecast;
	@InjectView(R.id.timeLabel) TextView mTimeLabel;
	@InjectView(R.id.locationLabel) TextView mLocationLabel;
	@InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
	@InjectView(R.id.humidityValue) TextView mHumidityValue;
	@InjectView(R.id.precipValue) TextView mPrecipValue;
	@InjectView(R.id.summaryLabel) TextView mSummaryLabel;
	@InjectView(R.id.iconImageView) ImageView mIconImageView;
	@InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
	@InjectView(R.id.progressBar) ProgressBar mProgressBar;

	private final String mApiKey = "807f790501f3841dd94c54f8ef1e7ff2";
	private double mLatitude = 0;
	private double mLongitude = 0;
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		mProgressBar.setVisibility(View.INVISIBLE);

		mRefreshImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getForecast();
			}
		});

		buildGoogleApiClient();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	private void getForecast() {
		if(!isNetworkAvailable()) {
			networkUnavailableError();
			return;
		}

		toggleRefresh();
		mGoogleApiClient.connect();

		Log.i(TAG, "Connected: " + mGoogleApiClient.isConnected());

		final String mForecastUrl = "https://api.forecast.io/forecast/" + mApiKey + "/" +
				mLatitude + "," + mLongitude;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
							.url(mForecastUrl)
							.build();

		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				toggleRefresh();
				alertUserAboutError();
			}

			@Override
			public void onResponse(Response response) throws IOException {
				toggleRefresh();
				try {
					String jsonData = response.body().string();
					Log.v(TAG, jsonData);
					if (response.isSuccessful()) {
						mForecast = new Forecast(jsonData);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								updateDisplay();
							}
						});
					} else{
						showErrorDialog(getString(R.string.error_title),
								getString(R.string.error_message));
					}
				} catch (IOException e) {
					Log.e(TAG, "Exception caught: ", e);
				} catch (JSONException e) {
					Log.e(TAG, "Json exception caught: ", e);
				}
			}
		});
	}

	private void toggleRefresh() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mProgressBar.getVisibility() == View.INVISIBLE) {
					mProgressBar.setVisibility(View.VISIBLE);
					mRefreshImageView.setVisibility(View.INVISIBLE);
				} else {
					mProgressBar.setVisibility(View.INVISIBLE);
					mRefreshImageView.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void updateDisplay() {
		Current current = mForecast.getCurrent();
		mTemperatureLabel.setText(current.getTemperature() + "");
		mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
		mHumidityValue.setText(current.getHumidity() + "");
		mPrecipValue.setText(current.getPrecipChance() + "%");
		mSummaryLabel.setText(current.getSummary());
		Drawable drawable = getResources().getDrawable(current.getIconId());
		mIconImageView.setImageDrawable(drawable);
		mLocationLabel.setText(current.getTimeZone());
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager manager =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();

		return info != null && info.isConnected();
	}

	private void alertUserAboutError() {
		showErrorDialog(getString(R.string.error_title),
				getString(R.string.error_message));
	}

	private void networkUnavailableError() {
		showErrorDialog(getString(R.string.error_title),
				getString(R.string.network_unavailable_message));
	}

	private void locationUnavailableError() {
		showErrorDialog(getString(R.string.error_title),
				getString(R.string.location_unavailable_message));
	}

	private void showErrorDialog(String title, String message) {
		AlertDialogFragment dialog = new AlertDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("message", message);
		dialog.setArguments(bundle);
		dialog.show(getFragmentManager(), "error_dialog");
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.i(TAG, "onConnected");
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);
		if (mLastLocation != null) {
			Log.i(TAG, String.valueOf(mLastLocation.getLatitude()));
			Log.i(TAG, String.valueOf(mLastLocation.getLongitude()));
			mLatitude = mLastLocation.getLatitude();
			mLongitude = mLastLocation.getLongitude();
			getForecast();
		} else {
			locationUnavailableError();
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i(TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " +
				connectionResult.getErrorCode());
	}
}
