package com.swe.prototype.activities;

import android.os.Bundle;

import com.swe.prototype.R;

public class NoteActivity extends BaseActivity {

	private static final String TAG = "NoteActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);

	}
}
