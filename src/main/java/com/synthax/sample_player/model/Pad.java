package com.synthax.sample_player.model;

import com.synthax.model.SynthaxReverb;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

import java.io.File;
import java.io.IOException;

public class Pad {

    private SamplePlayer samplePlayer;
    private Gain padGain;
    private SynthaxReverb reverb;
    private String fileName;

    public Pad(String path) {
        setFileName(path);
        padGain = new Gain(1, 0.0f);
        initPad(path);
        reverb = new SynthaxReverb(padGain);
    }

    private void initPad(String path) {
        Sample sample;
        try {
            sample = new Sample(path);
        } catch (IOException | OperationUnsupportedException | FileFormatException e) {
            throw new RuntimeException(e);
        }
        samplePlayer = new SamplePlayer(sample);
        samplePlayer.setKillOnEnd(false);
        padGain.addInput(samplePlayer);
    }

    public void playPad() {
        padGain.setGain(1.0f);
        samplePlayer.setToLoopStart();
        samplePlayer.start();
    }

    public void setSample(String path) {
        Sample sample;
        try {
            sample = new Sample(path);
        } catch (IOException | OperationUnsupportedException | FileFormatException e) {
            throw new RuntimeException(e);
        }
        samplePlayer.setSample(sample);
        setFileName(path);
    }

    public void setFileName(String path) {
        File file = new File(path);
        fileName = file.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public void setGain(float gain) {
        padGain.setGain(gain);
    }

    public Gain getPadGain() {
        return reverb.getOutput();
    }
}


