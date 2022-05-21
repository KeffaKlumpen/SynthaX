package com.synthax.sample_player.controller;

import com.synthax.sample_player.model.Pad;
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

    public SamplePlayerController(SamplePlayerView samplePlayerView) {
        this.samplePlayerView = samplePlayerView;
        pads = new Pad[padCount];
        
        masterGain = new Gain(1, 1.0f);
        initPads();
        AudioContext.getDefaultContext().out.addInput(masterGain);
    }

    private void initPads() {
        String sourceClap = "src/main/resources/com/synthax/samples/clap.wav";
        String sourceHiHat = "src/main/resources/com/synthax/samples/hihat.wav";
        String sourceKick = "src/main/resources/com/synthax/samples/kick.wav";
        String sourceSnare = "src/main/resources/com/synthax/samples/snare.wav";
        for (int i = 0; i < padCount; i++) {
            if (i == 0) {
                pads[i] = new Pad(sourceClap, this, i+1);
            } else if (i == 1) {
                pads[i] = new Pad(sourceHiHat, this, i+1);
            } else if (i == 2) {
                pads[i] = new Pad(sourceKick, this, i+1);
            } else if (i == 3) {
                pads[i] = new Pad(sourceSnare, this, i+1);
            } else {
                pads[i] = new Pad(sourceSnare, this, i+1);
            }
        }
        for (Pad p : pads) {
            masterGain.addInput(p.getPadOutput());
        }
        fillSourceMap(sourceClap, sourceHiHat, sourceKick, sourceSnare);
    }

    private void fillSourceMap(String sourceClap, String sourceHiHat, String sourceKick, String sourceSnare) {
        sourceMap = new HashMap<>();
        sourceMap.put(sourceClap.substring(sourceClap.lastIndexOf("/") + 1, sourceClap.lastIndexOf(".")), sourceClap);
        sourceMap.put(sourceHiHat.substring(sourceHiHat.lastIndexOf("/") + 1 , sourceHiHat.lastIndexOf(".")), sourceHiHat);
        sourceMap.put(sourceKick.substring(sourceKick.lastIndexOf("/") + 1, sourceKick.lastIndexOf(".")), sourceKick);
        sourceMap.put(sourceSnare.substring(sourceSnare.lastIndexOf("/") + 1, sourceSnare.lastIndexOf(".")), sourceSnare);
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

    public void bypassPadReverb() {
        currentPad.bypassReverb();
    }

    public void setAllGainValues() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (Pad p : pads) {
            p.setGain(0.5f);
        }
        setCurrentPad(0);
    }
}
