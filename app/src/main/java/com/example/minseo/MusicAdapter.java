package com.example.minseo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<Music> musicList; // 음악 리스트 데이터
    private OnItemClickListener listener; // 아이템 클릭 리스너

    // 아이템 클릭 리스너 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(Music music);
    }

    // MusicAdapter 생성자
    public MusicAdapter(List<Music> musicList, OnItemClickListener listener) {
        this.musicList = musicList; // 음악 리스트 초기화
        this.listener = listener; // 리스너 초기화
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ViewHolder 생성: find_item 레이아웃 인플레이트
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_item, parent, false);
        return new MusicViewHolder(view); // ViewHolder 반환
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        // ViewHolder와 데이터를 바인드
        Music music = musicList.get(position); // 현재 위치의 음악 데이터 가져오기
        holder.bind(music, listener); // ViewHolder에 데이터 바인딩
    }

    @Override
    public int getItemCount() {
        return musicList.size(); // 아이템 개수 반환
    }

    // ViewHolder 클래스 정의
    static class MusicViewHolder extends RecyclerView.ViewHolder {

        ImageView albumCover; // 앨범 커버 이미지뷰
        TextView title; // 음악 제목 텍스트뷰
        TextView artist; // 아티스트 이름 텍스트뷰

        // ViewHolder 생성자
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰 초기화
            albumCover = itemView.findViewById(R.id.album_cover);
            title = itemView.findViewById(R.id.music_title);
            artist = itemView.findViewById(R.id.music_artist);
        }

        // 데이터와 뷰를 바인딩
        public void bind(Music music, OnItemClickListener listener) {
            // 음악 데이터 설정
            albumCover.setImageResource(music.getImageResource());
            title.setText(music.getTitle());
            artist.setText(music.getArtist());
            // 아이템 클릭 리스너 설정
            itemView.setOnClickListener(v -> listener.onItemClick(music));
        }
    }
}
