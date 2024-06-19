// PlaybackListFragment.java

package com.example.minseo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlaybackListFragment extends Fragment {

    private MusicManager musicManager;
    private RecyclerView playbackMusicRecyclerView;
    private PlaybackListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playbacklist, container, false);

        musicManager = MusicManager.getInstance(getActivity());
        List<Music> playbackList = musicManager.getPlaybackList();

        playbackMusicRecyclerView = view.findViewById(R.id.playbacklists);
        playbackMusicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new PlaybackListAdapter(playbackList, musicManager);
        playbackMusicRecyclerView.setAdapter(adapter);

        return view;
    }
}