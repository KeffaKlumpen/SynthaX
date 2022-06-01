package com.synthax.sample_player.model;

import com.synthax.model.effects.SynthaxReverb;
import com.synthax.sample_player.controller.SamplePlayerController;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

import java.io.IOException;

/**
 * Class containing data for a single Pad in the Sample Player
 * @author Teodor Wegestål
 * @author Viktor Lenberg
 */
public class Pad {

    private SamplePlayer samplePlayer;
    private Gain padGain;
    private SynthaxReverb reverb;
    private String fileName;
    private SamplePlayerController samplePlayerController;
    private final int padIndex;

    public Pad(String path, SamplePlayerController samplePlayerController, int padIndex) {
        this.padIndex = padIndex;
        this.samplePlayerController = samplePlayerController;
        setFileName(subString(path));
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
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        samplePlayerController.setSequencerLabel(this.fileName, padIndex);
    }

    private String subString(String fileName) {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 2, fileName.lastIndexOf("."));
        } else {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 2, fileName.lastIndexOf("."));
        }

        return fileName;
    }

    public void setGain(float gain) {
        padGain.setGain(gain);
    }

    public Gain getPadOutput() {
        return reverb.getOutput();
    }

    public float getGain() {
        return padGain.getGain();
    }

    public String getSampleName() {
        return fileName;
    }

    public void setReverbSize(float size) {
        reverb.setReverbSize(size);
    }

    public float getReverbSize() {
        return reverb.getReverbSize();
    }

    public void setReverbTone(float tone) {
        reverb.setReverbTone(tone);
    }

    public float getReverbTone() {
        return reverb.getReverbTone();
    }

    public void setReverbAmount(float amount) {
        reverb.setReverbAmount(amount);
    }

    public float getReverbAmount() {
        return reverb.getReverbAmount();
    }

    public void bypassReverb(boolean isActive) {
        reverb.setPadActive(isActive);
    }

    public boolean getReverbActive() {
        return reverb.getReverbIsActive();
    }
}


