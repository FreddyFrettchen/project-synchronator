package com.swe.prototype.activities;

import com.swe.prototype.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

public class NoteActivity extends BaseActivity {
	private static final String TAG = "NoteActivity";
	ProgressDialog dialog;
	
	private TextView title 	= null;
	private TextView text 	= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		getActionBar().setTitle("View Note");  
		title = (TextView)findViewById(R.id.note_title);
		text = (TextView)findViewById(R.id.note_text);
		
		title.setText(getIntent().getExtras().getString("title"));
		text.setText(getIntent().getExtras().getString("text"));
	}
}
