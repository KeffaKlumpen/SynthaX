package com.synthax.model;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.Reverb;

public class SynthReverb {

    private final Reverb reverb;
    private final Glide amountGlide;
    private final Gain output;
    private float cachedAmountValue;
    private boolean isActive = false;

    public SynthReverb(UGen filterOutput) {

        reverb = new Reverb(AudioContext.getDefaultContext(), 1);

        amountGlide = new Glide(AudioContext.getDefaultContext(), 0.0f, 20f);
        Gain amountGain = new Gain(AudioContext.getDefaultContext(), 1, amountGlide);
        amountGain.addInput(reverb);

        Gain dryGain = new Gain(AudioContext.getDefaultContext(), 1, 1.0f);
        dryGain.addInput(filterOutput);

        // this controls the size of the reverb
        reverb.setSize(0.0f);
        // this controls the tone of the reverb
        reverb.setDamping(0.0f);
        reverb.addInput(filterOutput);

        output = new Gain(AudioContext.getDefaultContext(), 1, 1f);
        output.addInput(amountGain);
        output.addInput(dryGain);
    }

    public Gain getOutput() {
        return output;
    }

    public void setReverbSize(float size) {
        reverb.setSize(size);
    }

    public void setReverbTone(float tone) {
        reverb.setDamping(tone);
    }

    public void setReverbAmount(float amount) {
        if (isActive) {
            amountGlide.setValue(amount);
        } else {
            cachedAmountValue = amount;
        }
    }

    public void setActive(boolean active) {
        isActive = active;

        if (active) {
            setReverbAmount(cachedAmountValue);
        } else {
            cachedAmountValue = amountGlide.getCurrentValue();
            amountGlide.setValue(0.0f);
        }
    }
}