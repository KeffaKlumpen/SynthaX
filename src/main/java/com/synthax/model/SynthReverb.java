package com.synthax.model;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.Reverb;

public class SynthReverb {

    private final Reverb reverb;
    private final Glide wetGlide;
    private final Glide dryGlide;
    private final Gain output;
    private float cachedDryWetValue;
    private boolean isActive = false;

    public SynthReverb(UGen filterOutput) {

        reverb = new Reverb(AudioContext.getDefaultContext(), 1);

        wetGlide = new Glide(AudioContext.getDefaultContext(), 0.0f, 20f);
        Gain wetGain = new Gain(AudioContext.getDefaultContext(), 1, wetGlide);
        wetGain.addInput(reverb);

        dryGlide = new Glide(AudioContext.getDefaultContext(), 0.0f, 20f);
        Gain dryGain = new Gain(AudioContext.getDefaultContext(), 1, dryGlide);
        dryGain.addInput(filterOutput);

        // this controls the size of the reverb
        reverb.setSize(0.0f);
        // this controls the tone of the reverb
        reverb.setDamping(0.0f);
        reverb.addInput(filterOutput);

        output = new Gain(AudioContext.getDefaultContext(), 1, 1f);
        output.addInput(wetGain);
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

    public void setReverbDryWet(float dryWet) {
        if (isActive) {
            wetGlide.setValue(dryWet);
            dryGlide.setValue(1f - dryWet);
        } else {
            cachedDryWetValue = dryWet;
        }
    }

    public void setActive(boolean active) {
        isActive = active;

        if (active) {
            setReverbDryWet(cachedDryWetValue);
        } else {
            cachedDryWetValue = wetGlide.getCurrentValue();
        }
    }
}
