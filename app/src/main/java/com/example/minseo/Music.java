package com.example.minseo;

import java.util.Objects;

public class Music {
    private String title; // 음악 제목
    private String artist; // 아티스트 이름
    private int imageResource; // 이미지 리소스 ID
    private int audioResourceId; // 오디오 리소스 ID

    // Music 생성자
    public Music(String title, String artist, int imageResource, int audioResourceId) {
        this.title = title;
        this.artist = artist;
        this.imageResource = imageResource;
        this.audioResourceId = audioResourceId;
    }

    // 제목을 반환하는 getter 메소드
    public String getTitle() {
        return title;
    }

    // 아티스트 이름을 반환하는 getter 메소드
    public String getArtist() {
        return artist;
    }

    // 이미지 리소스를 반환하는 getter 메소드
    public int getImageResource() {
        return imageResource;
    }

    // 오디오 리소스 ID를 반환하는 getter 메소드
    public int getAudioResourceId() {
        return audioResourceId;
    }

    // 두 Music 객체를 비교하는 equals 메소드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 객체인지 확인
        if (o == null || getClass() != o.getClass()) return false; // 클래스가 다른지 확인
        Music music = (Music) o; // 다른 Music 객체로 형변환
        // 제목과 아티스트가 동일한지 확인
        return title.equals(music.title) && artist.equals(music.artist);
    }

    // hashCode 메소드 재정의
    @Override
    public int hashCode() {
        // 제목과 아티스트를 기반으로 해시 코드 생성
        return Objects.hash(title, artist);
    }
}
