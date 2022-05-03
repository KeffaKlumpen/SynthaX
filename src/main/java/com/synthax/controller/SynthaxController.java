package com.synthax.controller;

import com.synthax.model.SynthLFO;
import com.synthax.model.enums.MidiNote;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

public class SynthaxController {
    private final Gain masterGain;
    private Glide masterGainGlide;
    private SynthLFO synthLFO;
    private final BiquadFilter filterHP;
    private final BiquadFilter filterNotch;
    private final BiquadFilter filterLP;
    private final OscillatorManager oscillatorManager;

    private boolean hpActive = false;
    private float savedHPCutoff = 0f;
    private float savedLPCutoff = 22000f;

    /**
     * Setup AudioContext, OscillatorManager and create all necessary links.
     * @author Joel Eriksson Sinclair
     */
    public SynthaxController() {
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
        filterHP.setFrequency(0f);

        filterNotch = new BiquadFilter(ac, 1, BiquadFilter.NOTCH);
        //filterNotch.addInput(filterHP);
        //filterNotch.setGain(1f);

        filterLP = new BiquadFilter(ac, 1, BiquadFilter.LP);
        filterLP.addInput(filterHP);
        filterLP.setFrequency(22000f);

        masterGain.addInput(filterHP);

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
        float mapped = map(cutoff, 0f, 1f, 800f, 2500f);
        System.out.println("mapped: " + mapped);
        if(hpActive) {
            filterHP.setFrequency(mapped);
        } else {
            savedHPCutoff = mapped;
            System.out.println("Saved: " + savedHPCutoff);

        }
    }

    public void setHPSlope(float slope) {
        float mapped = map(slope, 0f, 1f, 0.1f, 1f);
        System.out.println("mapped: " + mapped);
        filterHP.setQ(mapped);
    }

    public void setHPActive(boolean isActive) {
        hpActive = isActive;

        if(isActive) {
            filterHP.setFrequency(savedHPCutoff);
        } else {
            savedHPCutoff = filterHP.getFrequency();
            System.out.println("Saved: " + savedHPCutoff);
            filterHP.setFrequency(0f);
        }
    }

    public void setNotchFrequency(float frequency) {
        System.err.println("SynthaxController.setNotchFrequency() - Not implemented.");
    }

    public void setNotchRange(float q) {
        System.err.println("SynthaxController.setNotchRange() - Not implemented.");
    }

    /**
     * Set the frequency for the LP-filter (all frequencies above this frequency will be lowered.
     * @param cutoff
     */
    public void setLPCutoff(float cutoff) {
        float mapped = map(cutoff, 0f, 1f, 50f, 1500f);
        System.out.println("mapped: " + mapped);
        filterLP.setFrequency(mapped);
    }

    public void setLPSlope(float slope) {
        float mapped = map(slope, 0f, 1f, 0.1f, 1f);
        System.out.println("mapped: " + mapped);
        filterLP.setQ(mapped);
    }

    public void bypassLP(boolean bypass) {
        if(bypass) {
            setLPCutoff(savedLPCutoff);
        } else {
            savedLPCutoff = filterLP.getFrequency();
            setLPCutoff(22000f);
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
