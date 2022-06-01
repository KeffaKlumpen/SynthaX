package com.synthax.model.effects;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.Reverb;

/**
 * Class that handles the reverb effect
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 */
public class SynthaxReverb {

    private final Reverb reverb;
    private final Glide amountGlide;
    private final Gain output;
    private float cachedAmountValue;
    private boolean isActive = false;

    public SynthaxReverb(UGen filterOutput) {

        reverb = new Reverb(AudioContext.getDefaultContext(), 1);

        // this controls the amount of reverb
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

    public float getReverbSize() {
        return reverb.getSize();
    }

    public void setReverbTone(float tone) {
        reverb.setDamping(tone);
    }

    public float getReverbTone() {
        return reverb.getDamping();
    }

    public void setReverbAmount(float amount) {
        if (isActive) {
            amountGlide.setValue(amount);
        } else {
            cachedAmountValue = amount;
        }
    }

    public float getReverbAmount() {
        if (isActive) {
            return amountGlide.getCurrentValue();
        } else {
            return cachedAmountValue;
        }
    }

    public void setActive() {
        isActive = !isActive;

        if (isActive) {
            setReverbAmount(cachedAmountValue);
        } else {
            cachedAmountValue = amountGlide.getCurrentValue();
            amountGlide.setValue(0.0f);
        }
    }


    public void setPadActive(boolean isActive) {
        this.isActive = isActive;

        if (isActive) {
            setReverbAmount(cachedAmountValue);
        } else {
            cachedAmountValue = amountGlide.getCurrentValue();
            amountGlide.setValue(0.0f);
        }
    }

    public boolean getReverbIsActive() {
        return isActive;
    }
}
