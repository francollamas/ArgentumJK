package com.argentumjk.client.implementations;

import static com.argentumjk.client.general.FileNames.getMusicDir;
import static com.argentumjk.client.general.FileNames.getSoundFontDir;

import com.argentumjk.client.general.IMidiPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

public class DesktopMidiPlayer implements IMidiPlayer {
    private Soundbank soundfont;
    private Sequence sequence;
    private Sequencer sequencer;
    private Synthesizer synthesizer;
    private Receiver receiver;
    private int currentNum;

    public DesktopMidiPlayer() {

    }

    @Override
    public void initialize() {
        try {
            soundfont = MidiSystem.getSoundbank(Gdx.files.internal(getSoundFontDir()).file());
            sequencer = MidiSystem.getSequencer();
            synthesizer = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            //Gdx.app.error("MidiPlayer", "Error opening midi device.", e);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play(int num) {
        if (currentNum == num) return;
        FileHandle file = Gdx.files.internal(getMusicDir("" + num));
        try {
            sequence = MidiSystem.getSequence(file.read());
            sequencer.open();
            synthesizer.open();
            synthesizer.loadAllInstruments(soundfont);
            receiver = synthesizer.getReceiver();
            sequencer.getTransmitter().setReceiver(receiver);
            sequencer.setSequence(sequence);
            setLooping(true);
            sequencer.start();
            currentNum = num;
        } catch (Exception e) {
            Gdx.app.error("MidiPlayer", "Error opening midi: " + num + ".", e);
        }
    }

    @Override
    public boolean isLooping() {
        if(sequencer != null){
            return sequencer.getLoopCount() != 0;
        }
        return false;
    }

    @Override
    public void setLooping(boolean loop) {
        if(sequencer != null){
            if(!loop){
                sequencer.setLoopCount(0);
                return;
            }
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        stop();
    }

    @Override
    public void stop() {
        if(sequencer != null && sequencer.isOpen()){
            sequencer.stop();
        }
    }

    @Override
    public void release() {
        if(sequencer != null){
            sequencer.close();
        }
    }

    @Override
    public boolean isPlaying() {
        return sequencer.isRunning();
    }

    @Override
    public void setVolume(float volume) {

    }

    @Override
    public void setMuteMidi(boolean muteMidi) {

    }
}
