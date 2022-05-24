package com.synthax.controller;

import com.synthax.model.oscillator.NoiseVoice;
import com.synthax.model.oscillator.Voice;
import com.synthax.util.HelperMath;
import net.beadsproject.beads.core.UGen;

/**
 * Forwards midi-messages to a number of noise generating voices to control when they are active.
 * @author Joel Eriksson Sinclair
 */
public class NoiseController extends VoiceController {
    private static final float MAX_GAIN = 0.3f;

    private float savedGain = MAX_GAIN / 2f;
    private boolean isActive = false;

    public NoiseController(int voiceCount) {
        super(voiceCount);

        voiceOutputGlide.setValue(0f);
    }

    @Override
    protected Voice createVoice(int i) {
        return new NoiseVoice(this, i);
    }

    public void setGain(float gain) {
        float mapped = HelperMath.map(gain, 0f, 1f, 0f, MAX_GAIN);

        if(isActive) {
            voiceOutputGlide.setValue(mapped);
        } else {
            savedGain = mapped;
        }
    }

    //todo: 2022-05-23 use passed in bool?
    public void setActive() {
        setActive(true);
    }
    @Override
    public void setActive(boolean active) {
        isActive = !isActive;

        if(isActive) {
            voiceOutputGlide.setValue(savedGain);
        } else {
            savedGain = voiceOutputGlide.getTargetValue();
            voiceOutputGlide.setValue(0f);
        }
    }

    @Override
    public UGen getOutput() {
        return voiceOutput;
    }
}
