package com.synthax.sample_player.controller;

import com.synthax.sample_player.model.Pad;
import com.synthax.view.SamplePlayerView;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;

public class SamplePlayerController {
    
    private final Gain masterGain;
    private final Pad[] pads;
    private final int padCount = 9;
    private SamplePlayerView samplePlayerView;

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
                pads[i] = new Pad(sourceClap);
            } else if (i == 1) {
                pads[i] = new Pad(sourceHiHat);
            } else if (i == 2) {
                pads[i] = new Pad(sourceKick);
            } else if (i == 3) {
                pads[i] = new Pad(sourceSnare);
            } else {
                pads[i] = new Pad(sourceSnare);
            }
        }
        for (Pad p : pads) {
            masterGain.addInput(p.getPadGain());
        }
    }

    public void playPad(int index) {
        pads[index].playPad();
    }

    public void setMasterGain(float gain) {
        masterGain.setGain(gain);
    }
}
