package com.synthax.SynthaX;

import com.synthax.SynthaX.oscillator.Oscillator;
import com.synthax.controller.OscillatorManager;
import com.synthax.model.ADSRValues;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

public class Synth {
    private final Gain masterGain;
    private Glide masterGainGlide;
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

        masterGainGlide = new Glide(ac, 0.5f, 50);
        masterGain = new Gain(ac, 1, masterGainGlide);

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

    public void playNote(float frequency) {
        oscillatorManager.playFrequency(frequency);
    }

    public void releaseVoice(int voiceIndex){

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
