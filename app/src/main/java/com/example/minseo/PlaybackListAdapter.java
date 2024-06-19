// PlaybackListAdapter.java

package com.example.minseo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlaybackListAdapter extends RecyclerView.Adapter<PlaybackListAdapter.ViewHolder> {

    private List<Music> playbackList;
    private MusicManager musicManager;

    public PlaybackListAdapter(List<Music> playbackList, MusicManager musicManager) {
        this.playbackList = playbackList;
        this.musicManager = musicManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playbacklist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Music music = playbackList.get(position);
        holder.titleTextView.setText(music.getTitle());
        holder.artistTextView.setText(music.getArtist());
        holder.albumImageView.setImageResource(music.getImageResource());

        holder.itemView.setOnClickListener(v -> {
            musicManager.setCurrentSelectedMusic(music);
            musicManager.playMusic(music.getAudioResourceId());
        });
    }

    @Override
    public int getItemCount() {
        return playbackList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView artistTextView;
        public ImageView albumImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title);
            artistTextView = itemView.findViewById(R.id.music_artist);
            albumImageView = itemView.findViewById(R.id.album_image);
        }
    }
}