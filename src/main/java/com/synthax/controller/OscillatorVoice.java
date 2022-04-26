/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.controller;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

public class OscillatorVoice {
    private final WavePlayer wavePlayer;
    private final Gain output;
    private final Envelope gainEnv;

    public OscillatorVoice(Buffer waveBuffer){
        wavePlayer = new WavePlayer(0f, waveBuffer);
        gainEnv = new Envelope();
        output = new Gain(1, gainEnv);
        output.addInput(wavePlayer);
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

    public Gain getOutput() {
        return output;
    }

    public WavePlayer getWavePlayer() {
        return wavePlayer;
    }

    public void setWavePlayerBuffer(Buffer buffer){
        wavePlayer.setBuffer(buffer);
    }

}
