package com.example.minseo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    HomeFragment myFragment;
    FindFragment findFragment;
    MusicPlayerFragment musicPlayerFragment;
    Button title;
    ImageButton state, next;
    ImageView cover;
    MusicManager musicManager;
    LinearLayout musicBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findFragment = new FindFragment();
        myFragment = new HomeFragment();
        musicPlayerFragment = new MusicPlayerFragment();
        title = findViewById(R.id.button_title);
        state = findViewById(R.id.button_state);
        next = findViewById(R.id.button_next);
        cover = findViewById(R.id.image_cover);
        musicBar = findViewById(R.id.musicBar);

        musicManager = MusicManager.getInstance(this);

        // 처음에 HomeFragment를 표시하도록 설정
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containers, myFragment)
                    .commit();
        }

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationView);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, myFragment).commit();
                    return true;
                } else if (itemId == R.id.find) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, findFragment).commit();
                    return true;
                }
                return false;
            }
        });

        // musicBar의 title과 cover를 눌렀을 때 MusicPlayerFragment로 전환
        View.OnClickListener musicBarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Music currentMusic = musicManager.getCurrentSelectedMusic();
                if (currentMusic != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("imageResource", currentMusic.getImageResource());
                    bundle.putString("title", currentMusic.getTitle());
                    bundle.putString("artist", currentMusic.getArtist());
                    bundle.putInt("audioResourceId", currentMusic.getAudioResourceId());
                    musicPlayerFragment.setArguments(bundle);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.containers, musicPlayerFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        };

        title.setOnClickListener(musicBarClickListener);
        cover.setOnClickListener(musicBarClickListener);

        // 음악 재생/멈춤 버튼 설정
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicManager.isPlaying()) {
                    musicManager.pauseMusic();
                    state.setImageResource(R.drawable.ic_play);
                } else {
                    musicManager.resumeMusic();
                    state.setImageResource(R.drawable.ic_pause);
                }
            }
        });

        // 다음 음악 버튼 설정
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.playNextMusic();
            }
        });

        // 음악 정보를 업데이트하기 위해 MusicManager의 리스너 설정
        musicManager.setOnMusicStateChangedListener(new MusicManager.OnMusicStateChangedListener() {
            @Override
            public void onMusicStarted(Music music) {
                // 음악이 시작될 때 현재 음악의 제목과 앨범 커버를 설정
                if (music != null) {
                    title.setText(music.getTitle());
                    cover.setImageResource(music.getImageResource());
                    state.setImageResource(R.drawable.ic_pause);
                    musicBar.setVisibility(View.VISIBLE); // 음악이 시작되면 musicBar를 보이도록 설정
                }
            }

            @Override
            public void onMusicStopped() {
                // 음악이 중지될 때 title 버튼과 cover 이미지를 초기화
                title.setText("");
                cover.setImageResource(0); // 빈 이미지 리소스로 초기화
                state.setImageResource(R.drawable.ic_play);
                musicBar.setVisibility(View.GONE); // 음악이 중지되면 musicBar를 숨기도록 설정
            }
        });
    }
}
