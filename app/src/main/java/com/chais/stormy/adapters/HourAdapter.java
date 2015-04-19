package com.chais.stormy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chais.stormy.R;
import com.chais.stormy.weather.Hour;

/**
 * Created by Enrique on 19/04/2015.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
	private Hour[] mHours;

	public HourAdapter(Hour[] hours) {
		mHours = hours;
	}

	@Override
	public HourViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.hourly_list_item, viewGroup, false);
		return new HourViewHolder(view);
	}

	@Override
	public void onBindViewHolder(HourViewHolder hourViewHolder, int i) {
		hourViewHolder.bindHour(mHours[i]);
	}

	@Override
	public int getItemCount() {
		return mHours.length;
	}

	public class HourViewHolder extends RecyclerView.ViewHolder {
		public TextView mTimeLabel;
		public TextView mSummaryLabel;
		public TextView mTemperatureLabel;
		public ImageView mIconImageView;

		public HourViewHolder(View itemView) {
			super(itemView);

			mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
			mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
			mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
			mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
		}

		public void bindHour(Hour hour) {
			mTimeLabel.setText(hour.getHour());
			mSummaryLabel.setText(hour.getSummary());
			mTemperatureLabel.setText(hour.getTemperature() + "°");
			mIconImageView.setImageResource(hour.getIconId());
		}
	}
}
