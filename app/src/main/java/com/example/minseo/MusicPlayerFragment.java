package com.example.minseo;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MusicPlayerFragment extends Fragment {

    private MusicManager musicManager;
    private View musicBar;
    private ImageView lpCoverImageView, lpImageView;
    private ObjectAnimator rotationAnimator, rotationAnimatorCover;
    private ImageButton playPauseButton, heartButton, listButton, previousButton, nextButton;
    private SeekBar seekBar;
    private TextView currentTimeTextView, totalTimeTextView;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicplayer, container, false);

        lpImageView = view.findViewById(R.id.lp);
        lpCoverImageView = view.findViewById(R.id.lp_cover);
        TextView titleTextView = view.findViewById(R.id.title);
        TextView singerTextView = view.findViewById(R.id.singer);
        playPauseButton = view.findViewById(R.id.button_state);
        seekBar = view.findViewById(R.id.seekBar);
        currentTimeTextView = view.findViewById(R.id.current_time);
        totalTimeTextView = view.findViewById(R.id.total_time);
        heartButton = view.findViewById(R.id.button_heart);
        listButton = view.findViewById(R.id.button_list);
        previousButton = view.findViewById(R.id.button_previous);
        nextButton = view.findViewById(R.id.button_next);

        musicManager = MusicManager.getInstance(getActivity());

        rotationAnimator = ObjectAnimator.ofFloat(lpImageView, "rotation", 0f, 360f);
        rotationAnimator.setDuration(2000);
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnimator.setInterpolator(new LinearInterpolator());

        rotationAnimatorCover = ObjectAnimator.ofFloat(lpCoverImageView, "rotation", 0f, 360f);
        rotationAnimatorCover.setDuration(2000);
        rotationAnimatorCover.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnimatorCover.setInterpolator(new LinearInterpolator());

        Bundle bundle = getArguments();
        if (bundle != null) {
            int imageResource = bundle.getInt("imageResource");
            String title = bundle.getString("title");
            String artist = bundle.getString("artist");
            int audioResourceId = bundle.getInt("audioResourceId");
            int currentPosition = bundle.getInt("currentPosition");

            lpCoverImageView.setImageResource(imageResource);
            titleTextView.setText(title);
            singerTextView.setText(artist);

            // 현재 재생 위치로 이동
            if (!musicManager.isPlaying()) {
                musicManager.playMusic(audioResourceId);
                musicManager.seekTo(currentPosition);
                musicManager.resumeMusic();
            }

            Music currentMusic = new Music(title, artist, imageResource, audioResourceId);
            musicManager.setCurrentSelectedMusic(currentMusic);
            musicManager.playMusic(audioResourceId);

            updateHeartButton();
        } else {
            Music currentMusic = musicManager.getCurrentSelectedMusic();
            if (currentMusic != null) {
                lpCoverImageView.setImageResource(currentMusic.getImageResource());
                titleTextView.setText(currentMusic.getTitle());
                singerTextView.setText(currentMusic.getArtist());
            }
            updateHeartButton();
        }

        musicBar = getActivity().findViewById(R.id.musicBar);
        if (musicBar != null) {
            musicBar.setVisibility(View.GONE);
        }

        setupPlayPauseButton();
        setupSeekBar();
        setupHeartButton();

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment playbackListFragment = new PlaybackListFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.containers, playbackListFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.playPreviousMusic();
                updateUI();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicManager.playNextMusic();
                updateUI();
            }
        });

        updateSeekBar();

        return view;
    }

    private void updateUI() {
        Music currentMusic = musicManager.getCurrentSelectedMusic();
        if (currentMusic != null) {
            lpCoverImageView.setImageResource(currentMusic.getImageResource());
            ((TextView) getView().findViewById(R.id.title)).setText(currentMusic.getTitle());
            ((TextView) getView().findViewById(R.id.singer)).setText(currentMusic.getArtist());
            playPauseButton.setImageResource(musicManager.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
        }
        updateHeartButton();
        if (musicBar != null) {
            musicBar.setVisibility(View.GONE);
        }
    }

    private void setupHeartButton() {
        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Music currentMusic = musicManager.getCurrentSelectedMusic();
                if (currentMusic != null) {
                    musicManager.toggleFavoriteMusic(currentMusic);
                    updateHeartButton();
                }
            }
        });
        updateHeartButton();
    }

    private void updateHeartButton() {
        Music currentMusic = musicManager.getCurrentSelectedMusic();
        if (currentMusic != null && musicManager.isFavoriteMusic(currentMusic)) {
            heartButton.setImageResource(R.drawable.ic_fullheart);
        } else {
            heartButton.setImageResource(R.drawable.ic_emptyheart);
        }
    }

    private void setupPlayPauseButton() {
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicManager.isPlaying()) {
                    musicManager.pauseMusic();
                    playPauseButton.setImageResource(R.drawable.ic_play);
                    rotationAnimator.pause();
                    rotationAnimatorCover.pause();
                } else {
                    musicManager.resumeMusic();
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    rotationAnimator.resume();
                    rotationAnimatorCover.resume();
                }
            }
        });
    }

    private void setupSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicManager.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateSeekBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (musicManager.isPlaying()) {
                    int currentPosition = musicManager.getCurrentPosition();
                    int duration = musicManager.getDuration();
                    seekBar.setMax(duration);
                    seekBar.setProgress(currentPosition);

                    currentTimeTextView.setText(formatTime(currentPosition));
                    totalTimeTextView.setText(formatTime(duration));
                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private String formatTime(int timeInMillis) {
        int minutes = (timeInMillis / 1000) / 60;
        int seconds = (timeInMillis / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicManager.stopMusic();
        rotationAnimator.cancel();
        rotationAnimatorCover.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rotationAnimator.cancel();
        rotationAnimatorCover.cancel();
        if (musicBar != null) {
            musicBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (musicManager.isPlaying()) {
            rotationAnimator.start();
            rotationAnimatorCover.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        rotationAnimator.pause();
        rotationAnimatorCover.pause();
    }
}
