package com.vitargo.webradiotracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.vitargo.webradiotracker.databinding.FragmentSecondBinding;
import com.vitargo.webradiotracker.db.Song;
import com.vitargo.webradiotracker.db.SongTrackerDBHelper;

import java.util.List;

public class SecondFragment extends Fragment {

    private SongTrackerDBHelper helper;

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        helper = new SongTrackerDBHelper(this.getContext());
        TableLayout table = view.findViewById(R.id.song_list);
        updateTable(table);
        super.onViewCreated(view, savedInstanceState);
        binding.buttonSecond.setOnClickListener(view1 -> NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void updateTable(TableLayout table){
        List<Song> songs = helper.getAllSongs();
        for (Song song : songs){
            TableRow row = new TableRow(table.getContext());
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowParams);
            TextView name = new TextView(table.getContext());
            name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            name.setText(song.getArtist());
            row.addView(name);
            TextView score = new TextView(table.getContext());
            score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            score.setText(song.getTitle());
            row.addView(score);
            TextView date = new TextView(table.getContext());
            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            date.setText(song.getDate());
            row.addView(date);
            table.addView(row);
        }
    }
}