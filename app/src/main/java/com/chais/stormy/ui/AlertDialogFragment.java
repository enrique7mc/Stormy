package com.chais.stormy.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.chais.stormy.R;

/**
 * Created by Enrique on 06/04/2015.
 */
public class AlertDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Context context = getActivity();
		Bundle bundle = getArguments();
		String title = bundle.getString("title");
		String message = bundle.getString("message");

		return new AlertDialog.Builder(context).setTitle(title)
		       .setMessage(message)
			   .setPositiveButton(context.getString(R.string.error_ok_button_text), null)
			   .create();
	}
}
