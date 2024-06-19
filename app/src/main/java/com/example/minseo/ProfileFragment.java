package com.example.minseo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private MusicManager musicManager;
    private CircleImageView userProfile;
    private TextView userName;
    private TextView showFavorite;
    private RecyclerView listeningMusicRecyclerView;
    private EditProfileFragment editProfileFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment의 레이아웃을 inflate
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 뷰 초기화
        userProfile = view.findViewById(R.id.user_profile);
        userName = view.findViewById(R.id.user_name);
        showFavorite = view.findViewById(R.id.show_favorite);
        listeningMusicRecyclerView = view.findViewById(R.id.ListeningMusic);
        ImageButton editButton = view.findViewById(R.id.button_edit);
        editProfileFragment = new EditProfileFragment();

        // MusicManager 초기화
        musicManager = MusicManager.getInstance(getActivity());

        // 감상 중인 음악 리스트를 가져옴
        List<Music> listeningMusicList = musicManager.getPlaybackList();

        // 리사이클러뷰 설정
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        listeningMusicRecyclerView.setLayoutManager(layoutManager);
        ListeningMusicAdapter adapter = new ListeningMusicAdapter(listeningMusicList, music -> {
            // 음악 재생
            musicManager.setCurrentSelectedMusic(music);
            musicManager.playMusic(music.getAudioResourceId());
        });
        listeningMusicRecyclerView.setAdapter(adapter);

        // 수정 버튼 클릭 리스너 설정
        editButton.setOnClickListener(v -> {
            // 수정 버튼 클릭 시 EditProfileFragment로 전환
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.containers, editProfileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    // RecyclerView의 Adapter 클래스 정의
    private class ListeningMusicAdapter extends RecyclerView.Adapter<ListeningMusicAdapter.ListeningMusicViewHolder> {
        private List<Music> listeningMusicList;
        private OnItemClickListener listener;

        public ListeningMusicAdapter(List<Music> listeningMusicList, OnItemClickListener listener) {
            this.listeningMusicList = listeningMusicList;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ListeningMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
            return new ListeningMusicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListeningMusicViewHolder holder, int position) {
            Music music = listeningMusicList.get(position);
            holder.coverImageView.setImageResource(music.getImageResource());
            holder.titleTextView.setText(music.getTitle());
            holder.artistTextView.setText(music.getArtist());
            holder.bind(music, listener);
        }

        @Override
        public int getItemCount() {
            return listeningMusicList.size();
        }

        public class ListeningMusicViewHolder extends RecyclerView.ViewHolder {
            ImageView coverImageView;
            TextView titleTextView;
            TextView artistTextView;

            public ListeningMusicViewHolder(@NonNull View itemView) {
                super(itemView);
                coverImageView = itemView.findViewById(R.id.coverImageView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                artistTextView = itemView.findViewById(R.id.artistTextView);
            }

            public void bind(final Music music, final OnItemClickListener listener) {
                itemView.setOnClickListener(v -> listener.onItemClick(music));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Music music);
    }
}
