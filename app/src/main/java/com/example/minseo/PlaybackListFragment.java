package com.example.minseo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * 재생 목록을 표시하는 프래그먼트 클래스.
 */
public class PlaybackListFragment extends Fragment {

    private MusicManager musicManager; // 음악 재생을 관리하는 매니저
    private RecyclerView playbackMusicRecyclerView; // 재생 목록을 표시하는 리사이클러뷰
    private PlaybackListAdapter adapter; // 리사이클러뷰 어댑터

    /**
     * 프래그먼트의 뷰를 생성하는 메서드.
     *
     * @param inflater           레이아웃 인플레이터
     * @param container          부모 컨테이너
     * @param savedInstanceState 저장된 인스턴스 상태
     * @return 생성된 뷰
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playbacklist, container, false);

        // MusicManager 인스턴스를 가져와서 재생 목록을 얻음
        musicManager = MusicManager.getInstance(getActivity());
        List<Music> playbackList = musicManager.getPlaybackList();

        // RecyclerView 설정
        playbackMusicRecyclerView = view.findViewById(R.id.playbacklists);
        playbackMusicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 어댑터 설정
        adapter = new PlaybackListAdapter(playbackList, musicManager);
        playbackMusicRecyclerView.setAdapter(adapter);

        return view;
    }
}
