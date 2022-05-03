package com.synthax.controller;

import com.synthax.model.SynthLFO;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.ADSRValues;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

public class SynthaxController {
    private final Gain masterGain;
    private Glide masterGainGlide;
    private SynthLFO synthLFO;
    private BiquadFilter filterHP;
    private BiquadFilter filterNotch;
    private BiquadFilter filterLP;

    private final OscillatorManager oscillatorManager;

    /**
     * Setup AudioContext, OscillatorManager and create all necessary links.
     * @author Joel Eriksson Sinclair
     */
    public SynthaxController(){
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        masterGainGlide = new Glide(ac, 0.5f, 50);
        masterGain = new Gain(ac, 1, masterGainGlide);

        synthLFO = new SynthLFO();
        //oscmanager.getoutput.addinput(lfo)

        oscillatorManager = OscillatorManager.getInstance();
        Gain oscCombined = oscillatorManager.getOutput();

        /* TODO: Dessa ska länkas till knobs. Hjälp.
            filterHP.setFrequency(0-150) -- knob: Cutoff
            filterHP.setQ(0-1) -- knob: slope (värdet ska begränsas mer sen, 0-1 är för generöst)
            filterNotch.setFrequency(150-2500) -- knob: Filter
            filterNotch.setQ(0-1) -- knob: range
            filterLP.setFrequency(0-150) -- knob: Cutoff
            filterLP.setQ(0-1) -- knob: slope (värdet ska begränsas mer sen, 0-1 är för generöst)
         */
        filterHP = new BiquadFilter(ac, 1, BiquadFilter.HP);
        filterHP.addInput(oscCombined);

        filterNotch = new BiquadFilter(ac, 1, BiquadFilter.NOTCH);
        filterNotch.addInput(filterHP);

        filterLP = new BiquadFilter(ac, 1, BiquadFilter.LP);
        filterLP.addInput(filterNotch);

        masterGain.addInput(filterLP);

        // Send to audio-device
        ac.out.addInput(masterGain);
        ac.start();
    }

    /**
     * @param oscillatorController
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorDown(OscillatorController oscillatorController){
        oscillatorManager.moveOscillatorDown(oscillatorController);
    }

    /**
     * @param oscillatorController
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorUp(OscillatorController oscillatorController){
        oscillatorManager.moveOscillatorUp(oscillatorController);
    }

    /**
     * @param oscillatorController
     * @author Joel Eriksson Sinclair
     */
    public void addOscillator(OscillatorController oscillatorController){
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

    public void releaseAllVoices(){
        System.out.println("Synth release all voices");
        oscillatorManager.releaseAllVoices();
    }

    public void setMasterGain(float gain){
        masterGainGlide.setValue(gain);
        ADSRValues.setPeakGain(gain);
    }
}
