package com.synthax.SynthaX;

import com.synthax.SynthaX.ChainableUGens.ChainableUGen;
import com.synthax.SynthaX.ChainableUGens.Oscillator;
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

        oscillatorManager = new OscillatorManager();
        masterGain.addInput(oscillatorManager.getOutput());

        // Send to audio-device
        ac.out.addInput(masterGain);
        ac.start();
    }

    public void addOscillator(Oscillator oscillator){
        oscillatorManager.addOscillator(oscillator);
    }

    public void removeOscillator(Oscillator osc) {
        oscillatorManager.removeOscillator(osc);
    }

    public void keyPressed(){
        System.out.println("Synth down");
        masterGainGlide.setValue(.2f);
    }

    public void keyReleased(){
        System.out.println("Synth up");
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
