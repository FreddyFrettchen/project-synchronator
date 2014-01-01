package com.swe.prototype.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.swe.prototype.R;
import com.swe.prototype.globalsettings.Settings;
import com.swe.prototype.globalsettings.Tools;

public class ListNotesActivity extends BaseActivity {

	private static final String TAG = "ListNotesActivity";
	private final static int ADD_CONTACT_BUTTON = 0x126;
	private final static int REFRESH_CONTACT_BUTTON = 0x127;
	
	List notes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		ListView noteList = (ListView) this.findViewById(R.id.notelist);

		notes = this.getNotes(); // hier zieht der sich immer die neuen
									// aktuellen Notiz Daten
		ListAdapter adapter = new ArrayAdapter<String>(this,
				R.layout.layout_note, notes);

		noteList.setAdapter(adapter);
		noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				// �ber pos bekomme ich die pos, die die notiz in der liste
				// hatte, k�nnte man auch anders machen
				showNote(pos);

			}

		});
		noteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int pos, long id) {
				showContextMenu(pos);
				return false;
			}

		});

	}

	protected void showContextMenu(final int pos) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog contextMenu = builder.create();
		LayoutInflater inflator = getLayoutInflater();
		View optionDialogView = inflator.inflate(
				R.layout.dialog_change_delete_note, null);
		Button changeButton = (Button) optionDialogView
				.findViewById(R.id.change_button);
		Button deleteButton = (Button) optionDialogView
				.findViewById(R.id.delete_button);
		changeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeNote(pos);
				contextMenu.cancel();
			}
		});

		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteNote(pos);
				contextMenu.cancel();
			}
		});

		contextMenu.setView(optionDialogView);
		contextMenu.setTitle("actions");
		// falls man das �ber die 3 butten des Alertdialogs regeln m�chte
		/*
		 * contextMenu.setNeutralButton("Delete", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) {
		 * deleteNote(pos); } }); contextMenu.setPositiveButton("Change", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * changeNote(pos); } });
		 */
		// contextMenu.setNegativeButton("Cancel",null);
		contextMenu.show();

	}
	
	protected void createNote(){
		Intent intent = new Intent(ListNotesActivity.this,
				ChangeNoteActivity.class);
		Bundle b = new Bundle();
		b.putBoolean("createNewNote", true); 
		intent.putExtras(b);
		startActivity(intent);
		finish();
		
	}

	protected void deleteNote(int pos) {
		System.out.println("Delete note:" + pos);
		// hier muss der dann ne methode auf dem datenmodel aufrufen
	}

	private void changeNote(int pos) {
		// hier wollte ich eig das in nem Alert dialog realisieren, dass man die
		// notiz �ndern kann.
		// das klappt aber auf teufel komm raus nicht, weil ich nicht auf ein
		// edittext in dem alertdialog zugreifen kann.
		// daher ruf ich jetzt einfach nen neuen intent aus und lass die
		// noteActivity das handeln
		/*
		 * System.out.println("Change note:"+pos); AlertDialog.Builder
		 * changeNoteDialog = new AlertDialog.Builder(this); LayoutInflater
		 * inflator = getLayoutInflater(); View changeDialogView =
		 * inflator.inflate(R.layout.dialog_change_note, null);
		 * changeNoteDialog.setView(changeDialogView); EditText changeNote
		 * =(EditText) findViewById(R.id.edittext_change_note);
		 * 
		 * changeNote.setText("lalalla");
		 * changeNoteDialog.setTitle("Notiz: "+pos);
		 * changeNoteDialog.setPositiveButton("Save", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * 
		 * } }); changeNoteDialog.setNegativeButton("Cancel",null);
		 * changeNoteDialog.create().show();
		 */

		Intent intent = new Intent(ListNotesActivity.this,
				ChangeNoteActivity.class);
		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putBoolean("createNewNote", false);// Your id
		intent.putExtras(b); // Put your id to your next Intent
		startActivity(intent);
		finish();

	}

	/*
	 * Diese Methode versorgt unseren ListAdapter immer beim oncreate mit den
	 * aktuellen Notiz daten
	 */
	private List getNotes() {
		String[] noteNames = new String[] { "", "Note 1", "Frohe Wihnachten",
				"Wuenscht", "euch", "Dany", ":D" };

		ArrayList<String> noteNamesList = new ArrayList<String>();
		noteNamesList.addAll(Arrays.asList(noteNames));

		return noteNamesList;

	}

	public void showNote(int pos) {
		AlertDialog.Builder note = new AlertDialog.Builder(this);
		// hier werdend ann auch die entsprechenden werte aus der datenbank
		// gezogen
		note.setTitle("Notiz: " + pos);
		note.setMessage("Nicht vergessen hier den Text aus dem datenmodel zu holen\nNicht vergessen hier den Text aus dem datenmodel zu holen\nNicht vergessen hier den Text aus dem datenmodel zu holen\n\tNicht vergessen hier den Text aus dem datenmodel zu holen\nPosition aus der liste");
		note.setPositiveButton("Close", null);
		note.create().show();
	}
	
	private void refreshNotes() {
		
	}
	
	@Override
	protected void addClicked(){
		this.createNote();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ADD_CONTACT_BUTTON, 0, "add");
		menu.add(0, REFRESH_CONTACT_BUTTON, 0, "refresh");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_contacts, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case REFRESH_CONTACT_BUTTON:
			Log.i(TAG, "syncing notes");
			refreshNotes();
			return true;
		case ADD_CONTACT_BUTTON:
			show(ChangeNoteActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean onContextItemSelected(final MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.edit:
			Log.i(TAG, "edit:");
			return true;
		case R.id.move:
			Log.i(TAG, "move:");
			return true;
		case R.id.delete:
			Log.i(TAG, "delete:");
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
}
