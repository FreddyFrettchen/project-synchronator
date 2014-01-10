package com.swe.prototype.activities;

import com.swe.prototype.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class NoteActivity extends Activity {
	private static final String TAG = "NoteActivity";
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changenote);
	}
}
