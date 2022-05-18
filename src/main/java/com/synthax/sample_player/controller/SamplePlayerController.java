package com.synthax.sample_player.controller;

import com.synthax.sample_player.model.Pad;
import net.beadsproject.beads.ugens.Gain;

public class SamplePlayerController {
    
    private Gain masterGain;
    private Pad[] pads;
    private final int padCount = 9;

    public SamplePlayerController() {
        pads = new Pad[padCount];
        
        masterGain = new Gain(1, 1.0f);
        initPads();
    }

    private void initPads() {
        String sourceClap = "src/main/resources/com/synthax/samples/clap.wav";

        for (Pad p : pads) {
            p = new Pad(sourceClap);
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
