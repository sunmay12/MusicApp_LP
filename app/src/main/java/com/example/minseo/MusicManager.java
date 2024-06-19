package com.example.minseo;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class MusicManager {
    private static MusicManager instance;
    private List<Music> allMusics = new ArrayList<>(); // 모든 음악을 저장할 리스트
    private Context context;
    private int currentIndex = -1;
    private MediaPlayer mediaPlayer;
    private Music currentSelectedMusic; // 현재 선택된 노래를 저장할 변수
    private List<Music> playbackList = new ArrayList<>(); // 재생 목록

    private OnMusicStateChangedListener musicStateChangedListener; // 음악 상태 변화 리스너

    private List<Music> favoriteMusics = new ArrayList<>(); // 보관함에 있는 음악을 저장할 리스트

    // MusicManager 생성자 (private으로 설정하여 외부에서 직접 인스턴스를 생성하지 못하게 함)
    private MusicManager(Context context) {
        this.context = context;
        initializeMusics(); // 음악 데이터를 초기화
    }

    // 싱글톤 패턴을 이용하여 MusicManager 인스턴스를 반환하는 메서드
    public static MusicManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicManager(context.getApplicationContext());
        }
        return instance;
    }

    // 음악 데이터를 초기화하는 메서드
    private void initializeMusics() {
        // 음악 데이터 추가
        addMusic(new Music("그녀가 웃잖아...", "김형중", R.drawable.lp_khj_2, R.raw.ms_khj_shessmile));
        addMusic(new Music("좋은 사람", "Toy", R.drawable.lp_toy_fermata, R.raw.ms_toy_goodperson));
        addMusic(new Music("피아니시모", "Toy", R.drawable.lp_toy_decapo, R.raw.ms_toy_pianisimo));
        addMusic(new Music("오렌지 마말레이드", "자우림", R.drawable.lp_jaurim_wonderland, R.raw.ms_jaurim_orange));
        addMusic(new Music("미안해 널 좋아해", "자우림", R.drawable.lp_jaurim_apa, R.raw.ms_jaurim_sorryiloveyou));
        addMusic(new Music("애인발견!!", "자우림", R.drawable.lp_jaurim_perpleheart, R.raw.ms_jaurim_findlover));
        addMusic(new Music("사랑하기 때문에", "유재하", R.drawable.lp_yjh_becauseilove, R.raw.ms_yjh_becauseilove));
        addMusic(new Music("지난 날", "유재하", R.drawable.lp_yjh_becauseilove, R.raw.ms_yjh_lastday));
        addMusic(new Music("내 마음에 비친 내 모습", "유재하", R.drawable.lp_yjh_becauseilove, R.raw.ms_yjh_meinmymind));
        addMusic(new Music("소녀", "이문세", R.drawable.lp_lms_idontknowyet, R.raw.ms_lms_agirl));
        addMusic(new Music("사랑이 지나가면", "이문세", R.drawable.lp_lms_4, R.raw.ms_lms_theloveispassed));
        addMusic(new Music("옛사랑", "이문세", R.drawable.lp_lms_7, R.raw.ms_lms_pastlove));
        addMusic(new Music("가로수 그늘 아래 서면", "이문세", R.drawable.lp_lms_5, R.raw.ms_lms_underthetree));
        addMusic(new Music("조조할인", "이문세", R.drawable.lp_lms_hwa, R.raw.ms_lms_earlymorning));
        addMusic(new Music("S.A.D", "The Volunteers", R.drawable.lp_thevolun_thevolun, R.raw.ms_thevolun_sad));
        addMusic(new Music("Summer", "The Volunteers", R.drawable.lp_thevolun_thevolun, R.raw.ms_thevolun_summer));
        addMusic(new Music("New Plant", "The Volunteers", R.drawable.lp_thevolun_newplant, R.raw.ms_thevolun_newplant));
        addMusic(new Music("지켜줄게", "백예린", R.drawable.lp_yr_ourlove, R.raw.ms_yr_seeyouagain));
        addMusic(new Music("그건 아마 우리의 잘못은 아닐 거야", "백예린", R.drawable.lp_yr_ourlove, R.raw.ms_yr_notourfault));
        addMusic(new Music("Bye Bye My Blue", "백예린", R.drawable.lp_yr_byebye, R.raw.ms_yr_byebye));
        addMusic(new Music("popo", "백예린", R.drawable.lp_yr_everyletter, R.raw.ms_yr_popo));
        addMusic(new Music("Antifreeze", "백예린", R.drawable.lp_yr_gift, R.raw.ms_yr_antifreeze));
        addMusic(new Music("숙녀에게", "변진섭", R.drawable.lp_bjs_again, R.raw.ms_bjs_toaladay));
        addMusic(new Music("네게 줄 수 있는 건 오직 사랑뿐", "변진섭", R.drawable.lp_bjs_alone, R.raw.ms_bjs_onlythingtogiveyou));
        addMusic(new Music("Oh, My Love", "S.E.S", R.drawable.lp_ses_1, R.raw.ms_ses_ohmylove));
        addMusic(new Music("애인찾기", "S.E.S", R.drawable.lp_ses_2, R.raw.ms_ses_findlover));
        addMusic(new Music("Sweet Dream", "장나라", R.drawable.lp_nara_sweetdream, R.raw.ms_nara_sweetdream));
        addMusic(new Music("고백하기 좋은 날", "장나라", R.drawable.lp_nara_loveschool, R.raw.ms_nara_agoodday));
        addMusic(new Music("연애", "김현철", R.drawable.lp_khc_best, R.raw.ms_khc_dating));
        addMusic(new Music("동네", "김현철", R.drawable.lp_khc_1, R.raw.ms_khc_village));
        addMusic(new Music("안녕", "박혜경", R.drawable.lp_phk_saraphim, R.raw.ms_phk_hi));
        addMusic(new Music("고백", "박혜경", R.drawable.lp_phk_01, R.raw.ms_phk_confession));
        addMusic(new Music("민들레 (full ver.)", "우효", R.drawable.lp_oohyo_dandelion, R.raw.ms_oohyo_dandelion));
        addMusic(new Music("청춘 (DAY)", "우효", R.drawable.lp_oohyo_youth, R.raw.ms_oohyo_youth));
    }

    // 음악 데이터를 리스트에 추가하는 메서드
    private void addMusic(Music music) {
        allMusics.add(music);
    }

    // 음악을 재생하는 메서드
    public void playMusic(int musicResource) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, musicResource);
        mediaPlayer.start();

        // 현재 선택된 음악을 재생 목록에 추가
        if (currentSelectedMusic != null) {
            addToPlaybackList(currentSelectedMusic);
        }

        // 음악 재생 상태 변경 리스너 호출
        if (musicStateChangedListener != null) {
            musicStateChangedListener.onMusicStarted(currentSelectedMusic);
        }
    }

    // 음악 재생을 멈추는 메서드
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;

            // 음악 중지 상태 변경 리스너 호출
            if (musicStateChangedListener != null) {
                musicStateChangedListener.onMusicStopped();
            }
        }
    }

    public void playPreviousMusic() {
        if (playbackList != null && !playbackList.isEmpty()) {
            currentIndex = (currentIndex - 1 + playbackList.size()) % playbackList.size();
            Music previousMusic = playbackList.get(currentIndex);
            setCurrentSelectedMusic(previousMusic);
            playMusic(previousMusic.getAudioResourceId());
        }
    }

    public void playNextMusic() {
        if (playbackList != null && !playbackList.isEmpty()) {
            currentIndex = (currentIndex + 1) % playbackList.size();
            Music nextMusic = playbackList.get(currentIndex);
            setCurrentSelectedMusic(nextMusic);
            playMusic(nextMusic.getAudioResourceId());
        }
    }

    // 음악 상태 변경 리스너 설정 메서드
    public void setOnMusicStateChangedListener(OnMusicStateChangedListener listener) {
        this.musicStateChangedListener = listener;
    }

    // 인터페이스 정의
    public interface OnMusicStateChangedListener {
        void onMusicStarted(Music music); // 음악이 시작될 때 호출
        void onMusicStopped(); // 음악이 멈출 때 호출
    }

    // 현재 재생 중인 음악의 전체 길이를 반환하는 메서드
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    // 현재 재생 위치를 반환하는 메서드
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    // 음악 재생 위치를 변경하는 메서드
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    // 즐겨찾기 음악 목록을 반환하는 메서드
    public List<Music> getFavoriteMusicList() {
        return new ArrayList<>(favoriteMusics);
    }

    // 즐겨찾기 음악을 추가하는 메서드
    public void addFavoriteMusic(Music music) {
        if (!favoriteMusics.contains(music)) {
            favoriteMusics.add(music);
        }
    }

    // 즐겨찾기 음악을 제거하는 메서드
    public void removeFavoriteMusic(Music music) {
        favoriteMusics.remove(music);
    }

    // 특정 음악이 즐겨찾기에 있는지 확인하는 메서드
    public boolean isFavoriteMusic(Music music) {
        return favoriteMusics.contains(music);
    }

    // 즐겨찾기 음악을 토글하는 메서드 (있으면 제거, 없으면 추가)
    public void toggleFavoriteMusic(Music music) {
        if (isFavoriteMusic(music)) {
            removeFavoriteMusic(music);
        } else {
            addFavoriteMusic(music);
        }
    }

    // 현재 선택된 음악을 반환하는 메서드
    public Music getCurrentSelectedMusic() {
        return currentSelectedMusic;
    }

    // 현재 선택된 음악을 설정하는 메서드
    public void setCurrentSelectedMusic(Music music) {
        currentSelectedMusic = music;
        currentIndex = playbackList.indexOf(music);
    }

    // 재생 목록에 음악을 추가하는 메서드
    public void addToPlaybackList(Music music) {
        if(!playbackList.contains(music)) {
            playbackList.add(music);
        }
    }

    // 재생 목록을 반환하는 메서드
    public List<Music> getPlaybackList() {
        return playbackList;
    }

    // 음악이 재생 중인지 확인하는 메서드
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    // 음악을 일시 정지하는 메서드
    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    // 음악 다시 재생하는 메서드
    public void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // 음악 제목으로 음악을 검색하는 메서드
    public List<Music> searchMusicByTitle(String query) {
        List<Music> searchResults = new ArrayList<>();
        for (Music music : allMusics) {
            if (music.getTitle().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(music);
            }
        }
        return searchResults;
    }
}
