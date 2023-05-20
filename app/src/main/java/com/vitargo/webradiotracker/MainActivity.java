package com.vitargo.webradiotracker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.vitargo.webradiotracker.databinding.ActivityMainBinding;
import com.vitargo.webradiotracker.db.SongTrackerDBHelper;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private SongTrackerDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new SongTrackerDBHelper(this);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                showToast(isInternetConnected(MainActivity.this));
                SongTrackerTask task = new SongTrackerTask(helper, MainActivity.this);
                task.execute();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 20000);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(view -> {
            FragmentManager mn = MainActivity.this.getSupportFragmentManager();
            Fragment navHostFragment = mn.getPrimaryNavigationFragment();
            FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
            Fragment fragment = fragmentManager.getPrimaryNavigationFragment();
            if (fragment instanceof SecondFragment) {
                helper.deleteAllSongs();
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_SecondFragment_update);

            } else {
                MainActivity.this.finish();
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }

    private void showToast(boolean check) {
        if (!check) {
            this.runOnUiThread(() -> Toast.makeText(MainActivity.this, "Internet Connection Lost! You can just see the history!", Toast.LENGTH_LONG).show());
        }
    }
}