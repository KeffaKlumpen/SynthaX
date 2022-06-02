package com.synthax.controller;

import com.synthax.model.oscillator.OscillatorVoice;
import com.synthax.model.oscillator.Voice;
import com.synthax.model.enums.Waveforms;
import com.synthax.model.enums.CombineMode;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.OctaveOperands;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;

/**
 * Accepts MIDI-signals and forwards these to be played by an available voice.
 * This class also handles messages to the different voices
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 * @author Joel Eriksson Sinclair
 * @author Luke Eales
 * @author Axel Nilsson
 */
public class OscillatorController extends VoiceController {
    private Waveforms waveform = Waveforms.SINE;

    private OctaveOperands octaveOperand = OctaveOperands.EIGHT;
    private float detuneCent;

    private UGen finalOutput;

    /**
     * Setup internal chain structure.
     * @author Joel Eriksson Sinclair
     */
    public OscillatorController(int voiceCount) {
        super(voiceCount);

        finalOutput = new Add(AudioContext.getDefaultContext(), 1, voiceOutput);
    }

    @Override
    protected Voice createVoice(int i) {
        if(waveform == null) {
            waveform = Waveforms.SINE;
        }
        return new OscillatorVoice(waveform.getBuffer(), this, i);
    }

    @Override
    protected float getDetunedFrequency(float baseFreq, float detuneCent) {
        float detunedFreq = applyDetuning(baseFreq, detuneCent);
        detunedFreq = applyOctaveOffset(detunedFreq);
        detunedFreq = applyDetuning(detunedFreq);
        return detunedFreq;
    }

    //region GUI forwarding (click to open/collapse)
    // FIXME: 2022-04-07 Bypassing an Mult Oscillator makes it so no sound reaches the output. (Multiplying with the 0-buffer).
    public void bypassOscillator(boolean onOff) {
        for (int i = 0; i < voiceCount; i++) {
            ((OscillatorVoice)voices[i]).bypass(onOff);
        }
    }

    public void setWaveform(Waveforms waveform) {
        this.waveform = waveform;
        for (Voice voice : voices) {
            ((OscillatorVoice)voice).setWavePlayerBuffer(waveform.getBuffer());
        }
    }

    public void setOctaveOperand(OctaveOperands octaveOperand) {
        updateOctaveOffset(this.octaveOperand, octaveOperand);
        this.octaveOperand = octaveOperand;
    }

    public void setLFODepth(float depth) {
        for (Voice voice : voices) {
            ((OscillatorVoice)voice).getOscillatorLFO().setDepth(depth);
        }
    }

    public void setLFORate(float rate) {
        for (Voice voice : voices) {
            ((OscillatorVoice)voice).getOscillatorLFO().setRate(rate);
        }
    }

    public void setDetuneCent(float detuneCent) {
        this.detuneCent = detuneCent;

        // Update the detune for active voices, allowing the detune effects to be applied dynamically.
        for (int i = 0; i < voiceCount; i++) {
            // Avoid updating detune on silent voices, better performance and also avoid nulls.
            if(!voiceAvailability[i]) {
                ((OscillatorVoice)voices[i]).updateDetuneValue(detuneCent, octaveOperand);
            }
        }
    }

    public void setGain(float gain) {
        voiceOutputGlide.setValue(gain);
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public UGen getOutput() {
        return finalOutput;
    }
    //endregion GUI forwarding

    //region CombineMode Output (click to open/collapse)
    public void setOutputType(CombineMode combineMode) {
        UGen newOutput = null;

        switch (combineMode) {
            case ADD -> newOutput = new Add(1, voiceOutput);
            case MULT -> newOutput = new Mult(1, voiceOutput);
        }
        if(newOutput != null) {
            finalOutput = newOutput;
            OscillatorManager.getInstance().setupInOuts(this);
        }
    }

    public void setInput(UGen input) {
        finalOutput.clearInputConnections();
        if(input != null) {
            finalOutput.addInput(input);
        }
    }
    //endregion CombineMode Output

    //region frequency-altering-helpers (click to open/collapse)
    private float applyOctaveOffset(float frequency) {
        return frequency * octaveOperand.getValue();
    }

    /**
     * Updates the played frequency if the user is changing the octaveoperand during playback.
     */
    private void updateOctaveOffset(OctaveOperands oldValue, OctaveOperands newValue) {
        if (oldValue.getOperandValue() < newValue.getOperandValue()) {
            for (Voice voice : voices) {
                float freq = ((OscillatorVoice)voice).getOscillatorLFO().getPlayedFrequency();
                ((OscillatorVoice)voice).getOscillatorLFO().setPlayedFrequency(freq * 0.5f);
            }
        } else if (oldValue.getOperandValue() > newValue.getOperandValue()) {
            for (Voice voice : voices) {
                float freq = ((OscillatorVoice)voice).getOscillatorLFO().getPlayedFrequency();
                ((OscillatorVoice)voice).getOscillatorLFO().setPlayedFrequency(freq * 2);
            }
        }
    }

    /**
     * Alters the frequency to the correct cent value
     */
    private float applyDetuning(float frequency) {
        return (float)(frequency * (Math.pow(2, (detuneCent/1200))));
    }

    private float applyDetuning(float frequency, float detuneCent) {
        return (float)(frequency * (Math.pow(2, (detuneCent/1200))));
    }
    //endregion frequency-altering-helpers

    //region delay-setters (click to open/collapse)
    public void setDelayFeedback(float feedBackDuration) {
        for(Voice voice : voices) {
            ((OscillatorVoice)voice).getDelay().setFeedbackDuration(feedBackDuration);
        }
    }

    public void setDelayTime(float delayTime) {
        for (Voice voice : voices) {
            ((OscillatorVoice)voice).getDelay().setDelayTime(delayTime);
        }
    }

    public void setDelayDecay(float decayValue) {
        for (Voice voice : voices) {
            ((OscillatorVoice)voice).getDelay().setDecay(decayValue);
        }
    }

    public void setDelayLevel(float levelValue) {
        for (Voice voice : voices) {
            ((OscillatorVoice)voice).getDelay().setLevel(levelValue);
        }
    }

    public void setDelayActive() {
        for (Voice voice : voices) {
            ((OscillatorVoice)voice).getDelay().setActive();
        }
    }
    //endregion delay-setters
}