package com.synthax.SynthaX;

import com.synthax.SynthaX.ChainableUGens.ChainableUGen;
import com.synthax.SynthaX.ChainableUGens.Oscillator;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

import java.util.ArrayList;

public class Synth {
    private AudioContext ac;
    private Gain masterGain;
    private Glide masterGainGlide;

    /**
     Rubrikerna OSC 1, FM Oscillator och OSC 2 kan bli fristående JavaFX.Controller klasser.
     Dessa klasser behöver ha en "public UGen getOutput()" och en "public void setInput(UGen ugen)"
     SynthController eller liknande kommer sen hantera alla dessa Controller klasser i en lista,
     och endast anropa getOutput eller setInput (när vi lägger till en ny, eller när vi ändrar ordningen i listan).

     T.ex. Osc 1 returnerar osc1 genom getOutput
     T.ex. FM Oscillator kan sen göra setInput(osc1.getOutput()),
        hur FM Oscillator implementerar input hanteras internt i klassen.
    */
    public Synth(){
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);
        masterGainGlide = new Glide(ac, 0.0f, 10.0f);
        masterGain = new Gain(ac, 1, masterGainGlide);

        /*
        ///// OSC 1
        WavePlayer osc1wp = new WavePlayer(ac, 150.f, Buffer.SINE);
        Gain gain1 = new Gain(ac, 1, 1f);
        gain1.addInput(osc1wp);
        // "osc1" represents the output from this first Oscillator.
        Add osc1 = new Add(ac, 1, gain1);
        //osc1.addInput(??) --- since osc1 is the first in the line, we do not add any input here
        ///// OSC 1 END

        ///// FM Oscillator
        WavePlayer freqModulator = new WavePlayer(ac, 2, Buffer.SINE);
        Mult freqMod = new Mult(ac, 1, freqModulator);
        freqMod.addInput(osc1); // route gain1 through the freqMod
        // "freqMod" is the output of this FM Osc effect.
        ///// FM Oscillator END

        ///// OSC 2
        WavePlayer osc2wp = new WavePlayer(ac, 600f, Buffer.SAW);
        Gain gain2 = new Gain(ac, 1, .5f);
        gain2.addInput(osc2wp);
        // "osc2" is the output of this second Oscillator, combined with any previous signal.
        Add osc2 = new Add(ac, 1, gain2);
        osc2.addInput(freqMod);
        ///// OSC 2 END

        // Here we should route through other UGens (oscillators and stuff)
        //masterGain.addInput(osc2);
         */

        // Send to audio-device
        ac.out.addInput(masterGain);
        ac.start();
    }

    private ArrayList<ChainableUGen> ugens = new ArrayList<>();

    public void addToChain(ChainableUGen newUGen){
        // Adding it to the end of the list for now..

        if(ugens.size() > 0){
            ChainableUGen prev = ugens.get(ugens.size() - 1);
            newUGen.setInput(prev.getOutput());
        }

        masterGain.clearInputConnections();
        masterGain.addInput(newUGen.getOutput());

        ugens.add(newUGen);

        System.out.println("Added " + newUGen + " to Synth!");
    }

    public void keyPressed(){
        System.out.println("Synth down");
        masterGainGlide.setValue(.2f);
    }

    public void keyReleased(){
        System.out.println("Synth up");
        masterGainGlide.setValue(0f);
    }

    public void removeOscillator(Oscillator osc) {
        //remove oscillator and connect ugens in chain
    }
}
