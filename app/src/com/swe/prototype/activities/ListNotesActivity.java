package com.swe.prototype.activities;

import android.os.Bundle;

import com.swe.prototype.R;

public class ListNotesActivity extends BaseActivity {

	private static final String TAG = "ListNotesActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listnotes);
	}
}
