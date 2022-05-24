package com.synthax.sample_player.controller;

import com.synthax.sample_player.model.Pad;
import com.synthax.sample_player.model.SamplePlayerSequencer;
import com.synthax.view.SamplePlayerView;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;

import java.io.File;
import java.util.HashMap;

/**
 * Controller class for the Sample Player
 * @author Teodor Wegestål
 * @author Viktor Lenberg
 */
public class SamplePlayerController {
    
    private final Gain masterGain;
    private final Pad[] pads;
    private final int padCount = 9;
    private Pad currentPad;
    private SamplePlayerView samplePlayerView;
    private HashMap<String, String> sourceMap;
    private SamplePlayerSequencer sequencer;

    private final File fileRoot = new File("src/main/resources/com/synthax/samples");

    public SamplePlayerController(SamplePlayerView samplePlayerView) {
        this.samplePlayerView = samplePlayerView;
        pads = new Pad[padCount];
        sequencer = new SamplePlayerSequencer(this, pads);

        masterGain = new Gain(1, 1.0f);
        initPads();
        AudioContext.getDefaultContext().out.addInput(masterGain);
    }

    private void initPads() {
        String[] sources = getSourceFiles();
        fillSourceMap(sources);

        for (int i = 0; i < padCount; i++) {
            pads[i] = new Pad(sources[i], this, i+1);
        }

        for (Pad p : pads) {
            masterGain.addInput(p.getPadOutput());
        }
    }

    private void fillSourceMap(String[] sources) {
        String os = System.getProperty("os.name");

        sourceMap = new HashMap<>();
        for (String s : sources) {
            String key = s.substring(s.lastIndexOf("/") + 2, s.lastIndexOf("."));
            if (os.contains("Windows")) {
                key = s.substring(s.lastIndexOf("\\") + 2, s.lastIndexOf("."));
            }
            sourceMap.put(key, s);
        }
    }

    public void playPad(int index) {
        pads[index].playPad();
    }

    public void setMasterGain(float gain) {
        masterGain.setGain(gain);
    }

    public void setSequencerLabel(String fileName, int padIndex) {
        samplePlayerView.setSequencerLabel(fileName, padIndex);
    }

    public void setCurrentPad(int index) {
        currentPad = pads[index];
        samplePlayerView.setValuesInPadView(
                currentPad.getSampleName(),
                currentPad.getGain(),
                currentPad.getReverbAmount(),
                currentPad.getReverbSize(),
                currentPad.getReverbTone(),
                currentPad.getReverbActive());
    }

    private String[] getSourceFiles() {
        File[] files = fileRoot.listFiles();
        assert files != null;
        String[] sourceFiles = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            sourceFiles[i] = files[i].getPath();
        }
        return sourceFiles;
    }

    //region Sequencer GUI-forwarding (click to open/collapse)
    public void setSequencerStep(boolean activated, int step, int pad) {
        sequencer.setPadActive(activated, step, pad);
    }

    public void setSequencerRate(float rate) {
        sequencer.setBPM(rate);
    }

    public void setStepIndicatorOrange(int index) {
        samplePlayerView.setStepIndicatorOrange(index);
    }

    public void setStepIndicatorGray(int index) {
        samplePlayerView.setStepIndicatorGray(index);
    }

    public void startSequencer() {
        sequencer.start();
    }

    public void stopSequencer() {
        sequencer.stop();
    }

    public void clearSequencer() {
        sequencer.clearSequencer();
    }

    public boolean getSequencerIsRunning() {
        return sequencer.isRunning();
    }

    //endregion Sequencer GUI-forwarding

    public void setPadGain(float gain) {
        currentPad.setGain(gain);
    }

    public void setPadSample(String sampleName) {
        currentPad.setSample(sourceMap.get(sampleName));
        currentPad.setFileName(sampleName);
    }

    public void setPadReverbSize(float size) {
        currentPad.setReverbSize(size);
    }

    public void setPadReverbTone(float tone) {
        currentPad.setReverbTone(tone);
    }

    public void setPadReverbAmount(float amount) {
        currentPad.setReverbAmount(amount);
    }

    public void bypassPadReverb(boolean isActive) {
        currentPad.bypassReverb(isActive);
    }

    public void setAllGainValues() {
        for (Pad p : pads) {
            p.setGain(0.5f);
        }
        setCurrentPad(0);
    }
}
