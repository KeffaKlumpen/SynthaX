package com.synthax.controller;

import com.synthax.model.Delay;
import com.synthax.model.enums.MidiNote;
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
    private final OscillatorController controller;
    private final int voiceIndex;

    private final WavePlayer wavePlayer;
    private final Gain naturalGain;
    private final Envelope gainEnv;
    private final Glide normGainGlide;

    private final OscillatorLFO oscillatorLFO;
    private final Delay delay;

    private MidiNote currentNote;

    public OscillatorVoice(Buffer waveBuffer, OscillatorController controller, int voiceIndex){
        this.controller = controller;
        this.voiceIndex = voiceIndex;

        oscillatorLFO = new OscillatorLFO();
        wavePlayer = new WavePlayer(oscillatorLFO.getFrequencyModulation(), waveBuffer);

        gainEnv = new Envelope();
        naturalGain = new Gain(1, gainEnv);
        naturalGain.addInput(wavePlayer);

        normGainGlide = new Glide(0f, 10f);
        Gain normalizedGain = new Gain(1, normGainGlide);
        normalizedGain.addInput(naturalGain);

        delay = new Delay(normalizedGain);
    }

    /**
     * Sets the voice to generate sound of the given note. Amplitude modifed over time by the other parameters.
     * The frequency is altered by the LFO
     * Amplitude is altered by the ADSR-envelope
     * The echo is altered by the Delay-envelope
     */
    public void noteOn(MidiNote note, float detunedFrequency, float maxGain, float attackTime, float sustainGain, float decayTime) {
        currentNote = note;

        oscillatorLFO.setPlayedFrequency(detunedFrequency);

        gainEnv.clear();
        gainEnv.addSegment(maxGain, attackTime);
        gainEnv.addSegment(sustainGain, decayTime);

        delay.getEnvelope().clear();
        delay.getEnvelope().addSegment(1f, 10f);
        delay.getEnvelope().addSegment(1f, delay.getFeedbackDuration());
        delay.getEnvelope().addSegment(0f, 10f);

        if(sustainGain <= 0.01f) {
            float maxPlayTime = attackTime + decayTime;
            controller.notifyAvailableVoice(voiceIndex, maxPlayTime);
        }
    }

    public void noteOff(MidiNote note, float releaseTime) {
        // IF midiNote = oldMidiNote..
        if(note == currentNote) {
            gainEnv.clear();
            gainEnv.addSegment(0f, releaseTime);

            controller.notifyAvailableVoice(voiceIndex, releaseTime);
        }
    }

    public void stopPlay(){
        gainEnv.clear();
    }

    public void bypass(boolean onOff){
        wavePlayer.pause(!onOff);
    }

    public Gain getNaturalGain() {
        return naturalGain;
    }

    public Glide getNormGainGlide() {
        return normGainGlide;
    }

    public OscillatorLFO getOscillatorLFO() {
        return oscillatorLFO;
    }

    public Delay getDelay() {
        return delay;
    }

    public void setWavePlayerBuffer(Buffer buffer){
        wavePlayer.setBuffer(buffer);
    }

    public void updateDetuneValue(float detuneCent) {
        float detunedFrequency = applyDetune(currentNote.getFrequency(), detuneCent);
        oscillatorLFO.setPlayedFrequency(detunedFrequency);
    }

    private float applyDetune(float frequency, float detuneCent) {
        return (float)(frequency * (Math.pow(2, (detuneCent/1200))));
    }
}
