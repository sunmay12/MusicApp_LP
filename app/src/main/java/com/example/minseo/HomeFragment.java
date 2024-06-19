package com.example.minseo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFragment extends Fragment {

    private ProfileFragment profileFragment;
    private MusicManager musicManager;
    private ImageView profileImage;
    private TextView profileName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment의 레이아웃을 inflate
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        profileFragment = new ProfileFragment();
        profileImage = view.findViewById(R.id.profile);
        profileName = view.findViewById(R.id.userName);
        musicManager = MusicManager.getInstance(getActivity());

        // 프로필 정보 업데이트
        loadProfileInfo();

        // MusicManager에서 즐겨찾기한 음악 리스트를 가져옴
        List<Music> favoriteMusicList = musicManager.getFavoriteMusicList();

        // RecyclerView 설정
        RecyclerView recyclerView = view.findViewById(R.id.favoriteMusicRecyclerView);
        // GridLayoutManager로 변경, spanCount를 2로 설정
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // RecyclerView에 어댑터 설정
        FavoriteMusicAdapter adapter = new FavoriteMusicAdapter(favoriteMusicList, music -> {
            // 음악 재생
            musicManager.setCurrentSelectedMusic(music);
            musicManager.playMusic(music.getAudioResourceId());
        });
        recyclerView.setAdapter(adapter);

        // Profile 이미지 뷰 설정
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // profile_fragment로 전환
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.containers, profileFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    private void loadProfileInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", null);
        String profileImageUriString = sharedPreferences.getString("profileImageUri", null);

        if (userName != null) {
            profileName.setText(userName);
        }
        if (profileImageUriString != null) {
            Uri profileImageUri = Uri.parse(profileImageUriString);
            profileImage.setImageURI(profileImageUri);
        }
    }

    // RecyclerView의 Adapter 클래스 정의
    private class FavoriteMusicAdapter extends RecyclerView.Adapter<FavoriteMusicAdapter.FavoriteMusicViewHolder> {
        private List<Music> favoriteMusicList;
        private OnItemClickListener listener;

        // Adapter 생성자
        public FavoriteMusicAdapter(List<Music> favoriteMusicList, OnItemClickListener listener) {
            this.favoriteMusicList = favoriteMusicList;
            this.listener = listener;
        }

        @NonNull
        @Override
        public FavoriteMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
            return new FavoriteMusicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavoriteMusicViewHolder holder, int position) {
            Music music = favoriteMusicList.get(position);
            holder.coverImageView.setImageResource(music.getImageResource());
            holder.titleTextView.setText(music.getTitle());
            holder.artistTextView.setText(music.getArtist());
            holder.bind(music, listener);
        }

        @Override
        public int getItemCount() {
            return favoriteMusicList.size();
        }

        // ViewHolder 클래스 정의
        public class FavoriteMusicViewHolder extends RecyclerView.ViewHolder {
            ImageView coverImageView;
            TextView titleTextView;
            TextView artistTextView;

            public FavoriteMusicViewHolder(@NonNull View itemView) {
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

    // OnItemClickListener 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(Music music);
    }
}
