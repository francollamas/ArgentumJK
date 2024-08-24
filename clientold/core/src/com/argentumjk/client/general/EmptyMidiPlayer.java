package com.argentumjk.client.general;

public class EmptyMidiPlayer implements IMidiPlayer {

    @Override
    public void initialize() {

    }

    @Override
    public boolean isLooping() {
        return false;
    }

    @Override
    public void setLooping(boolean loop) {

    }

    @Override
    public void play(int num) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void release() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void setVolume(float volume) {

    }

    @Override
    public void setMuteMidi(boolean muteMidi) {

    }
}
