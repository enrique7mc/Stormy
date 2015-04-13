package com.chais.stormy;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private CurrentWeather mCurrentWeather;
	@InjectView(R.id.timeLabel) TextView mTimeLabel;
	@InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
	@InjectView(R.id.humidityValue) TextView mHumidityValue;
	@InjectView(R.id.precipValue) TextView mPrecipValue;
	@InjectView(R.id.summaryLabel) TextView mSummaryLabel;
	@InjectView(R.id.iconImageView) ImageView mIconImageView;
	@InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
	@InjectView(R.id.progressBar) ProgressBar mProgressBar;

	private final String mApiKey = "807f790501f3841dd94c54f8ef1e7ff2";
	private final double mLatitude = 9.460018;
	private final double mLongitude = -99.239414;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		mProgressBar.setVisibility(View.INVISIBLE);

		mRefreshImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getForecast(mLatitude, mLongitude);
			}
		});

		getForecast(mLatitude, mLongitude);

		Log.d(TAG, "Main UI code");
	}

	private void getForecast(double latitude, double longitude) {
		if(!isNetworkAvailable()) {
			networkUnavailableError();
			return;
		}

		toggleRefresh();

		String mForecastUrl = "https://api.forecast.io/forecast/" + mApiKey + "/" +
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
						mCurrentWeather = getCurrentDetails(jsonData);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								updateDisplay();
							}
						});

					} else{
						alertUserAboutError();
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
		mTemperatureLabel.setText(mCurrentWeather.getTemperature() + "");
		mTimeLabel.setText("At " + mCurrentWeather.getFormattedTime() + " it will be");
		mHumidityValue.setText(mCurrentWeather.getHumidity() + "");
		mPrecipValue.setText(mCurrentWeather.getPrecipChance() + "%");
		mSummaryLabel.setText(mCurrentWeather.getSummary());
		Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
		mIconImageView.setImageDrawable(drawable);
	}

	private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
		JSONObject forecast = new JSONObject(jsonData);
		String timezone = forecast.getString("timezone");

		JSONObject currently = forecast.getJSONObject("currently");
		CurrentWeather currentWeather = new CurrentWeather();
		currentWeather.setIcon(currently.getString("icon"));
		currentWeather.setTime(currently.getLong("time"));
		currentWeather.setTemperature(
				CurrentWeather.FahrenheitToCelsius(currently.getDouble("temperature")));
		currentWeather.setHumidity(currently.getDouble("humidity"));
		currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
		Log.i(TAG, "From JSON: " + currently.getDouble("precipProbability"));
		currentWeather.setSummary(currently.getString("summary"));
		currentWeather.setTimeZone(timezone);

		Log.i(TAG, currentWeather.getFormattedTime());

		return currentWeather;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager manager =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();

		return info != null && info.isConnected();
	}

	private void alertUserAboutError() {
		AlertDialogFragment dialog = new AlertDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", getString(R.string.error_title));
		bundle.putString("message", getString(R.string.error_message));
		dialog.setArguments(bundle);
		dialog.show(getFragmentManager(), "error_dialog");
	}

	private void networkUnavailableError() {
		AlertDialogFragment dialog = new AlertDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", getString(R.string.error_title));
		bundle.putString("message", getString(R.string.network_unavailable_message));
		dialog.setArguments(bundle);
		dialog.show(getFragmentManager(), "error_dialog");
	}
}
