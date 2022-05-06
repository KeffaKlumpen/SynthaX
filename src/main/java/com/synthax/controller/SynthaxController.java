package com.synthax.controller;

import com.synthax.model.EQFilters;
import com.synthax.model.SynthLFO;
import com.synthax.model.SynthReverb;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.Waveforms;
import com.synthax.model.sequencer.Sequencer;
import com.synthax.model.sequencer.SequencerMode;
import com.synthax.view.SynthaxView;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

import java.util.Random;

/**
 *
 * @author Joel Eriksson Sinclair
 */
public class SynthaxController {
    private SynthaxView synthaxView;

    private final Gain masterGain;
    private final Glide masterGainGlide;
    private SynthLFO synthLFO;
    private final OscillatorManager oscillatorManager;
    private final EQFilters filters;
    private final Sequencer sequencer;
    private SynthReverb reverb;

    /**
     * Setup AudioContext, OscillatorManager and create all necessary links.
     * @author Joel Eriksson Sinclair
     */
    public SynthaxController(SynthaxView synthaxView) {
        this.synthaxView = synthaxView;
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        sequencer = new Sequencer(this);

        masterGainGlide = new Glide(ac, 0.5f, 50);
        masterGain = new Gain(ac, 1, masterGainGlide);

        oscillatorManager = OscillatorManager.getInstance();
        Gain oscCombined = oscillatorManager.getFinalOutput();

        synthLFO = new SynthLFO();
        synthLFO.setInput(oscCombined);

        filters = new EQFilters();
        filters.addInput(synthLFO.getOutput());

        reverb = new SynthReverb(filters.getOutput());
        masterGain.addInput(reverb.getOutput());

        // Send to audio-device
        ac.out.addInput(masterGain);
        ac.start();
    }

    //region OscillatorManager (click to open/collapse)
    /**
     * @param oscillatorController
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorDown(OscillatorController oscillatorController) {
        oscillatorManager.moveOscillatorDown(oscillatorController);
    }

    /**
     * @param oscillatorController
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorUp(OscillatorController oscillatorController) {
        oscillatorManager.moveOscillatorUp(oscillatorController);
    }

    /**
     * @param oscillatorController
     * @author Joel Eriksson Sinclair
     */
    public void addOscillator(OscillatorController oscillatorController) {
        oscillatorManager.addOscillator(oscillatorController);
    }

    /**
     * @param oscillatorController
     * @author Joel Eriksson Sinclair
     */
    public void removeOscillator(OscillatorController oscillatorController) {
        oscillatorManager.removeOscillator(oscillatorController);
    }
    //endregion

    //region MIDI-handling (click to open/collapse)
    /**
     * Forward noteOn message
     * @param midiNote
     */
    public void noteOn(MidiNote midiNote, int velocity) {
        oscillatorManager.noteOn(midiNote, velocity);
    }

    /**
     * noteOn for sequencer
     * @param velocity
     */
    public void noteOn(MidiNote midiNote, int velocity, float detuneCent) {
        oscillatorManager.noteOn(midiNote, velocity, detuneCent);
    }
    /**
     * Forward noteOff message
     * @param midiNote
     */
    public void noteOff(MidiNote midiNote){
        oscillatorManager.noteOff(midiNote);
    }

    public void releaseAllVoices() {
        System.out.println("Synth release all voices");
        oscillatorManager.releaseAllVoices();
    }
    //endregion

    //region Filters (click to open/collapse)
    public void setHPCutoff(float cutoff) {
        filters.setHPCutoff(cutoff);
    }

    public void setHPActive(boolean newActive) {
        filters.setHPActive(newActive);
    }

    public void setEQActive(int i, boolean newVal) {
        filters.setEQActive(i, newVal);
    }

    public void setEQGain(int i, float newVal) {
        filters.setEQGain(i, newVal);
    }

    public void setEQFreq(int i, float newVal) {
        filters.setEQFrequency(i, newVal);
    }

    public void setEQRange(int i, float newVal) {
        filters.setEQRange(i, newVal);
    }

    public void setLPCutoff(float cutoff) {
        filters.setLPCutoff(cutoff);
    }

    public void setLPActive(boolean newActive) {
        filters.setLPActive(newActive);
    }
    //endregion

    //region LFO (click to open/collapse)
    public void setLFODepth(float depth) {
        synthLFO.setDepth(depth);
    }

    public void setLFORate(float rate) {
        synthLFO.setRate(rate);
    }

    public void setLFOWaveform(Waveforms waveform) {
        synthLFO.setWaveform(waveform);
    }

    public void setLFOActive(boolean newActive) {
        synthLFO.setActive(newActive);
    }
    //endregion

    //region Noise-GUI-forwarding (click to open/collapse)
    public void setNoiseGain(float gain) {
        oscillatorManager.getNoiseController().setGain(gain);
    }

    public void setNoiseActive(boolean isActive) {
        oscillatorManager.getNoiseController().setActive(isActive);
    }
    //endregion

    //region Sequencer (click to open/collapse)
    public void setStepOnOff(int i, boolean on) {
        sequencer.setOnOff(i, on);
    }

    public void setSequencerMode(SequencerMode sequencerMode) {
        sequencer.setSequencerMode(sequencerMode);
    }

    public boolean sequencerIsRunning() {
        return sequencer.isRunning();
    }

    public void sequencerOn() {
        sequencer.start();
    }

    public void sequencerOff() {
        sequencer.stop();
    }

    public void setSeqMidiNote(int i, MidiNote midiNote) {
        sequencer.setStepMidiNote(i, midiNote);
    }

    public void setSeqDetuneCent(int i, float detuneCent) {
        sequencer.setStepDetuneCent(i, detuneCent);
    }

    public void setSeqVelocity(int i, float velocity) {
        sequencer.setStepVelocity(i, velocity);
    }

    public void setSeqBPM(float rate) {
        sequencer.setBPM(rate);
    }

    public void setSeqNSteps(int nSteps) {
        sequencer.setNSteps(nSteps);
    }

    public void setSeqButtonOrange(int i) {
        synthaxView.setSeqButtonOrange(i);
    }

    public void setSeqButtonGray(int i) {
        synthaxView.setSeqButtonGray(i);
    }

    public void randomize(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int onOff = random.nextInt(2);
            synthaxView.setSequencerStepsOnOff(onOff == 0, i);
            int out = 0;
            for (int j = 0; j < 4; j++) {
                out += random.nextInt(88);
            }
            out /= 4;
            out += 21;
            synthaxView.setSequencerFreqKnobs(MidiNote.values()[out], i);
        }
    }
    //endregion

    //region Delay-GUI-forwarding (click to open/collapse)
    public void setDelayFeedback(float feedBackDuration) {
        oscillatorManager.setDelayFeedback(feedBackDuration);
    }

    public void setDelayTime(float delayTime) {
        oscillatorManager.setDelayTime(delayTime);
    }

    public void setDelayDecay(float decayValue) {
        oscillatorManager.setDelayDecay(decayValue);
    }

    public void setDelayLevel(float levelValue) {
        oscillatorManager.setDelayLevel(levelValue);
    }

    public void setDelayActive(boolean active) {
        oscillatorManager.setDelayActive(active);
    }
    //endregion

    //region reverb-GUI-forwarding (click to open/collapse)
    public void setReverbActive(boolean active) {
        reverb.setActive(active);
    }

    public void setReverbAmount(float amount) {
        reverb.setReverbAmount(amount);
    }

    public void setReverbSize(float size) {
        reverb.setReverbSize(size);
    }

    public void setReverbTone(float tone) {
        reverb.setReverbTone(tone);
    }
    // endregion

    public void setMasterGain(float gain) {
        masterGainGlide.setValue(gain);
    }
}
