package com.argentumjk.client.general;

public interface IMidiPlayer {
    void initialize();
    boolean isLooping();
    void setLooping(boolean loop);
    void play(int num);
    void pause();
    void resume();
    void stop();
    void release();
    boolean isPlaying();
    void setVolume(float volume);
    void setMuteMidi(boolean muteMidi);
}
