package com.synthax.controller;

import com.synthax.model.SynthaxDelay;
import com.synthax.model.oscillator.OscillatorLFO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Class that handles the necessary components to generate a sound wave
 * @author Joel Eriksson Sinclair
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 */
public class OscillatorVoice {
    private final WavePlayer wavePlayer;
    private final Gain naturalGain;
    private final Envelope gainEnv;
    private final Glide normalizedGainGlide;

    private final OscillatorLFO oscillatorLFO;
    private final SynthaxDelay delay;

    private float realFrequency;

    public OscillatorVoice(Buffer waveBuffer) {
        oscillatorLFO = new OscillatorLFO();
        wavePlayer = new WavePlayer(oscillatorLFO.getFrequencyModulation(), waveBuffer);

        gainEnv = new Envelope();
        naturalGain = new Gain(1, gainEnv);
        naturalGain.addInput(wavePlayer);

        normalizedGainGlide = new Glide(0f, 10f);
        Gain normalizedGain = new Gain(1, normalizedGainGlide);
        normalizedGain.addInput(naturalGain);

        delay = new SynthaxDelay(normalizedGain);
    }

    /**
     * Sets the voice to generate sound of the given frequency.
     * The frequency is altered by the LFO
     * Amplitude is altered by the ADSR-envelope
     * The echo is altered by the Delay-envelope
     */
    public void playFreq(float freq, float maxGain, float attackTime, float sustainGain, float decayTime, float realFrequency) {
        this.realFrequency = realFrequency;
        oscillatorLFO.setPlayedFrequency(freq);
        gainEnv.clear();
        gainEnv.addSegment(maxGain, attackTime);
        gainEnv.addSegment(sustainGain, decayTime);

        delay.getEnvelope().clear();
        delay.getEnvelope().addSegment(1f, 10f);
        delay.getEnvelope().addSegment(1f, delay.getFeedbackDuration());
        delay.getEnvelope().addSegment(0f, 10f);
    }

    public void stopPlay(float releaseTime) {
        gainEnv.clear();
        gainEnv.addSegment(0f, releaseTime);
    }

    public void bypass(boolean onOff) {
        wavePlayer.pause(!onOff);
    }

    public void updateDetuneValue(float detuneCent) {
        float freq = applyDetune(realFrequency, detuneCent);
        oscillatorLFO.setPlayedFrequency(freq);
    }

    //region Getters&Setters (click to open/collapse)
    public Gain getNaturalGain() {
        return naturalGain;
    }

    public Glide getNormalizedGainGlide() {
        return normalizedGainGlide;
    }

    public OscillatorLFO getOscillatorLFO() {
        return oscillatorLFO;
    }

    public SynthaxDelay getDelay() {
        return delay;
    }

    public void setWavePlayerBuffer(Buffer buffer){
        wavePlayer.setBuffer(buffer);
    }
    //endregion Getters&Setters

    private float applyDetune(float frequency, float detuneCent) {
        return (float)(frequency * (Math.pow(2, (detuneCent/1200))));
    }
}
