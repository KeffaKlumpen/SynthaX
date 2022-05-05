package com.synthax.controller;

import com.synthax.model.enums.MidiNote;
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

    private final Gain output;

    private final ArrayList<OscillatorController> oscillatorControllers = new ArrayList<>();

    private OscillatorManager(){
        output = new Gain(1, 1f);
        //new DebugThread(2000).start();
    }

    /**
     * @param midiNote
     * @param velocity
     * @author Joel Eriksson Sinclair
     */
    public void noteOn(MidiNote midiNote, int velocity){
        for (OscillatorController osc : oscillatorControllers) {
            osc.noteOn(midiNote, velocity);
        }
    }

    /**
     * notOn for sequencer
     * @param velocity
     */
    public void noteOn(MidiNote midiNote, int velocity, float detuneCent){
        for (OscillatorController osc : oscillatorControllers) {
            osc.noteOn(midiNote, velocity, detuneCent);
        }
    }

    /**
     *
     * @param midiNote
     * @author Joel Eriksson Sinclair
     */
    public void noteOff(MidiNote midiNote){
        for (OscillatorController osc : oscillatorControllers) {
            osc.noteOff(midiNote);
        }
    }

    public void releaseAllVoices(){
        for (OscillatorController osc : oscillatorControllers) {
            int voiceCount = osc.getVoiceCount();
            for (int i = 0; i < voiceCount; i++) {
                osc.stopVoice(i);
            }
        }
    }

    /**
     * Setup input and output connections for the provided Oscillator and it's neighbours.
     * @param oscillatorController Oscillator to setup
     * @author Joel Eriksson Sinclair
     */
    public void setupInOuts(OscillatorController oscillatorController){
        int index = oscillatorControllers.indexOf(oscillatorController);

        if(index < 0 || index >= oscillatorControllers.size()){
            System.err.println("Oscillator is not found in the chain!");
            return;
        }

        // input
        // if we are the first oscillator, or has previous.
        if(index == 0){
            oscillatorController.setInput(null);
            System.out.println("Setting our input to null");
        }
        else {
            oscillatorController.setInput(oscillatorControllers.get(index - 1).getOscillatorOutput());
            System.out.println("Setting our input to previous osc.getOutput");
        }

        // output
        // If we are the last oscillator, or has next.
        UGen oscOutput = oscillatorController.getOscillatorOutput();
        if(index == oscillatorControllers.size() - 1){
            output.clearInputConnections();
            output.addInput(oscOutput);
            System.out.println("Setting total.Output to our output.");
        }
        else {
            oscillatorControllers.get(index + 1).setInput(oscOutput);
            System.out.println("Setting nextOsc.input to our output.");
        }
    }

    //region List Managing
    /**
     * Adds an oscillator to the end of the chain.
     * @param osc Oscillator to be added
     * @author Joel Eriksson Sinclair
     */
    public void addOscillator(OscillatorController osc){
        oscillatorControllers.add(osc);
        setupInOuts(osc);

        System.out.println("Added " + osc + " to Synth!");
    }

    /**
     * Remove the specified Oscillator from the chain. Link up any neighbouring Oscillators correctly.
     * @param osc Oscillator to be removed
     * @author Joel Eriksson Sinclair
     */
    public void removeOscillator(OscillatorController osc){
        int index = oscillatorControllers.indexOf(osc);

        if(index < 0 || index >= oscillatorControllers.size()){
            return;
        }

        oscillatorControllers.remove(index);

        int previous = index - 1;

        // This can cause some overlap, setting the same thing multiple times... Shit's dumb
        if(oscillatorControllers.size() > 0){
            if(previous >= 0){
                setupInOuts(oscillatorControllers.get(previous));
            }
            if(index < oscillatorControllers.size()){
                setupInOuts(oscillatorControllers.get(index));
            }
        } else {
            output.clearInputConnections();
        }
    }

    /**
     * @param oscillatorController
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorDown(OscillatorController oscillatorController) {
        int index = oscillatorControllers.indexOf(oscillatorController);
        if(index < 0){
            return;
        }

        if(index == oscillatorControllers.size() - 1){
            return;
        }

        OscillatorController nextOsc = oscillatorControllers.get(index + 1);
        oscillatorControllers.add(index, nextOsc);
        oscillatorControllers.remove(index + 2);

        // TODO: We need to re-establish in/outs. Which do we need to update?
        setupInOuts(oscillatorController);
        setupInOuts(nextOsc);

        System.out.println(oscillatorControllers);
    }

    /**
     * @param oscillatorController
     * @author Joel Eriksson Sinclair
     */
    public void moveOscillatorUp(OscillatorController oscillatorController) {
        int index = oscillatorControllers.indexOf(oscillatorController);
        if(index < 0){
            return;
        }

        if(index == 0){
            return;
        }

        OscillatorController prevOsc = oscillatorControllers.get(index - 1);
        oscillatorControllers.add(index + 1, prevOsc);
        oscillatorControllers.remove(index - 1);

        setupInOuts(oscillatorController);
        setupInOuts(prevOsc);

        System.out.println(oscillatorControllers);
    }
    //endregion


    /**
     * Return the Gain object which all oscillators are chained to.
     * @return output
     */
    public Gain getOutput(){
        return output;
    }

    //region Stupid debugging
    /**
     * Prints out the inputs of all oscillators.
     */
    public void debugPrintOscillatorInputs(){
        System.out.println("--DEBUG--");
        for (OscillatorController o : oscillatorControllers) {
            System.out.println("-----Osc: " + o);
            for (UGen u : o.getOscillatorOutput().getConnectedInputs()) {
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
