package com.example.minseo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1; // 이미지 선택 요청 코드
    private static final String SHARED_PREFS = "sharedPrefs"; // SharedPreferences 이름
    private static final String PROFILE_IMAGE = "profileImage"; // 프로필 이미지 키
    private static final String USER_NAME = "userName"; // 사용자 이름 키

    private MusicManager musicManager; // 음악 매니저 인스턴스
    private CircleImageView editProfile; // 프로필 이미지 뷰
    private EditText editName; // 사용자 이름 입력 필드
    private RecyclerView listeningMusicRecyclerView; // 감상 중인 음악 리스트를 표시하는 리사이클러뷰
    private Bitmap selectedBitmap; // 선택된 이미지 비트맵

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false); // 레이아웃을 인플레이트

        // 뷰 초기화
        editProfile = view.findViewById(R.id.edit_profile);
        editName = view.findViewById(R.id.uedit_name);
        listeningMusicRecyclerView = view.findViewById(R.id.ListeningMusic);
        ImageButton finishButton = view.findViewById(R.id.button_finish);

        // MusicManager 초기화
        musicManager = MusicManager.getInstance(getActivity());

        // 감상 중인 음악 리스트를 가져옴
        List<Music> listeningMusicList = musicManager.getPlaybackList();

        // 리사이클러뷰 설정
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2); // 2열의 그리드 레이아웃
        listeningMusicRecyclerView.setLayoutManager(layoutManager);
        ListeningMusicAdapter adapter = new ListeningMusicAdapter(listeningMusicList, music -> {
            // 음악 재생
            musicManager.setCurrentSelectedMusic(music);
            musicManager.playMusic(music.getAudioResourceId());
        });
        listeningMusicRecyclerView.setAdapter(adapter);

        // 프로필 이미지 클릭 리스너 설정
        editProfile.setOnClickListener(v -> openImageSelector());

        // 완료 버튼 클릭 리스너 설정
        finishButton.setOnClickListener(v -> {
            saveUserData(); // 사용자 데이터 저장
            // 프로필 수정 완료 후 기존 화면으로 돌아가기
            getParentFragmentManager().popBackStack();
        });

        // SharedPreferences에서 사용자 데이터 불러오기
        loadUserData();

        return view;
    }

    // EditText 클릭 이벤트 처리
    public void clearHintOnClick(View v) {
        // 클릭 시 힌트 텍스트 비우기
        editName.setHint("");
    }

    // 이미지 선택기 열기
    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // 이미지 타입 설정
        startActivityForResult(intent, PICK_IMAGE_REQUEST); // 이미지 선택기 시작
    }

    // 이미지 선택 결과 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); // 선택된 이미지 URI
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri); // 이미지 비트맵으로 변환
                editProfile.setImageBitmap(selectedBitmap); // 프로필 이미지 뷰에 설정
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 사용자 데이터 저장
    private void saveUserData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (selectedBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); // 비트맵 압축
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT); // 비트맵을 Base64로 인코딩
            editor.putString(PROFILE_IMAGE, encodedImage); // 프로필 이미지 저장
        }

        String name = editName.getText().toString();
        editor.putString(USER_NAME, name); // 사용자 이름 저장

        editor.apply(); // 변경사항 적용
    }

    // 사용자 데이터 불러오기
    private void loadUserData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String encodedImage = sharedPreferences.getString(PROFILE_IMAGE, null);
        String name = sharedPreferences.getString(USER_NAME, "user");

        if (encodedImage != null) {
            byte[] decodedByte = Base64.decode(encodedImage, Base64.DEFAULT); // Base64로 인코딩된 이미지 디코딩
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            editProfile.setImageBitmap(bitmap); // 프로필 이미지 설정
        }

        editName.setText(name); // 사용자 이름 설정
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false); // 홈 아이템 레이아웃 인플레이트
            return new ListeningMusicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListeningMusicViewHolder holder, int position) {
            Music music = listeningMusicList.get(position);
            holder.coverImageView.setImageResource(music.getImageResource()); // 커버 이미지 설정
            holder.titleTextView.setText(music.getTitle()); // 음악 제목 설정
            holder.artistTextView.setText(music.getArtist()); // 아티스트 이름 설정
            holder.bind(music, listener); // 음악 항목 바인딩
        }

        @Override
        public int getItemCount() {
            return listeningMusicList.size(); // 아이템 개수 반환
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
                itemView.setOnClickListener(v -> listener.onItemClick(music)); // 아이템 클릭 리스너 설정
            }
        }
    }

    // OnItemClickListener 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(Music music); // 아이템 클릭 시 호출되는 메소드
    }
}
