package com.synthax.SynthaX;

import com.synthax.SynthaX.oscillator.Oscillator;
import com.synthax.controller.OscillatorManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

import java.util.ArrayList;

public class Synth {
    private final Gain masterGain;
    private final Glide masterGainGlide;
    private ADSR adsr;
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

        masterGainGlide = new Glide(ac, 0.0f, 10.0f);
        adsr = new ADSR(ac);
        masterGain = new Gain(ac, 1, adsr.getEnvelope());

        lfo = new LFO(ac);


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
        //masterGainGlide.setValue(.5f);
        adsr.attackDecay();
    }

    public void keyReleased(){
        System.out.println("Synth up");
        //masterGainGlide.setValue(0f);
        adsr.release();
    }

    public ADSR getADSR() {
        return adsr;
    }

    //Testmetod för att kunna spela olika toner
    public void setFrequency(char c) {

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
