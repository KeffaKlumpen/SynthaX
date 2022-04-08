package com.synthax.SynthaX;

import com.synthax.SynthaX.ChainableUGens.Oscillator;
import com.synthax.controller.OscillatorManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

public class Synth {
    private final Gain masterGain;
    private final Glide masterGainGlide;

    private final OscillatorManager oscillatorManager;

    /**
     * Setup AudioContext, OscillatorManager and create all necessary links.
     * @author Joel Eriksson Sinclair
     */
    public Synth(){
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        masterGainGlide = new Glide(ac, 0.0f, 10.0f);
        masterGain = new Gain(ac, 1, masterGainGlide);

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
     *
     * @param oscillator
     * @author Joel Eriksson Sinclair
     */
    public void addOscillator(Oscillator oscillator){
        oscillatorManager.addOscillator(oscillator);
    }

    /**
     *
     * @param oscillator
     * @author Joel Eriksson Sinclair
     */
    public void removeOscillator(Oscillator oscillator) {
        oscillatorManager.removeOscillator(oscillator);
    }

    public void keyPressed(){
        masterGainGlide.setValue(.2f);
    }

    public void keyReleased(){
        masterGainGlide.setValue(0f);
    }

    //Testmetod för att kunna spela olika toner
    public void playNote(char c) {
        switch (c) {
            case 'C' -> {
                masterGainGlide.setValue(.2f);
                oscillatorManager.playFrequency(261.63f);
            }
            case 'D' -> {
                masterGainGlide.setValue(.2f);
                oscillatorManager.playFrequency(293.66f);
            }
            case 'E' -> {
                masterGainGlide.setValue(.2f);
                oscillatorManager.playFrequency(329.63f);
            }
        }
    }
}
