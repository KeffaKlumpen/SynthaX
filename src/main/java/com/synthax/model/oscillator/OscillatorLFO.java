package com.synthax.model.oscillator;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Mult;
import net.beadsproject.beads.ugens.WavePlayer;

public class OscillatorLFO {
    private WavePlayer lfo;
    private float depth = 0f;
    private float playedFrequency = 0f;
    private Function frequencyModulation;

    public OscillatorLFO() {
        lfo = new WavePlayer(AudioContext.getDefaultContext(), 20f, Buffer.SINE);

        frequencyModulation = new Function(lfo) {
            @Override
            public float calculate() {
                return (x[0] * depth) + playedFrequency;
            }
        };
    }

    public Function getFrequencyModulation() {
        return frequencyModulation;
    }

    public void setPlayedFrequency(float playedFrequency) {
        this.playedFrequency = playedFrequency;
    }

    public void setRate(float rate) {
        rate = convertRate(rate);
        System.out.println(rate);
        lfo.setFrequency(rate);
    }

    private float convertRate(float rate) {
        return (float) (rate * 39.9 + 0.1);
    }

    public void setDepth(float depth) {
        this.depth = depth * 100;
    }
}
