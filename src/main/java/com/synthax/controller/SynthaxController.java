package com.synthax.controller;

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
    private final BiquadFilter filterHP;
    private final BiquadFilter filterNotch;
    private final BiquadFilter filterLP;
    private final OscillatorManager oscillatorManager;

    // High-pass
    private final float hpMinFreq = 800f;
    private final float hpMaxFreq = 2500f;
    private final float HPdisableFreq = 0.001f;     // value where the filter is effectively disabled.
    private boolean hpActive = false;
    private float savedHPCutoff = hpMinFreq;
    // Low-pass
    private final float lpMinFreq = 50f;
    private final float lpMaxFreq = 1500f;
    private final float LPdisableFreq = 22000f; // value where the filter is effectively disabled.
    private boolean lpActive = false;
    private float savedLPCutoff = lpMinFreq;

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
        Gain oscCombined = oscillatorManager.getOutput();

        filterHP = new BiquadFilter(ac, 1, BiquadFilter.HP);
        filterHP.addInput(oscCombined);
        filterHP.setFrequency(HPdisableFreq);

        filterNotch = new BiquadFilter(ac, 1, BiquadFilter.NOTCH);
        //filterNotch.addInput(filterHP);
        //filterNotch.setGain(1f);

        filterLP = new BiquadFilter(ac, 1, BiquadFilter.LP);
        filterLP.addInput(filterHP);
        filterLP.setFrequency(LPdisableFreq);

        masterGain.addInput(filterLP);

        // Send to audio-device
        ac.out.addInput(masterGain);
        ac.start();
    }

    //region OscillatorManager forwarding (click to collapse)
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

    public void setHPCutoff(float cutoff) {
        float mapped = map(cutoff, 0f, 1f, hpMinFreq, hpMaxFreq);
        if(hpActive) {
            filterHP.setFrequency(mapped);
        } else {
            savedHPCutoff = mapped;
            System.out.println("Saved: " + savedHPCutoff);
        }
    }

    public void setHPSlope(float slope) {
        float mapped = map(slope, 0f, 1f, 0.1f, 1f);
        filterHP.setQ(mapped);
    }

    public void setHPActive(boolean newActive) {
        hpActive = newActive;

        if(hpActive) {
            filterHP.setFrequency(savedHPCutoff);
        } else {
            savedHPCutoff = filterHP.getFrequency();
            System.out.println("Saved: " + savedHPCutoff);
            filterHP.setFrequency(HPdisableFreq);
            filterHP.reset();
        }
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
        float mapped = map(cutoff, 0f, 1f, lpMinFreq, lpMaxFreq);
        if(lpActive) {
            filterLP.setFrequency(mapped);
        } else {
            savedLPCutoff = mapped;
        }
    }

    public void setLPSlope(float slope) {
        float mapped = map(slope, 0f, 1f, 0.1f, 1f);
        filterLP.setQ(mapped);
    }

    public void setLPActive(boolean newActive) {
        lpActive = newActive;

        if(lpActive) {
            filterLP.setFrequency(savedLPCutoff);
        } else {
            savedLPCutoff = filterLP.getFrequency();
            filterLP.setFrequency(LPdisableFreq);
            filterLP.reset();
        }
    }

    public void setMasterGain(float gain) {
        masterGainGlide.setValue(gain);
    }

    /**
     * Helper function to map a value from one range to another range.
     */
    private float map(float x, float in_min, float in_max, float out_min, float out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
