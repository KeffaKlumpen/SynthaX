package com.synthax.model.oscillator;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;

/**
 * The combined volume of multiple voices can cause distortion
 * This UGen normalizes each voices gain dependent on how many voices are active.
 * @author Joel Eriksson Sinclair
 */
public class VoiceNormalizer extends UGen {
    private Gain[] inGains;
    private Glide[] outGains;

    private int inouts;

    public VoiceNormalizer(int inouts) {
        this(getDefaultContext(), inouts);
    }

    public VoiceNormalizer(AudioContext context, int voices) {
        super(context, voices, voices);
        this.inouts = voices;
        inGains = new Gain[voices];
        outGains = new Glide[voices];
    }

    /**
     * Set the un-normalized gain for the voice. Must be set for each voice.
     */
    public void setInGain(Gain inGain, int index) {
        inGains[index] = inGain;
    }

    /**
     * Set the to-be-normalized gain for the voice. Must be set for each inout.
     */
    public void setOutGain(Glide outGain, int index) {
        outGains[index] = outGain;
    }

    /**
     * Calculates the total gain being played, and then normalizes each individual gain based on the total.
     */
    @Override
    public void calculateBuffer() {
        float totalGain = 0f;
        float[] currGains = new float[ins];
        if(inGains != null) {
            for (int i = 0; i < inouts; i++) {
                if(inGains[i] != null) {
                    currGains[i] = inGains[i].getGain();
                    totalGain += inGains[i].getGain();
                }
            }
        }
        if(outGains != null) {
            if (totalGain != 0) {
                for (int i = 0; i < inouts; i++) {
                    if(outGains[i] != null) {
                        outGains[i].setValue(currGains[i] / totalGain);
                    }
                }
            }
        }
    }
}
