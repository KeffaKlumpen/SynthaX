package com.synthax.model.oscillator;

import com.synthax.controller.OscillatorController;
import com.synthax.controller.SynthaxController;
import com.synthax.model.effects.SynthaxDelay;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.OctaveOperands;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
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
        delay.getEnvelope().addSegment(1f, SynthaxController.MINIMUM_GLIDE_DURATION);
        delay.getEnvelope().addSegment(1f, delay.getFeedbackDuration());
        delay.getEnvelope().addSegment(0f, SynthaxController.MINIMUM_GLIDE_DURATION);
    }

    public void noteOn(MidiNote note, float detunedFrequency, float maxGain, float attackTime, float sustainGain, float decayTime, float seqDetune) {
        super.noteOn(note, detunedFrequency, maxGain, attackTime, sustainGain, decayTime);

        // apply more detuning, nice sequencer stuff
        oscillatorLFO.setPlayedFrequency(detunedFrequency);

        delay.getEnvelope().clear();
        delay.getEnvelope().addSegment(1f, SynthaxController.MINIMUM_GLIDE_DURATION);
        delay.getEnvelope().addSegment(1f, delay.getFeedbackDuration());
        delay.getEnvelope().addSegment(0f, SynthaxController.MINIMUM_GLIDE_DURATION);
    }

    @Override
    public UGen getOutput() {
        return delay.getOutput();
    }

    public void bypass(boolean onOff) {
        wavePlayer.pause(!onOff);
    }

    public void updateDetuneValue(float detuneCent, OctaveOperands octaveOperand) {
        float octaveOffsetFrequency = applyOctaveOffset(currentNote.getFrequency(), octaveOperand);
        float detunedFrequency = applyDetune(octaveOffsetFrequency, detuneCent);
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

    /**
     *If we know the note a and the number n of cents in the interval from a to b
     * then b may be calculated by this formula
     */
    private float applyDetune(float frequency, float detuneCent) {
        return (float)(frequency * (Math.pow(2, (detuneCent/1200))));
    }

    private float applyOctaveOffset(float frequency, OctaveOperands octaveOperand) {
        return frequency * octaveOperand.getValue();
    }
}
