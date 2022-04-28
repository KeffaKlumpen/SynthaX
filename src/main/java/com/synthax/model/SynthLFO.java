package com.synthax.model;

import com.synthax.model.enums.Waveforms;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.WavePlayer;

public class SynthLFO {
    private WavePlayer lfo;
    private Waveforms waveform;

    private Function frequencyModulator;
    private float rate = 20f;
    private float depth = 0f;
    private float playedFrequency = 440f;

    public SynthLFO() {
        lfo = new WavePlayer(AudioContext.getDefaultContext(), rate, Buffer.SINE);

        frequencyModulator = new Function(lfo) {
            @Override
            public float calculate() {
                return (x[0] * depth) + playedFrequency;
            }
        };
    }

    public WavePlayer getLfo() {
        return lfo;
    }

    public void setLfo(WavePlayer lfo) {
        this.lfo = lfo;
    }

    public Waveforms getWaveform() {
        return waveform;
    }

    public void setWaveform(Waveforms waveform) {
        this.waveform = waveform;
    }

    public Function getFrequencyModulator() {
        return frequencyModulator;
    }

    public void setFrequencyModulator(Function frequencyModulator) {
        this.frequencyModulator = frequencyModulator;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public float getPlayedFrequency() {
        return playedFrequency;
    }

    public void setPlayedFrequency(float playedFrequency) {
        this.playedFrequency = playedFrequency;
    }
}
