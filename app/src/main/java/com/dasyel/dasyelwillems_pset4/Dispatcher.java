package com.dasyel.dasyelwillems_pset4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dasyel.dasyelwillems_pset4.NoteDb.Note;
import com.dasyel.dasyelwillems_pset4.NoteDb.NoteDbWrapper;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/*
This activity simply starts the activity which was last open
This activity also adds the notes for the initial start
 */
public class Dispatcher extends AppCompatActivity {
    private static final String floppyEmoji = new String(Character.toChars(0x1F4BE));
    private static final String noteEmoji = new String(Character.toChars(0x1F4C3));
    private static final String openEmoji = new String(Character.toChars(0x1F4C2));
    private static final String fireEmoji = new String(Character.toChars(0x1F525));
    private static final String plusEmoji = new String(Character.toChars(0x2795));
    private static final String[][] initMessages = {
            {openEmoji+" Open Note", "touch a note to open it"},
            {noteEmoji+" Add Note", "press the" + plusEmoji + "button at the top right"},
            {floppyEmoji + " Save Note", "press the "+ floppyEmoji +
                    " button at the top right or just press back"},
            {fireEmoji+" Delete Note", "Long press a note in the list"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher);
        SharedPreferences sp = getDefaultSharedPreferences(this);
        boolean initialStart = sp.getBoolean("init", true);

        //If app is started for first time: add all initMessages as notes
        if(initialStart){
            NoteDbWrapper noteDbWrapper = new NoteDbWrapper(this);
            for(String[] msg: initMessages) {
                Note note = noteDbWrapper.create();
                Note newNote = new Note(note.id, msg[0], msg[1]);
                noteDbWrapper.update(newNote);
            }
            sp.edit().putBoolean("init", false).apply(); //from now on it's not first start
        }

        //Based on the note_id in sharedPreferences decide which activity to start
        long id = sp.getLong(getString(R.string.NOTE_ID_KEY), -1);
        Intent intent;
        if(id < 0){
            // if no note id is present: go to toDoList
            intent = new Intent(this, ToDoList.class);
        } else {
            // go to note
            intent = new Intent(this, ToDoItem.class);
        }
        startActivity(intent);
        finish();
    }
}
