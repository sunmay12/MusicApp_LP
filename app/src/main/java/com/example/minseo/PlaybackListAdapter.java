package com.example.minseo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * RecyclerView.Adapter를 상속받아 재생 목록을 표시하는 어댑터 클래스.
 */
public class PlaybackListAdapter extends RecyclerView.Adapter<PlaybackListAdapter.ViewHolder> {

    private List<Music> playbackList; // 재생 목록을 담은 리스트
    private MusicManager musicManager; // 음악 재생을 관리하는 매니저

    /**
     * 어댑터 생성자.
     *
     * @param playbackList 재생 목록
     * @param musicManager 음악 재생을 관리하는 매니저
     */
    public PlaybackListAdapter(List<Music> playbackList, MusicManager musicManager) {
        this.playbackList = playbackList;
        this.musicManager = musicManager;
    }

    /**
     * ViewHolder 생성.
     *
     * @param parent   부모 ViewGroup
     * @param viewType 뷰 타입
     * @return 생성된 ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playbacklist_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * ViewHolder에 데이터를 바인딩.
     *
     * @param holder   ViewHolder
     * @param position 위치
     */
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

    /**
     * 아이템 개수를 반환.
     *
     * @return 재생 목록의 아이템 수
     */
    @Override
    public int getItemCount() {
        return playbackList.size();
    }

    /**
     * RecyclerView의 아이템 뷰를 저장하는 클래스.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView; // 음악 제목을 표시하는 텍스트뷰
        public TextView artistTextView; // 아티스트를 표시하는 텍스트뷰
        public ImageView albumImageView; // 앨범 이미지를 표시하는 이미지뷰

        /**
         * ViewHolder 생성자.
         *
         * @param itemView 아이템 뷰
         */
        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title);
            artistTextView = itemView.findViewById(R.id.music_artist);
            albumImageView = itemView.findViewById(R.id.album_image);
        }
    }
}
