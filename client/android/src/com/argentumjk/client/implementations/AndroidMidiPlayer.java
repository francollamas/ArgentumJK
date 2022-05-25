package com.argentumjk.client.implementations;

import static com.argentumjk.client.general.FileNames.getMusicDir;
import static com.argentumjk.client.general.FileNames.getSoundFontDir;

import android.content.Context;
import cn.sherlock.com.sun.media.sound.SF2Soundbank;
import cn.sherlock.com.sun.media.sound.SoftSynthesizer;
import com.argentumjk.client.general.IMidiPlayer;
import java.io.IOException;
import java.io.InputStream;
import jp.kshoji.javax.sound.midi.*;
import jp.kshoji.javax.sound.midi.io.StandardMidiFileReader;

public class AndroidMidiPlayer implements IMidiPlayer {

    private Context context;

    private SF2Soundbank soundFont;
    private SoftSynthesizer synth;
    private int currentNum;
    private Sequence currentSequence;
    private Sequencer sequencer;
    private Thread playNewFileThread;

    private long tickPosition;
    private boolean looping = true;
    private MetaEventListener metaEventListener;
    private boolean muteMidi;
    private float volume = 1f;
    private int lastMidiNum = 6;

    public AndroidMidiPlayer(Context context) {
        this.context = context;
        try {
            // Cargamos el SoundFont de Windows (para mantener los mismos sonidos que los originales)
            soundFont = new SF2Soundbank(context.getAssets().open(getSoundFontDir()));

            // Definimos un listener que reinicia el midi al terminar
            metaEventListener = (msg) -> {
                if (msg.getType() == 0x2F) { // End of track
                    // Restart the song
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        closeSynth();
                        tickPosition = 0;
                        resume();
                    }).start();

                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newSynth() throws Exception {
        if (synth != null) {
            synth.close();
        }

        try {
            synth = new SoftSynthesizer();
            synth.open();
            synth.loadAllInstruments(soundFont);
            int cantChannels = synth.getChannels().length;
            for (int i = 0; i < cantChannels; i++) {
                synth.getChannels()[i].programChange(i);
            }
            synth.getReceiver();
            MidiSystem.addMidiDevice(synth);
            sequencer = MidiSystem.getSequencer();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void play(int num) {
        lastMidiNum = num;

        if (muteMidi) return;
        if (num == currentNum) return;

        // Esperamos que se termine de cargar el archivo anterior
        while (playNewFileThread != null && playNewFileThread.isAlive()) {
        }

        playNewFileThread = new Thread(() -> {
            try {
                if (synth != null) {
                    closeSynth();
                }

                newSynth();
                InputStream is = context.getAssets().open(getMusicDir(num));
                StandardMidiFileReader fileReader = new StandardMidiFileReader();
                currentSequence = fileReader.getSequence(is);
                is.close();
                sequencer.open();
                sequencer.setSequence(currentSequence);
                sequencer.start();
                setLoop();
                currentNum = num;
                if (volume < 1)
                    new Thread(() -> {
                        long timer = System.currentTimeMillis();
                        while (System.currentTimeMillis() - timer < 10000) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            setVolume(volume);
                        }
                    }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        playNewFileThread.setDaemon(true);
        playNewFileThread.start();
    }

    private void setLoop() {
        if(sequencer != null){
            if (looping) {
                sequencer.addMetaEventListener(metaEventListener);
            }
            else {
                sequencer.removeMetaEventListener(metaEventListener);
            }
        }
    }

    @Override
    public void initialize() {

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
        looping = loop;
        if (sequencer != null && sequencer.isOpen()) {
            setLoop();
        }
    }

    @Override
    public void resume() {
        if (muteMidi) return;
        // Lo hacemos en otro hilo para que sea mas rapida la apertura de la app.
        new Thread(() -> {
            try {
                boolean mustPlay = currentSequence != null;

                try {
                    newSynth();
                    sequencer.open();
                    sequencer.setSequence(currentSequence);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mustPlay) {
                    sequencer.start();
                    setLoop();
                    sequencer.setTickPosition(tickPosition);
                    if (volume < 1)
                        new Thread(() -> {
                            long timer = System.currentTimeMillis();
                            while (System.currentTimeMillis() - timer < 10000) {
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                setVolume(volume);
                            }
                        }).start();
                }
            }
            catch(Exception e) {
                // TODO: cacheamos posible error en Android
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Para completamente la reproduccion
     */
    private void closeSynth() {
        if(sequencer != null) {
            if (sequencer.isOpen()) {
                sequencer.stop();
                sequencer.close();
                sequencer = null;
            }
            if (synth != null) {
                if (synth.isOpen()) {
                    synth.close();
                }
                MidiSystem.removeMidiDevice(synth);
                synth = null;
            }
        }
    }

    @Override
    public void pause() {
        if (sequencer != null) {
            tickPosition = sequencer.getTickPosition();
        }
        closeSynth();
    }

    @Override
    public void stop() {
        currentSequence = null;
        closeSynth();
        currentNum = -1;
    }

    @Override
    public void release() {
        currentSequence = null;
        closeSynth();
        if (playNewFileThread != null) {
            playNewFileThread.interrupt();
        }
    }

    @Override
    public boolean isPlaying() {
        if (sequencer != null && sequencer.isRunning()) return true;
        return false;
    }

    @Override
    public void setVolume(float volume) {
        this.volume = volume;
        if (synth != null) {
            for (MidiChannel channel : synth.getChannels()) {
                channel.controlChange(7, (int) (volume * 127));
            }
        }
    }

    @Override
    public void setMuteMidi(boolean muteMidi) {
        this.muteMidi = muteMidi;

        // Paramos la musica actual...
        if (muteMidi) {
            stop();
        }
        else {
            play(lastMidiNum);
        }
    }
}