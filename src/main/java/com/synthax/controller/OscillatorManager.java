package com.synthax.controller;

import com.synthax.model.enums.MidiNote;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

import java.util.ArrayList;

/**
 * This class is responsible for managing Oscillators, and connecting their input/output.
 * This class also handles messages to the different oscillators in the list
 * @author Joel Eriksson Sinclair
 */
public class OscillatorManager {
    private static OscillatorManager instance;
    public static OscillatorManager getInstance() {
        if(instance == null) {
            instance = new OscillatorManager();
        }
        return instance;
    }

    private final Gain oscillatorOutput;
    private final Gain finalOutput;

    private final ArrayList<OscillatorController> oscillatorControllers = new ArrayList<>();

    private final NoiseController noiseController;

    private OscillatorManager() {
        noiseController = new NoiseController(VoiceController.VOICE_COUNT);
        oscillatorOutput = new Gain(1, 1f);
        finalOutput = new Gain(1, 1f);
        finalOutput.addInput(noiseController.getOutput());
        finalOutput.addInput(oscillatorOutput);
    }

    public void noteOn(MidiNote midiNote, int velocity) {
        for (OscillatorController osc : oscillatorControllers) {
            osc.noteOn(midiNote, velocity);
        }
        noiseController.noteOn(midiNote, velocity);
    }

    public void noteOn(MidiNote midiNote, int velocity, float detuneCent) {
        for (OscillatorController osc : oscillatorControllers) {
            osc.noteOn(midiNote, velocity, detuneCent);
        }
        noiseController.noteOn(midiNote, velocity);
    }

    public void noteOff(MidiNote midiNote) {
        for (OscillatorController osc : oscillatorControllers) {
            osc.noteOff(midiNote);
        }

        noiseController.noteOff(midiNote);
    }

    /**
     * Setup input and output connections for the provided Oscillator and it's neighbours.
     * @param oscillatorController Oscillator to setup
     * @author Joel Eriksson Sinclair
     */
    public void setupInOuts(OscillatorController oscillatorController) {
        int index = oscillatorControllers.indexOf(oscillatorController);

        if(index < 0 || index >= oscillatorControllers.size()) {
            System.err.println("OscillatorManager.setupInOuts() - Oscillator Index out of bounds.");
            return;
        }

        // Set oscillators input
        // If we are the first oscillator in the chain
        if(index == 0) {
            oscillatorController.setInput(null);
        }
        // If we have previous oscillators in the chain, set our input to the previous output.
        else {
            oscillatorController.setInput(oscillatorControllers.get(index - 1).getOutput());
        }

        // Set oscillators output
        // If we are the last oscillator in the chain, set our output
        UGen oscOutput = oscillatorController.getOutput();
        if(index == oscillatorControllers.size() - 1) {
            oscillatorOutput.clearInputConnections();
            oscillatorOutput.addInput(oscOutput);
        }
        // If there are more oscillators in the chain, set their input to our output.
        else {
            oscillatorControllers.get(index + 1).setInput(oscOutput);
        }
    }

    //region List Managing (click to open/collapse)
    public void addOscillator(OscillatorController osc) {
        oscillatorControllers.add(osc);
        setupInOuts(osc);
    }

    /**
     * Remove the specified Oscillator from the chain. Link up any neighbouring Oscillators correctly.
     * @param osc Oscillator to be removed
     * @author Joel Eriksson Sinclair
     */
    public void removeOscillator(OscillatorController osc) {
        int index = oscillatorControllers.indexOf(osc);

        if(index < 0 || index >= oscillatorControllers.size()) {
            System.err.println("OscillatorManager.removeOscillator() - Oscillator Index out of bounds.");
            return;
        }

        oscillatorControllers.remove(index);

        int previous = index - 1;

        // This can cause some overlap, setting the same thing multiple times... Shit's dumb
        if(oscillatorControllers.size() > 0) {
            if(previous >= 0) {
                setupInOuts(oscillatorControllers.get(previous));
            }
            if(index < oscillatorControllers.size()) {
                setupInOuts(oscillatorControllers.get(index));
            }
        } else {
            oscillatorOutput.clearInputConnections();
        }
    }

    public void setOscillatorVoiceCount(int newVoiceCount) {
        for (OscillatorController osc : oscillatorControllers) {
            osc.setVoiceCount(newVoiceCount);
        }
        noiseController.setVoiceCount(newVoiceCount);
    }

    public void moveOscillatorDown(OscillatorController oscillatorController) {
        int index = oscillatorControllers.indexOf(oscillatorController);
        if(index < 0) {
            return;
        }

        if(index == oscillatorControllers.size() - 1) {
            return;
        }

        OscillatorController nextOsc = oscillatorControllers.get(index + 1);
        oscillatorControllers.add(index, nextOsc);
        oscillatorControllers.remove(index + 2);

        // TODO: We need to re-establish in/outs. Which do we need to update?
        setupInOuts(oscillatorController);
        setupInOuts(nextOsc);

    }

    public void moveOscillatorUp(OscillatorController oscillatorController) {
        int index = oscillatorControllers.indexOf(oscillatorController);
        if(index < 0) {
            return;
        }

        if(index == 0) {
            return;
        }

        OscillatorController prevOsc = oscillatorControllers.get(index - 1);
        oscillatorControllers.add(index + 1, prevOsc);
        oscillatorControllers.remove(index - 1);

        setupInOuts(oscillatorController);
        setupInOuts(prevOsc);
    }
    //endregion List Managing

    public Gain getFinalOutput() {
        return finalOutput;
    }

    public NoiseController getNoiseController() {
        return noiseController;
    }

    //region Delay-setters (click to open/collapse)
    public void setDelayFeedback(float feedBackDuration) {
        for(OscillatorController controller : oscillatorControllers) {
            controller.setDelayFeedback(feedBackDuration);
        }
    }

    public void setDelayTime(float delayTime) {
        for (OscillatorController controller : oscillatorControllers) {
            controller.setDelayTime(delayTime);
        }
    }

    public void setDelayDecay(float decayValue) {
        for (OscillatorController controller : oscillatorControllers) {
            controller.setDelayDecay(decayValue);
        }
    }

    public void setDelayLevel(float levelValue) {
        for (OscillatorController controller : oscillatorControllers) {
            controller.setDelayLevel(levelValue);
        }
    }

    public void setDelayActive() {
        for (OscillatorController controller : oscillatorControllers) {
            controller.setDelayActive();
        }
    }
    // endregion Delay-setters

    public void applyPitchBend(float detuneCent) {
        for (OscillatorController oscillatorController : oscillatorControllers) {
            oscillatorController.setDetuneCent(detuneCent);
        }
    }
}
