package com.vitargo.webradiotracker.db;

import android.provider.BaseColumns;

public class SongContract {
    private SongContract() {}

    public static class SongList implements BaseColumns {
        public static final String TABLE_NAME = "songlist";
        public static final String COLUMN_ARTIST = "artist";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATESTAMP = "date";
    }
}
