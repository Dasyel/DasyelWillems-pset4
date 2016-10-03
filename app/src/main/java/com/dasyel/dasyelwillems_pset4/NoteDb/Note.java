package com.dasyel.dasyelwillems_pset4.NoteDb;

/*
A simple immutable container to serve as note object
 */
public class Note {
    final public long id;
    final public String title;
    final public String contents;

    public Note(long id, String title, String contents){
        this.id = id;
        this.title = title;
        this.contents = contents;
    }
}
