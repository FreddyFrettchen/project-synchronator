package com.swe.prototype.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.swe.prototype.R;

public class ChangeNoteActivity extends BaseActivity {

	
	private static final String TAG = "NoteActivity";
	private int changeNotePos;
	EditText noteText;
	CheckBox synchronatorCheckbox;
	CheckBox googleCheckbox;
	CheckBox exchangeCheckbox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras(); // hier muss ich auch noch informationen mitschicken, von welchem account die daten kommen.
		changeNotePos = b.getInt("pos");
		setContentView(R.layout.activity_changenote);
		// hier dann entsprechend setzen, von wo die daten kommen.
		// hier dann checkboxen, ausblebenden, je nachdem ob der user den entsprechenden account hinzugefügt hat.
		synchronatorCheckbox = (CheckBox) findViewById(R.id.checkBox_synchronator);
		googleCheckbox = (CheckBox) findViewById(R.id.checkBox_google);
		exchangeCheckbox = (CheckBox) findViewById(R.id.checkBox_exchange);
		
		TextView noteTitle = (TextView)findViewById(R.id.textview_notetitle_changenote);
		noteText = (EditText)findViewById(R.id.edittext_changenote);
		noteTitle.setText(this.getNoteTitle(changeNotePos));
		noteText.setText(this.getNoteText(changeNotePos));
	}
	
	/*
	Die müssen wir überschreiben, damit das optionsmenü nicht sichtbar ist.
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	
	public void onClickSave(View v){
		System.out.println("Save wurde gedrückt!");
		System.out.println("Text: "+noteText.getText());
		// hier muss dann der string aus dem edit text geholt werden und an changeNotePos in die datenbank geschrieben werden
		if(synchronatorCheckbox.isChecked()){
			System.out.println("Synchronator is checked!");
		}
		if(googleCheckbox.isChecked()){
			System.out.println("google is checked!");
		}
		if(exchangeCheckbox.isChecked()){
			System.out.println("Exchange is checked!");
		}
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
