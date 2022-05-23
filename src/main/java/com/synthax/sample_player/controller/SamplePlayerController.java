package com.synthax.sample_player.controller;

import com.synthax.sample_player.model.Pad;
import com.synthax.sample_player.model.SamplePlayerSequencer;
import com.synthax.view.SamplePlayerView;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;

import java.util.Arrays;
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

    public SamplePlayerController(SamplePlayerView samplePlayerView) {
        this.samplePlayerView = samplePlayerView;
        pads = new Pad[padCount];
        sequencer = new SamplePlayerSequencer(this, pads);

        masterGain = new Gain(1, 1.0f);
        initPads();
        AudioContext.getDefaultContext().out.addInput(masterGain);
    }

    private void initPads() {
        String sourceClap = "src/main/resources/com/synthax/samples/clap.wav";
        String sourceClHat = "src/main/resources/com/synthax/samples/ClHat.wav";
        String sourceKick = "src/main/resources/com/synthax/samples/kick.wav";
        String sourceSnare = "src/main/resources/com/synthax/samples/snare.wav";
        String sourceCrash = "src/main/resources/com/synthax/samples/crash.wav";
        String sourceRide = "src/main/resources/com/synthax/samples/ride.wav";
        String sourceTom1 = "src/main/resources/com/synthax/samples/tom1.wav";
        String sourceTom2 = "src/main/resources/com/synthax/samples/tom2.wav";
        String sourceOpHat = "src/main/resources/com/synthax/samples/OpHat.wav";
        for (int i = 0; i < padCount; i++) {
            if (i == 0) {
                pads[i] = new Pad(sourceKick, this, i+1);
            } else if (i == 1) {
                pads[i] = new Pad(sourceSnare, this, i+1);
            } else if (i == 2) {
                pads[i] = new Pad(sourceClap, this, i+1);
            } else if (i == 3) {
                pads[i] = new Pad(sourceClHat, this, i+1);
            } else if (i == 4) {
                pads[i] = new Pad(sourceOpHat, this, i+1);
            } else if (i == 5) {
                pads[i] = new Pad(sourceTom1, this, i+1);
            } else if (i == 6) {
                pads[i] = new Pad(sourceTom2, this, i+1);
            } else if (i == 7) {
                pads[i] = new Pad(sourceCrash, this, i+1);
            } else {
                pads[i] = new Pad(sourceRide, this, i+1);
            }
        }
        for (Pad p : pads) {
            masterGain.addInput(p.getPadOutput());
        }
        fillSourceMap(sourceClap, sourceClHat, sourceKick, sourceSnare, sourceOpHat, sourceTom1, sourceTom2, sourceCrash, sourceRide);
    }

    private void fillSourceMap(String sourceClap, String sourceClHat, String sourceKick, String sourceSnare, String sourceOpHat, String sourceTom1, String sourceTom2, String sourceCrash, String sourceRide) {
        sourceMap = new HashMap<>();
        sourceMap.put(sourceClap.substring(sourceClap.lastIndexOf("/") + 1, sourceClap.lastIndexOf(".")), sourceClap);
        sourceMap.put(sourceClHat.substring(sourceClHat.lastIndexOf("/") + 1 , sourceClHat.lastIndexOf(".")), sourceClHat);
        sourceMap.put(sourceKick.substring(sourceKick.lastIndexOf("/") + 1, sourceKick.lastIndexOf(".")), sourceKick);
        sourceMap.put(sourceSnare.substring(sourceSnare.lastIndexOf("/") + 1, sourceSnare.lastIndexOf(".")), sourceSnare);
        sourceMap.put(sourceOpHat.substring(sourceOpHat.lastIndexOf("/") + 1, sourceOpHat.lastIndexOf(".")), sourceOpHat);
        sourceMap.put(sourceTom1.substring(sourceTom1.lastIndexOf("/") + 1, sourceTom1.lastIndexOf(".")), sourceTom1);
        sourceMap.put(sourceTom2.substring(sourceTom2.lastIndexOf("/") + 1, sourceTom2.lastIndexOf(".")), sourceTom2);
        sourceMap.put(sourceCrash.substring(sourceCrash.lastIndexOf("/") + 1, sourceCrash.lastIndexOf(".")), sourceCrash);
        sourceMap.put(sourceRide.substring(sourceRide.lastIndexOf("/") + 1, sourceRide.lastIndexOf(".")), sourceRide);
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

    //region Sequencer GUI-forwarding (click to open/collapse)
    public void setSequencerStep(boolean activated, int step, int pad) {
        sequencer.setPadActive(activated, step, pad);
    }

    public void setSequencerRate(float rate) {
        sequencer.setBPM(rate);
    }

    public void startSequencer() {
        sequencer.start();
    }

    public void stopSequencer() {
        sequencer.stop();
    }

    public boolean sequencerIsRunning() {
        return sequencer.isRunning();
    }

    //endregion Sequencer GUI-forwarding

    public void setPadGain(float gain) {
        currentPad.setGain(gain);
    }

    public void setPadSample(String sampleName) {
        currentPad.setSample(sourceMap.get(sampleName));
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
