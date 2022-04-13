package com.synthax.SynthaX;

import com.synthax.SynthaX.oscillator.Oscillator;
import com.synthax.controller.OscillatorManager;
import com.synthax.util.MidiHelpers;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

public class Synth {
    private final Gain masterGain;
    private LFO lfo;

    private final OscillatorManager oscillatorManager;

    /**
     * Setup AudioContext, OscillatorManager and create all necessary links.
     * @author Joel Eriksson Sinclair
     */
    public Synth(){
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        masterGain = new Gain(ac, 1, .5f);

        lfo = new LFO(ac);

        oscillatorManager = OscillatorManager.getInstance();
        masterGain.addInput(oscillatorManager.getOutput());

        // Send to audio-device
        ac.out.addInput(masterGain);
        ac.start();
    }

    /**
     * @param oscillator
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorDown(Oscillator oscillator){
        oscillatorManager.moveOscillatorDown(oscillator);
    }

    /**
     * @param oscillator
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorUp(Oscillator oscillator){
        oscillatorManager.moveOscillatorUp(oscillator);
    }

    /**
     * @param oscillator
     * @author Joel Eriksson Sinclair
     */
    public void addOscillator(Oscillator oscillator){
        oscillatorManager.addOscillator(oscillator);
    }

    /**
     * @param oscillator
     * @author Joel Eriksson Sinclair
     */
    public void removeOscillator(Oscillator oscillator) {
        oscillatorManager.removeOscillator(oscillator);
    }

    /**
     * Convert keyPress to a midi-message
     * @param c
     */
    public void noteOn(char c) {
        int midiNote = MidiHelpers.charToMidi(c);

        oscillatorManager.noteOn(midiNote, 128);
    }

    /**
     * Convert key-release to a midi-message
     * @param c
     */
    public void noteOff(char c){
        int midiNote = MidiHelpers.charToMidi(c);

        oscillatorManager.noteOff(midiNote);
    }

    public void releaseAllVoices(){
        System.out.println("Synth release all voices");
        oscillatorManager.releaseAllVoices();
    }

    public void setMasterGain(float gain){
        masterGain.setGain(gain);
    }
}
