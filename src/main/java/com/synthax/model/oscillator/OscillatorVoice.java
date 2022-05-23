package com.synthax.model.oscillator;

import com.synthax.controller.OscillatorController;
import com.synthax.model.SynthaxDelay;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.oscillator.OscillatorLFO;
import net.beadsproject.beads.core.UGen;
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
public class OscillatorVoice extends Voice {
    private final WavePlayer wavePlayer;

    private final OscillatorLFO oscillatorLFO;
    private final SynthaxDelay delay;

    public OscillatorVoice(Buffer waveBuffer, OscillatorController controller, int voiceIndex) {
        super(controller, voiceIndex);

        oscillatorLFO = new OscillatorLFO();
        wavePlayer = new WavePlayer(oscillatorLFO.getFrequencyModulation(), waveBuffer);

        naturalGain.addInput(wavePlayer);

        delay = new SynthaxDelay(normalizedGain);
    }

    /**
     * Sets the voice to generate sound of the given note. Amplitude modifed over time by the other parameters.
     * The frequency is altered by the LFO
     * Amplitude is altered by the ADSR-envelope
     * The echo is altered by the Delay-envelope
     */
    @Override
    public void noteOn(MidiNote note, float detunedFrequency, float maxGain, float attackTime, float sustainGain, float decayTime) {
        super.noteOn(note, detunedFrequency, maxGain, attackTime, sustainGain, decayTime);

        oscillatorLFO.setPlayedFrequency(detunedFrequency);

        delay.getEnvelope().clear();
        delay.getEnvelope().addSegment(1f, 10f);
        delay.getEnvelope().addSegment(1f, delay.getFeedbackDuration());
        delay.getEnvelope().addSegment(0f, 10f);
    }

    // SEQUENCER STUPID SHIT
    public void noteOn(MidiNote note, float detunedFrequency, float maxGain, float attackTime, float sustainGain, float decayTime, float seqDetune) {
        super.noteOn(note, detunedFrequency, maxGain, attackTime, sustainGain, decayTime);

        // apply more detuning, nice sequencer stuff
        oscillatorLFO.setPlayedFrequency(detunedFrequency);

        delay.getEnvelope().clear();
        delay.getEnvelope().addSegment(1f, 10f);
        delay.getEnvelope().addSegment(1f, delay.getFeedbackDuration());
        delay.getEnvelope().addSegment(0f, 10f);
    }

    @Override
    public UGen getOutput() {
        return delay.getOutput();
    }

    public void bypass(boolean onOff) {
        wavePlayer.pause(!onOff);
    }

    public void updateDetuneValue(float detuneCent) {
        float detunedFrequency = applyDetune(currentNote.getFrequency(), detuneCent);
        oscillatorLFO.setPlayedFrequency(detunedFrequency);
    }

    //region Getters&Setters (click to open/collapse)
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
