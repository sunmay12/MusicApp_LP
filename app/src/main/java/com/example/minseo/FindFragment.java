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
    private MusicManager musicManager; // 음악 매니저 인스턴스
    private EditText findView; // 검색어 입력 필드
    private ImageButton searchButton; // 검색 버튼
    private RecyclerView recyclerView; // 검색 결과를 표시할 리사이클러뷰
    private MusicAdapter adapter; // 리사이클러뷰 어댑터

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false); // 레이아웃 인플레이트

        // 뷰 초기화
        findView = view.findViewById(R.id.findView);
        searchButton = view.findViewById(R.id.search_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // 리사이클러뷰 레이아웃 매니저 설정

        musicManager = MusicManager.getInstance(getActivity()); // MusicManager 인스턴스 가져오기

        // 검색 버튼 클릭 리스너 설정
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = findView.getText().toString(); // 검색어 가져오기
                performSearch(searchText); // 검색 수행
            }
        });

        performSearch(""); // 초기 검색 수행 (모든 음악 표시)

        return view;
    }

    // 검색 수행 메소드
    private void performSearch(String query) {
        List<Music> results = musicManager.searchMusicByTitle(query); // 검색어로 음악 검색
        if (results.isEmpty()) {
            Toast.makeText(getActivity(), "음악을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show(); // 결과가 없을 경우 토스트 메시지 표시
        } else {
            adapter = new MusicAdapter(results, new MusicAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Music music) {
                    displayMusic(music); // 음악 항목 클릭 시 음악 재생 화면으로 이동
                }
            });
            recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 설정
        }
    }

    // 음악 재생 화면으로 이동하는 메소드
    private void displayMusic(Music music) {
        MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment(); // 음악 재생 프래그먼트 생성

        Bundle bundle = new Bundle();
        bundle.putInt("imageResource", music.getImageResource()); // 음악 이미지 리소스 전달
        bundle.putString("title", music.getTitle()); // 음악 제목 전달
        bundle.putString("artist", music.getArtist()); // 아티스트 이름 전달
        bundle.putInt("audioResourceId", music.getAudioResourceId()); // 오디오 리소스 ID 전달
        musicPlayerFragment.setArguments(bundle); // 번들을 프래그먼트에 설정

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containers, musicPlayerFragment); // 프래그먼트 교체
        transaction.addToBackStack(null); // 백스택에 추가
        transaction.commit(); // 트랜잭션 커밋
    }
}
