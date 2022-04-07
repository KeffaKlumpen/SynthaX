/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.controller;

import com.synthax.SynthaX.ChainableUGens.Oscillator;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

import java.util.ArrayList;

/**
 * This class is responsible for managing Oscillators, and connecting their input/output.
 * @author Joel Eriksson Sinclair
 */
public class OscillatorManager {
    private final ArrayList<Oscillator> oscillators = new ArrayList<>();

    private final Gain output;

    public OscillatorManager(){
        output = new Gain(1, 1f);
    }

    /**
     * Adds an oscillator to the end of the chain.
     * @param osc Oscillator to be added
     * @author Joel Eriksson Sinclair
     */
    public void addOscillator(Oscillator osc){
        oscillators.add(osc);
        setupInOuts(osc);

        System.out.println("Added " + osc + " to Synth!");
    }

    //TODO:
    public void moveOscillator(int from, int to){
        Oscillator osc1 = oscillators.get(from);
        Oscillator osc2 = oscillators.get(to);

        System.err.println("NOT IMPLEMENTED YET");
    }


    /**
     * Remove the specified Oscillator from the chain. Link up any neighbouring Oscillators correctly.
     * @param osc Oscillator to be removed
     * @author Joel Eriksson Sinclair
     */
    public void removeOscillator(Oscillator osc){
        int index = oscillators.indexOf(osc);

        if(index < 0 || index >= oscillators.size()){
            return;
        }

        oscillators.remove(index);

        int previous = index - 1;

        // This can cause some overlap, setting the same thing multiple times... Shit's dumb
        if(oscillators.size() > 0){
            if(previous >= 0){
                setupInOuts(oscillators.get(previous));
            }
            setupInOuts(oscillators.get(index));
        }
        else {
            output.clearInputConnections();
        }
    }

    /**
     * Make all the oscillators play a frequency.
     */
    public void playFrequency(float frequency){
        for (Oscillator osc : oscillators) {
            osc.playSound(frequency);
        }
    }

    /**
     * Setup input and output connections for the provided Oscillator object.
     * @param oscillator Oscillator to setup
     */
    private void setupInOuts(Oscillator oscillator){
        int index = oscillators.indexOf(oscillator);

        if(index < 0 || index >= oscillators.size()){
            return;
        }

        // input
        // if we are the first oscillator, or has previous.
        if(index == 0){
            oscillator.setInput(null);
        }
        else {
            oscillator.setInput(oscillators.get(index - 1).getOutput());
        }

        // output
        // If we are the last oscillator, or has next.
        UGen oscOutput = oscillator.getOutput();
        if(index == oscillators.size() - 1){
            output.clearInputConnections();
            output.addInput(oscOutput);
        }
        else {
            oscillators.get(index + 1).setInput(output);
        }
    }

    /**
     * Return the Gain object which all oscillators are chained to.
     * @return output
     */
    public Gain getOutput(){
        return output;
    }
}
