/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.controller;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

public class OscillatorVoice {
    private final WavePlayer wavePlayer;
    private final Gain naturalGain;
    private final Envelope gainEnv;
    private final Gain normalizedGain;
    private final Glide normGainGlide;

    public OscillatorVoice(Buffer waveBuffer){
        wavePlayer = new WavePlayer(0f, waveBuffer);

        gainEnv = new Envelope();
        naturalGain = new Gain(1, gainEnv);
        naturalGain.addInput(wavePlayer);

        normGainGlide = new Glide(0f, 10f);
        normalizedGain = new Gain(1, normGainGlide);
        normalizedGain.addInput(naturalGain);
    }

    public void playFreq(float freq, float maxGain, float attackTime, float sustainGain, float decayTime){
        wavePlayer.setFrequency(freq);
        gainEnv.clear();
        gainEnv.addSegment(maxGain, attackTime);
        gainEnv.addSegment(sustainGain, decayTime);
    }

    public void stopPlay(float releaseTime){
        gainEnv.clear();
        gainEnv.addSegment(0f, releaseTime);
    }

    public void bypass(boolean onOff){
        wavePlayer.pause(!onOff);
    }

    public Gain getNaturalGain() {
        return naturalGain;
    }

    public Gain getNormalizedGain() {
        return normalizedGain;
    }
    public void setNormalizedGain(float gain) {
        normGainGlide.setValue(gain);
    }

    public WavePlayer getWavePlayer() {
        return wavePlayer;
    }

    public void setWavePlayerBuffer(Buffer buffer){
        wavePlayer.setBuffer(buffer);
    }

}
