package com.swe.prototype.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.swe.prototype.R;

public class ChangeNoteActivity extends BaseActivity {

	
	private static final String TAG = "NoteActivity";
	private int changeNotePos;
	EditText noteText;
	EditText noteTitleEditText;
	TextView noteTitle;
	CheckBox synchronatorCheckbox;
	CheckBox googleCheckbox;
	CheckBox exchangeCheckbox;
	boolean createNewNote = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changenote);
		noteTitle = (TextView)findViewById(R.id.textview_notetitle_changenote);
		noteText = (EditText)findViewById(R.id.edittext_changenote);
		noteTitleEditText = (EditText)findViewById(R.id.edittext_notetitle_changenote); // hier könnte man versuchen das ding einzeilig zu machen
		synchronatorCheckbox = (CheckBox) findViewById(R.id.checkBox_synchronator);
		googleCheckbox = (CheckBox) findViewById(R.id.checkBox_google);
		exchangeCheckbox = (CheckBox) findViewById(R.id.checkBox_exchange);
		
		Bundle b = getIntent().getExtras(); 
		createNewNote = b.getBoolean("createNewNote");
		// da wir diese activity zum verändern und zum neuen Note erzeugen verwenden müssen wir beim onCreate und beim saven unterscheiden
		ActionBar actionbar = getActionBar();
		if(createNewNote){
			actionbar.setTitle("Create New Note");
			noteTitle.setText("Title: ");
			
			// checkboxen ausblenden falls user den account nicht hat.
			googleCheckbox.setVisibility(View.INVISIBLE);
			exchangeCheckbox.setVisibility(View.INVISIBLE);
		}
		else{
			actionbar.setTitle("Change Note");
			noteTitleEditText.setVisibility(View.INVISIBLE);
			changeNotePos = b.getInt("pos");
			noteTitle.setText(this.getNoteTitle(changeNotePos));
			noteText.setText(this.getNoteText(changeNotePos));
			//b.getInt // hier muss ich auch noch informationen mitschicken, von welchem account die daten kommen.
			// hier dann entsprechend setzen, von wo die daten kommen.
			// hier dann checkboxen, ausblebenden, je nachdem ob der user den entsprechenden account hinzugefuegt hat.
			googleCheckbox.setVisibility(View.INVISIBLE);
			exchangeCheckbox.setVisibility(View.INVISIBLE);
		}
		
		

		
		
		
		
		
	}
	
	/*
	Die muessen wir ueberschreiben, damit das optionsmenue nicht sichtbar ist.
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	
	public void onClickSave(View v){
		System.out.println("Save wurde gedrueckt!");
		System.out.println("Text: "+noteText.getText());
		
		if(this.createNewNote){
			// neue anlegen
			
		}
		else{
			// geänderte notiz speichern
			
		}
		// hier muss dann der string aus dem edit text geholt werden und an changeNotePos in die datenbank geschrieben werden
		int checkedCheckboxCounter =0;
		if(synchronatorCheckbox.isChecked()){
			checkedCheckboxCounter++;
			System.out.println("Synchronator is checked!");
		}
		if(googleCheckbox.isChecked()){
			checkedCheckboxCounter++;
			System.out.println("google is checked!");
		}
		if(exchangeCheckbox.isChecked()){
			checkedCheckboxCounter++;
			System.out.println("Exchange is checked!");
		}

		if(checkedCheckboxCounter==0){
			Toast.makeText(getApplicationContext(), "You have to check at least one checkbox!", Toast.LENGTH_SHORT).show();
			return;
		}
		//this.onBackPressed(); und this.finish() funktioniert nicht um zurueck zur notizliste zu kommen
		Intent intent = new Intent(ChangeNoteActivity.this,ListNotesActivity.class);
		startActivity(intent);
		finish();
	}
	public void onClickCancel(View v){
		Intent intent = new Intent(this,ListNotesActivity.class);
		startActivity(intent);
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
