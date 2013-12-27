package com.swe.prototype.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.swe.prototype.R;

public class ChangeNoteActivity extends BaseActivity {

	
	private static final String TAG = "NoteActivity";
	private int changeNotePos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		changeNotePos = b.getInt("pos");
		setContentView(R.layout.activity_changenote);
		TextView noteTitle = (TextView)findViewById(R.id.textview_notetitle_changenote);
		EditText noteText = (EditText)findViewById(R.id.edittext_changenote);
		noteTitle.setText(this.getNoteTitle(changeNotePos));
		noteText.setText(this.getNoteText(changeNotePos));
	}
	
	/*
	Die m�ssen wir �berschreiben, damit das optionsmen� nicht sichtbar ist.
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	
	public void onClickSave(View v){
		System.out.println("Save wurde gedr�ckt!");
		// hier muss dann der string aus dem edit text geholt werden und an changeNotePos in die datenbank geschrieben werden
	}
	public void onClickCancel(View v){
		this.finish();
	}

	private String getNoteText(int pos){
		String l = "Nicht vergessen hier den Text aus dem datenmodel zu holen\nNicht vergessen hier den Text aus dem datenmodel zu holen\nNicht vergessen hier den Text aus dem datenmodel zu holen\n\tNicht vergessen hier den Text aus dem datenmodel zu holen\nPosition aus der liste" +pos;
		return l;
	}
	private String getNoteTitle(int pos){
		String l = "Hole Titel aus Datenbestand: "+pos;
		return l;
	}
	
}