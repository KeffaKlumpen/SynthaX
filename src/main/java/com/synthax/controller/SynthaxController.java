package com.synthax.controller;

import com.synthax.model.EQFilters;
import com.synthax.model.SynthLFO;
import com.synthax.model.enums.MidiNote;
import com.synthax.view.SynthaxView;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

public class SynthaxController {
    private SynthaxView synthaxView;

    private final Gain masterGain;
    private final Glide masterGainGlide;
    private SynthLFO synthLFO;
    private final OscillatorManager oscillatorManager;
    private final EQFilters filters;

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

        masterGain.addInput(filters.getOutput());

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

    public void setMasterGain(float gain) {
        masterGainGlide.setValue(gain);
    }
}
