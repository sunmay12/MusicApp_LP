package com.example.minseo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

    // SharedPreferences 키 상수 정의
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String PROFILE_IMAGE = "profileImage";
    private static final String USER_NAME = "userName";

    // UI 요소 및 데이터 관리 객체 선언
    private MusicManager musicManager;
    private CircleImageView userProfile;
    private TextView userName;
    private ImageButton editButton;
    private RecyclerView listeningMusicRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_profile 레이아웃을 인플레이트하여 뷰 생성
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 뷰 요소 초기화
        userProfile = view.findViewById(R.id.user_profile);
        userName = view.findViewById(R.id.user_name);
        listeningMusicRecyclerView = view.findViewById(R.id.ListeningMusic);
        editButton = view.findViewById(R.id.button_edit);

        // MusicManager 초기화
        musicManager = MusicManager.getInstance(getActivity());

        // 현재 재생 중인 음악 리스트 가져오기
        List<Music> listeningMusicList = musicManager.getPlaybackList();

        // RecyclerView 설정
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        listeningMusicRecyclerView.setLayoutManager(layoutManager);
        ListeningMusicAdapter adapter = new ListeningMusicAdapter(listeningMusicList, music -> {
            // 음악 클릭 시 음악 재생
            musicManager.setCurrentSelectedMusic(music);
            musicManager.playMusic(music.getAudioResourceId());
        });
        listeningMusicRecyclerView.setAdapter(adapter);

        // SharedPreferences에서 사용자 데이터 불러오기
        loadUserData();

        // 수정 버튼 클릭 리스너 설정
        editButton.setOnClickListener(v -> {
            // EditProfileFragment로 전환
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.containers, new EditProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    // SharedPreferences에서 사용자 데이터를 불러와서 UI에 설정하는 메서드
    private void loadUserData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String encodedImage = sharedPreferences.getString(PROFILE_IMAGE, null);
        String name = sharedPreferences.getString(USER_NAME, "user-12345");

        // 프로필 이미지가 있는 경우 디코딩하여 설정
        if (encodedImage != null) {
            byte[] decodedByte = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            userProfile.setImageBitmap(bitmap);
        }

        // 사용자 이름 설정
        userName.setText(name);
    }

    // RecyclerView의 Adapter 클래스 정의
    private class ListeningMusicAdapter extends RecyclerView.Adapter<ListeningMusicAdapter.ListeningMusicViewHolder> {
        private List<Music> listeningMusicList;
        private OnItemClickListener listener;

        // 어댑터 생성자
        public ListeningMusicAdapter(List<Music> listeningMusicList, OnItemClickListener listener) {
            this.listeningMusicList = listeningMusicList;
            this.listener = listener;
        }

        // ViewHolder를 생성하여 반환하는 메서드
        @NonNull
        @Override
        public ListeningMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
            return new ListeningMusicViewHolder(view);
        }

        // ViewHolder에 데이터를 바인딩하는 메서드
        @Override
        public void onBindViewHolder(@NonNull ListeningMusicViewHolder holder, int position) {
            Music music = listeningMusicList.get(position);
            holder.coverImageView.setImageResource(music.getImageResource());
            holder.titleTextView.setText(music.getTitle());
            holder.artistTextView.setText(music.getArtist());
            holder.bind(music, listener);
        }

        // 아이템 개수 반환 메서드
        @Override
        public int getItemCount() {
            return listeningMusicList.size();
        }

        // ViewHolder 클래스 정의
        public class ListeningMusicViewHolder extends RecyclerView.ViewHolder {
            ImageView coverImageView;
            TextView titleTextView;
            TextView artistTextView;

            // ViewHolder 생성자
            public ListeningMusicViewHolder(@NonNull View itemView) {
                super(itemView);
                coverImageView = itemView.findViewById(R.id.coverImageView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                artistTextView = itemView.findViewById(R.id.artistTextView);
            }

            // 뷰에 클릭 리스너 바인딩 메서드
            public void bind(final Music music, final OnItemClickListener listener) {
                itemView.setOnClickListener(v -> listener.onItemClick(music));
            }
        }
    }

    // 아이템 클릭 리스너 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(Music music);
    }
}
