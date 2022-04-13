/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.controller;

import com.synthax.SynthaX.oscillator.Oscillator;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

import java.util.ArrayList;

/**
 * This class is responsible for managing Oscillators, and connecting their input/output.
 * @author Joel Eriksson Sinclair
 */
public class OscillatorManager {
    private static OscillatorManager instance;
    public static OscillatorManager getInstance(){
        if(instance == null){
            instance = new OscillatorManager();
        }
        return instance;
    }

    private final ArrayList<Oscillator> oscillators = new ArrayList<>();

    private final Gain output;

    private OscillatorManager(){
        output = new Gain(1, 1f);
        //new DebugThread(2000).start();
    }

    /**
     * @param noteNumber
     * @param velocity
     * @author Joel Eriksson Sinclair
     */
    public void noteOn(int noteNumber, int velocity){
        for (Oscillator osc : oscillators) {
            osc.noteOn(noteNumber, velocity);
        }
    }

    /**
     *
     * @param noteNumber
     * @author Joel Eriksson Sinclair
     */
    public void noteOff(int noteNumber){
        for (Oscillator osc : oscillators) {
            osc.noteOff(noteNumber);
        }
    }

    public void releaseAllVoices(){
        for (Oscillator osc : oscillators) {
            int voiceCount = osc.getVoiceCount();
            for (int i = 0; i < voiceCount; i++) {
                osc.stopVoice(i);
            }
        }
    }

    /**
     * Setup input and output connections for the provided Oscillator and it's neighbours.
     * @param oscillator Oscillator to setup
     * @author Joel Eriksson Sinclair
     */
    public void setupInOuts(Oscillator oscillator){
        int index = oscillators.indexOf(oscillator);

        if(index < 0 || index >= oscillators.size()){
            return;
        }

        // input
        // if we are the first oscillator, or has previous.
        if(index == 0){
            oscillator.setInput(null);
            System.out.println("Setting our input to null");
        }
        else {
            oscillator.setInput(oscillators.get(index - 1).getOutput());
            System.out.println("Setting our input to previous osc.getOutput");
        }

        // output
        // If we are the last oscillator, or has next.
        UGen oscOutput = oscillator.getOutput();
        if(index == oscillators.size() - 1){
            output.clearInputConnections();
            output.addInput(oscOutput);
            System.out.println("Setting total.Output to our output.");
        }
        else {
            oscillators.get(index + 1).setInput(oscOutput);
            System.out.println("Setting nextOsc.input to our output.");
        }
    }

    /**
     * Return the Gain object which all oscillators are chained to.
     * @return output
     */
    public Gain getOutput(){
        return output;
    }



    //region List Managing
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
            if(index < oscillators.size()){
                setupInOuts(oscillators.get(index));
            }
        }
        else {
            output.clearInputConnections();
        }
    }

    /**
     * @param oscillator
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorDown(Oscillator oscillator) {
        int index = oscillators.indexOf(oscillator);
        if(index < 0){
            return;
        }

        if(index == oscillators.size() - 1){
            return;
        }

        Oscillator nextOsc = oscillators.get(index + 1);
        oscillators.add(index, nextOsc);
        oscillators.remove(index + 2);

        // TODO: We need to re-establish in/outs. Which do we need to update?
        setupInOuts(oscillator);
        setupInOuts(nextOsc);

        System.out.println(oscillators);
    }

    /**
     * @param oscillator
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorUp(Oscillator oscillator) {
        int index = oscillators.indexOf(oscillator);
        if(index < 0){
            return;
        }

        if(index == 0){
            return;
        }

        Oscillator prevOsc = oscillators.get(index - 1);
        oscillators.add(index + 1, prevOsc);
        oscillators.remove(index - 1);

        setupInOuts(oscillator);
        setupInOuts(prevOsc);

        System.out.println(oscillators);
    }

    //endregion

    //region Stupid debugging
    /**
     * Prints out the inputs of all oscillators.
     */
    public void debugPrintOscillatorInputs(){
        System.out.println("--DEBUG--");
        for (Oscillator o : oscillators) {
            System.out.println("-----Osc: " + o);
            for (UGen u : o.getOutput().getConnectedInputs()) {
                System.out.println("---has a " + u);
            }
        }
        System.out.println("---END---");
    }

    /**
     * Repeatedly print out stuff. TODO: Make this a Util class and pass in a method basically.
     */
    class DebugThread extends Thread{

        private final long sleepTime;

        public DebugThread(long sleepTime){
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            while (!isInterrupted()){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                debugPrintOscillatorInputs();
            }
        }
    }
    //endregion
}
