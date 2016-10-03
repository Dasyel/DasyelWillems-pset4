package com.dasyel.dasyelwillems_pset4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.dasyel.dasyelwillems_pset4.NoteDb.Note;
import com.dasyel.dasyelwillems_pset4.NoteDb.NoteDbWrapper;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/*
This activity displays the toDoItem and lets the user edit it
The item is always saved before the activity stops
 */
public class ToDoItem extends AppCompatActivity {
    private NoteDbWrapper noteDbWrapper;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item);

        // Get the noteId of the note to be loaded
        SharedPreferences sp = getDefaultSharedPreferences(this);
        long noteId = sp.getLong(getString(R.string.NOTE_ID_KEY), -1);

        // Load the note from the database
        noteDbWrapper = new NoteDbWrapper(this);
        note = noteDbWrapper.read(noteId);

        // Set the title and contents according to the note object
        EditText titleView = (EditText) findViewById(R.id.title);
        EditText contentsView = (EditText) findViewById(R.id.contents);
        titleView.setText(note.title);
        contentsView.setText(note.contents);
    }

    // Add the save button to the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_button, menu);
        return true;
    }

    // Handle the save button pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        goBack();
        return true;
    }

    // Handle the backbutton pressed
    @Override
    public void onBackPressed() {
        goBack();
    }

    // Save the note if the activity is paused in any way
    @Override
    protected void onPause() {
        super.onPause();
        EditText titleView = (EditText) findViewById(R.id.title);
        EditText contentsView = (EditText) findViewById(R.id.contents);
        Note newNote = new Note(note.id, titleView.getText().toString(),
                contentsView.getText().toString());
        noteDbWrapper.update(newNote);
    }

    // Go back to ToDoList activity
    public void goBack(){
        Intent intent = new Intent(this, ToDoList.class);
        startActivity(intent);
        finish();
    }
}
