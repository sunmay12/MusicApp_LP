package com.example.minseo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FindFragment extends Fragment {
    private MusicManager musicManager;
    private EditText findView;
    private ImageButton searchButton;
    private RecyclerView recyclerView;
    private MusicAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        findView = view.findViewById(R.id.findView);
        searchButton = view.findViewById(R.id.search_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        musicManager = MusicManager.getInstance(getActivity());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = findView.getText().toString();
                performSearch(searchText);
            }
        });

        performSearch("");

        return view;
    }

    private void performSearch(String query) {
        List<Music> results = musicManager.searchMusicByTitle(query);
        if (results.isEmpty()) {
            Toast.makeText(getActivity(), "음악을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new MusicAdapter(results, new MusicAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Music music) {
                    displayMusic(music);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private void displayMusic(Music music) {
        MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("imageResource", music.getImageResource());
        bundle.putString("title", music.getTitle());
        bundle.putString("artist", music.getArtist());
        bundle.putInt("audioResourceId", music.getAudioResourceId());
        musicPlayerFragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containers, musicPlayerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
