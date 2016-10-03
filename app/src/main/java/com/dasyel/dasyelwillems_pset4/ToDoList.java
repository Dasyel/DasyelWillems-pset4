package com.dasyel.dasyelwillems_pset4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dasyel.dasyelwillems_pset4.Adapter.NoteListAdapter;
import com.dasyel.dasyelwillems_pset4.NoteDb.Note;
import com.dasyel.dasyelwillems_pset4.NoteDb.NoteDbWrapper;

import java.util.ArrayList;
import java.util.Arrays;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/*
This activity shows the list of toDoItems and handles the creation and deletion of notes
 */
public class ToDoList extends AppCompatActivity {

    private NoteDbWrapper noteDbWrapper;
    private NoteListAdapter noteListAdapter;
    private ArrayList<Note> noteArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        // Remove the note id from sharedPreferences such that the Dispatcher activity starts
        // this activity
        SharedPreferences sp = getDefaultSharedPreferences(this);
        sp.edit().remove(getString(R.string.NOTE_ID_KEY)).apply();

        // Get all notes from the database and add them to the listView
        noteDbWrapper = new NoteDbWrapper(this);
        Note[] noteArray = noteDbWrapper.readAll();
        noteArrayList = new ArrayList<>(Arrays.asList(noteArray));
        ListView listView = (ListView) findViewById(R.id.noteListView);
        noteListAdapter = new NoteListAdapter(this, R.layout.list_row, noteArrayList);
        listView.setAdapter(noteListAdapter);

        // Set onClickListener for transition to ToDoItem activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Note note = (Note) parent.getItemAtPosition(position);

                SharedPreferences sp = getDefaultSharedPreferences(ToDoList.this);
                sp.edit().putLong(getString(R.string.NOTE_ID_KEY), note.id).apply();

                Intent intent = new Intent(ToDoList.this, ToDoItem.class);
                startActivity(intent);
                finish();
            }
        });

        // Set onClickListener for deletion of items
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) parent.getItemAtPosition(position);
                noteDbWrapper.delete(note);
                noteArrayList.remove(note);
                noteListAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    // Add the plus sign button to the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_button, menu);
        return true;
    }

    // Handle the plus sign button pressed to create a note and go to its ToDoItem activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Note note = noteDbWrapper.create();
        SharedPreferences sp = getDefaultSharedPreferences(this);
        sp.edit().putLong(getString(R.string.NOTE_ID_KEY), note.id).apply();
        Intent intent = new Intent(this, ToDoItem.class);
        startActivity(intent);
        finish();
        return true;
    }
}
