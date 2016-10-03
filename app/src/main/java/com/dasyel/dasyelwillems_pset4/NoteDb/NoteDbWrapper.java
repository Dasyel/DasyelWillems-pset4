package com.dasyel.dasyelwillems_pset4.NoteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.dasyel.dasyelwillems_pset4.NoteDb.NoteDbContract.NoteTable;

/*
A wrapper around the db for easy CRUD functionality from other classes
 */
public class NoteDbWrapper {
    private NoteDbHelper noteDbHelper;

    public NoteDbWrapper(Context context){
        this.noteDbHelper = new NoteDbHelper(context);
    }

    // Create an empty note and return the Note with the ID
    public Note create(){
        SQLiteDatabase db = noteDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_NAME_TITLE, "");
        values.put(NoteTable.COLUMN_NAME_CONTENTS, "");
        long newId = db.insert(NoteTable.TABLE_NAME, null, values);
        return new Note(newId, "", "");
    }

    // Get all notes from the database
    public Note[] readAll(){
        String selectQuery = "SELECT  * FROM " + NoteTable.TABLE_NAME;
        SQLiteDatabase db = noteDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<Note> noteArrayList = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                int note_id = c.getInt(c.getColumnIndexOrThrow(NoteTable._ID));
                String note_title = c.getString(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_TITLE));
                String note_contents = c.getString(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_CONTENTS));
                noteArrayList.add(new Note(note_id, note_title, note_contents));
            } while(c.moveToNext());
        }
        c.close();
        return noteArrayList.toArray(new Note[noteArrayList.size()]);
    }

    // Get a note by id
    public Note read(long id){
        SQLiteDatabase db = noteDbHelper.getReadableDatabase();

        String[] projection = {
                NoteTable._ID,
                NoteTable.COLUMN_NAME_TITLE,
                NoteTable.COLUMN_NAME_CONTENTS
        };

        String selection = NoteTable._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                NoteTable.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if(c.getCount() == 0){
            return null;
        }

        c.moveToFirst();
        long note_id = c.getLong(c.getColumnIndexOrThrow(NoteTable._ID));
        String note_title = c.getString(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_TITLE));
        String note_contents = c.getString(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_CONTENTS));
        c.close();
        return new Note(note_id, note_title, note_contents);
    }

    // Update a note
    public void update(Note note){
        SQLiteDatabase db = noteDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_NAME_TITLE, note.title);
        values.put(NoteTable.COLUMN_NAME_CONTENTS, note.contents);

        String selection = NoteTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(note.id)};
        db.update(
                NoteTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    // Delete a note
    public void delete(Note note){
        SQLiteDatabase db = noteDbHelper.getWritableDatabase();
        String selection = NoteTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(note.id)};
        db.delete(NoteTable.TABLE_NAME, selection, selectionArgs);
    }
}
