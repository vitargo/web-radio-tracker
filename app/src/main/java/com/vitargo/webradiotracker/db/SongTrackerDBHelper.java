package com.vitargo.webradiotracker.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SongTrackerDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SongTracker.db";

    private static final String SQL_CREATE_SONGLIST =
            "CREATE TABLE " + SongContract.SongList.TABLE_NAME + " (" +
                    SongContract.SongList._ID + " INTEGER PRIMARY KEY," +
                    SongContract.SongList.COLUMN_ARTIST + " TEXT," +
                    SongContract.SongList.COLUMN_TITLE + " INTEGER," +
                    SongContract.SongList.COLUMN_DATESTAMP + " TEXT)";

    private static final String SQL_DELETE_SONGLIST =
            "DROP TABLE IF EXISTS " + SongContract.SongList.TABLE_NAME;

    public SongTrackerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SONGLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_SONGLIST);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<Song> getAllSongs() {
        List<Song> songList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + SongContract.SongList.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Song winner = new Song();
                winner.setId(Integer.parseInt(cursor.getString(0)));
                winner.setArtist(cursor.getString(1));
                winner.setTitle(cursor.getString(2));
                winner.setDate(cursor.getString(3));
                songList.add(winner);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    public void deleteAllSongs() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SongContract.SongList.TABLE_NAME);
    }

    public Song getLastRecord(){
        String selectQuery = "SELECT * FROM " + SongContract.SongList.TABLE_NAME + " ORDER BY " + SongContract.SongList._ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Song song = null;
        if (cursor.moveToFirst()) {
            song = new Song();
            song.setId(Integer.parseInt(cursor.getString(0)));
            song.setArtist(cursor.getString(1));
            song.setTitle(cursor.getString(2));
            song.setDate(cursor.getString(3));
        }
        return song;
    }
}