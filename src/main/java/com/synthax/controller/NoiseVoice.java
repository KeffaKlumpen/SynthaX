package com.synthax.controller;

import net.beadsproject.beads.ugens.*;

/**
 * Generates a soundwave of white noise.
 * @author Joel Eriksson Sinclair
 */
public class NoiseVoice {
    private final Noise noiseGenerator;
    private final Gain naturalGain;
    private final Envelope gainEnv;
    private final Gain normalizedGain;
    private final Glide normGainGlide;

    public NoiseVoice(){
        noiseGenerator = new Noise();

        gainEnv = new Envelope();
        naturalGain = new Gain(1, gainEnv);
        naturalGain.addInput(noiseGenerator);

        normGainGlide = new Glide(0f, 10f);
        normalizedGain = new Gain(1, normGainGlide);
        normalizedGain.addInput(naturalGain);
    }

    /**
     * Sets the voice to generate noise.
     * Volume of the sound changes over time by other parameters.
     */
    public void playNoise(float maxGain, float attackTime, float sustainGain, float decayTime){
        gainEnv.clear();
        gainEnv.addSegment(maxGain, attackTime);
        gainEnv.addSegment(sustainGain, decayTime);

        //TODO Noise delay
    }

    public void stopPlay(float releaseTime){
        gainEnv.clear();
        gainEnv.addSegment(0f, releaseTime);
    }

    public Gain getNaturalGain() {
        return naturalGain;
    }

    public Gain getNormalizedGain() {
        return normalizedGain;
    }

    public Glide getNormGainGlide() {
        return normGainGlide;
    }
}
