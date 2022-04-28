/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.controller;

import com.synthax.model.oscillator.OscillatorLFO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Generates a soundwave.
 * @author Joel Eriksson Sinclair
 */
public class OscillatorVoice {
    private final WavePlayer wavePlayer;
    private final Gain naturalGain;
    private final Envelope gainEnv;
    private final Gain normalizedGain;
    private final Glide normGainGlide;
    private OscillatorLFO oscillatorLFO;

    public OscillatorVoice(Buffer waveBuffer){
        oscillatorLFO = new OscillatorLFO();
        wavePlayer = new WavePlayer(oscillatorLFO.getFrequencyModulation(), waveBuffer);
        //wavePlayer = new WavePlayer(0f, waveBuffer);

        gainEnv = new Envelope();
        naturalGain = new Gain(1, gainEnv);
        naturalGain.addInput(wavePlayer);

        normGainGlide = new Glide(0f, 10f);
        normalizedGain = new Gain(1, normGainGlide);
        normalizedGain.addInput(naturalGain);
    }

    /**
     * Sets the voice to generate sound of the given frequency.
     * Volume of the sound changes over time by other parameters.
     * @param freq
     * @param maxGain
     * @param attackTime
     * @param sustainGain
     * @param decayTime
     */
    public void playFreq(float freq, float maxGain, float attackTime, float sustainGain, float decayTime){
        //wavePlayer.setFrequency(freq);
        //oscillatorLFO.setPlayedFrequency(freq);
        wavePlayer.setFrequency(freq);
        gainEnv.clear();
        gainEnv.addSegment(maxGain, attackTime);
        gainEnv.addSegment(sustainGain, decayTime);
    }

    /**
     * Notifies the voice to stop playing over a specified time.
     * @param releaseTime
     */
    public void stopPlay(float releaseTime){
        gainEnv.clear();
        gainEnv.addSegment(0f, releaseTime);
    }

    /**
     * Toggle wether the voice generates sound.
     * @param onOff
     */
    public void bypass(boolean onOff){
        wavePlayer.pause(!onOff);
    }

    /**
     * Return the gain before normalization.
     * @return
     */
    public Gain getNaturalGain() {
        return naturalGain;
    }

    /**
     * Return the gain to be normalized.
     * @return
     */
    public Gain getNormalizedGain() {
        return normalizedGain;
    }

    /**
     * Return the glide object to control the normalized gain.
     * @return
     */
    public Glide getNormGainGlide() {
        return normGainGlide;
    }

    public WavePlayer getWavePlayer() {
        return wavePlayer;
    }

    public OscillatorLFO getOscillatorLFO() {
        return oscillatorLFO;
    }

    /**
     * Set the shape of the sound to be played.
     * @param buffer
     */
    public void setWavePlayerBuffer(Buffer buffer){
        wavePlayer.setBuffer(buffer);
    }

}
