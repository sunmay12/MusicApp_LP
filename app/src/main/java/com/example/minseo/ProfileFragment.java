package com.example.minseo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

public class ProfileFragment extends Fragment {

    private ImageButton editBtn;
    private ImageView profileImage;
    private TextView nameText;
    private RecyclerView listeningMusicRecyclerView;
    private MusicManager musicManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment의 레이아웃을 inflate
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 뷰 초기화
        editBtn = view.findViewById(R.id.button_edit);
        profileImage = view.findViewById(R.id.user_profile);
        nameText = view.findViewById(R.id.user_name);
        listeningMusicRecyclerView = view.findViewById(R.id.ListeningMusic);

        // 프로필 정보 업데이트
        loadProfileInfo();

        // MusicManager 인스턴스 생성
        musicManager = MusicManager.getInstance(getActivity());

        // MusicManager에서 감상 중인 음악 리스트를 가져옴
        List<Music> listeningMusicList = musicManager.getPlaybackList();

        // RecyclerView 설정
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        listeningMusicRecyclerView.setLayoutManager(layoutManager);

        // RecyclerView에 어댑터 설정
        ListeningMusicAdapter adapter = new ListeningMusicAdapter(listeningMusicList);
        listeningMusicRecyclerView.setAdapter(adapter);

        editBtn.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.containers, new ProfileEditFragment())
                .addToBackStack(null)
                .commit());

        return view;
    }

    private void loadProfileInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", null);
        String profileImageUriString = sharedPreferences.getString("profileImageUri", null);

        if (userName != null) {
            nameText.setText(userName);
        }
        if (profileImageUriString != null) {
            Uri profileImageUri = Uri.parse(profileImageUriString);
            profileImage.setImageURI(profileImageUri);
        }
    }

    // RecyclerView의 Adapter 클래스 정의
    private class ListeningMusicAdapter extends RecyclerView.Adapter<ListeningMusicAdapter.ListeningMusicViewHolder> {
        private List<Music> listeningMusicList;

        // Adapter 생성자
        public ListeningMusicAdapter(List<Music> listeningMusicList) {
            this.listeningMusicList = listeningMusicList;
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
        }
    }
}
