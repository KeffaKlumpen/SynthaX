package com.synthax.controller;

import com.synthax.model.EQFilters;
import com.synthax.model.SynthLFO;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.sequencer.Sequencer;
import com.synthax.model.enums.SequencerMode;
import com.synthax.view.SynthaxView;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

import java.util.Random;

public class SynthaxController {
    private SynthaxView synthaxView;

    private final Gain masterGain;
    private final Glide masterGainGlide;
    private SynthLFO synthLFO;
    private final OscillatorManager oscillatorManager;
    private final EQFilters filters;
    private final Sequencer sequencer;
    private boolean randomFreq = true;
    private boolean randomGain = true;
    private boolean randomOnOff = true;
    //private final Delay delay;

    /**
     * Setup AudioContext, OscillatorManager and create all necessary links.
     * @author Joel Eriksson Sinclair
     */
    public SynthaxController(SynthaxView synthaxView) {
        this.synthaxView = synthaxView;
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        masterGainGlide = new Glide(ac, 0.5f, 50);
        masterGain = new Gain(ac, 1, masterGainGlide);

        synthLFO = new SynthLFO();
        //oscmanager.getoutput.addinput(lfo)

        oscillatorManager = OscillatorManager.getInstance();
        Gain oscCombined = oscillatorManager.getFinalOutput();

        filters = new EQFilters();
        filters.addInput(oscCombined);

        sequencer = new Sequencer(this);

        masterGain.addInput(filters.getOutput());

        //delay = new Delay(filters.getOutput());
        //masterGain.addInput(delay.getOutput());

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
        oscillatorManager.releaseAllVoices();
    }
    //endregion

    //region Filters (click to open/collapse)
    public void setHPCutoff(float cutoff) {
        filters.setHPCutoff(cutoff);
    }

    public void setHPSlope(float slope) {
        filters.setHPSlope(slope);
    }

    public void setHPActive(boolean newActive) {
        filters.setHPActive(newActive);
    }

    public void setNotchFrequency(float frequency) {
        System.err.println("SynthaxController.setNotchFrequency() - Not implemented.");
    }
    public void setNotchRange(float q) {
        System.err.println("SynthaxController.setNotchRange() - Not implemented.");
    }
    public void setNotchActive(boolean newActive) {
        System.err.println("SynthaxController.setNotchActive() - Not implemented.");
    }

    /**
     * Set the frequency for the LP-filter (all frequencies above this frequency will be lowered.
     * @param cutoff
     */
    public void setLPCutoff(float cutoff) {
        filters.setLPCutoff(cutoff);
    }

    public void setLPSlope(float slope) {
        filters.setLPSlope(slope);
    }

    public void setLPActive(boolean newActive) {
        filters.setLPActive(newActive);
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

    public void setRandomFreq(boolean randomFreq) {
        this.randomFreq = randomFreq;
    }

    public void setRandomGain(boolean randomGain) {
        this.randomGain = randomGain;
    }

    public void setRandomOnOff(boolean randomOnOff) {
        this.randomOnOff = randomOnOff;
    }

    public void randomize(int length) {
        if (randomFreq) {
            randomizeFreq(length);
        }
        if (randomOnOff) {
            randomizeOnOff(length);
        }
        if (randomGain) {
            randomizeGain(length);
        }
    }
    public void randomizeOnOff(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int onOff = random.nextInt(2);
            synthaxView.setSequencerStepsOnOff(onOff == 0, i);
        }
    }
    public void randomizeFreq(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int out = 0;
            for (int j = 0; j < 4; j++) {
                out += random.nextInt(88);
            }
            out /= 4;
            out += 21;
            synthaxView.setSequencerFreqKnobs(MidiNote.values()[out], i);
        }
    }
    public void randomizeGain(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int odds = random.nextInt(100);
            if (odds < 70) {
                synthaxView.setSequencerGain(1, i);
            } else {
                int b = random.nextInt(100);
                float f = b / 100f;
                synthaxView.setSequencerGain(f, i);
            }
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

    public void setMasterGain(float gain) {
        masterGainGlide.setValue(gain);
    }
}
