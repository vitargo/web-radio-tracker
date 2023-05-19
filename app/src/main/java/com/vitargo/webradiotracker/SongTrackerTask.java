package com.vitargo.webradiotracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import com.vitargo.webradiotracker.db.Song;
import com.vitargo.webradiotracker.db.SongContract;
import com.vitargo.webradiotracker.db.SongTrackerDBHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SongTrackerTask extends AsyncTask<URL, Void, Song> {

    private final SongTrackerDBHelper helper;

    private final MainActivity activity;

    public SongTrackerTask(SongTrackerDBHelper helper, MainActivity activity) {
        this.helper = helper;
        this.activity = activity;
    }

    @Override
    protected Song doInBackground(URL... urls) {
        Song result = new Song();
        try {
            HttpGet httppost = new HttpGet("https://webradio.io/api/radio/pi/current-song");
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                JSONObject jsono = new JSONObject(data);
                System.out.println(jsono.getString("artist") + jsono.getString("title"));
                result.setArtist(jsono.getString("artist"));
                result.setTitle(jsono.getString("title"));
                return result;
            }
        } catch (IOException | JSONException e) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Internet Connection Lost! You can just see the history!", Toast.LENGTH_LONG).show();
                }
            });
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Song song) {
        boolean result = false;
        Song lastSong = helper.getLastRecord();
        if (song != null && lastSong != null) {
            result = song.isTheSameSong(lastSong);
        }
        if (!result && song != null) {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SongContract.SongList.COLUMN_ARTIST, song.getArtist());
            values.put(SongContract.SongList.COLUMN_TITLE, song.getTitle());
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            values.put(SongContract.SongList.COLUMN_DATESTAMP, df.format(c));
            long newRowId = db.insert(SongContract.SongList.TABLE_NAME, null, values);
        }
        TextView text = activity.findViewById(R.id.textview_first);
        String textSong;
        if (song != null) {
            textSong = song.getArtist() + " \n " + song.getTitle();
            text.setText(textSong);
        }
        super.onPostExecute(song);
    }
}
