package com.dasyel.dasyelwillems_pset4.NoteDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.dasyel.dasyelwillems_pset4.NoteDb.NoteDbContract.*;

/*
A custom SQLiteOpenHelper
 */
class NoteDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Notes.db";

    NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_NOTE_TABLE =
                "CREATE TABLE " + NoteTable.TABLE_NAME + " (" +
                        NoteTable._ID + " INTEGER PRIMARY KEY," +
                        NoteTable.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        NoteTable.COLUMN_NAME_CONTENTS + TEXT_TYPE + " )";
        db.execSQL(SQL_CREATE_NOTE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_NOTE_TABLE =
                "DROP TABLE IF EXISTS " + NoteTable.TABLE_NAME;
        db.execSQL(SQL_DELETE_NOTE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
