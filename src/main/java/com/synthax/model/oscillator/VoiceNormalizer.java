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

    public VoiceNormalizer(AudioContext context, int inouts) {
        super(context, inouts, inouts);
        this.inouts = inouts;
        inGains = new Gain[inouts];
        outGains = new Glide[inouts];
    }

    public void setInGain(Gain inGain, int index) {
        inGains[index] = inGain;
    }

    public void setOutGain(Glide outGain, int index) {
        outGains[index] = outGain;
    }

    @Override
    public void calculateBuffer() {
        float totalGain = 0f;
        float[] currGains = new float[ins];
        for (int i = 0; i < inouts; i++) {
            currGains[i] = inGains[i].getGain();
            totalGain += inGains[i].getGain();
        }
        if (totalGain != 0) {
            for (int i = 0; i < inouts; i++) {
                outGains[i].setValue(currGains[i] / totalGain);
            }
        }
    }
}
