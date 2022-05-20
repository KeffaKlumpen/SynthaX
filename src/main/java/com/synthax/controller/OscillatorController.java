package com.synthax.controller;

import com.synthax.model.oscillator.VoiceNormalizer;
import com.synthax.model.enums.Waveforms;
import com.synthax.model.SynthaxADSR;
import com.synthax.model.enums.CombineMode;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.OctaveOperands;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
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
public class OscillatorController {
    private final OscillatorVoice[] voices;
    private final int voiceCount = 16;
    private int nextVoice = 0;
    private final Gain voiceOutput;
    private final Glide voiceOutputGlide;
    private UGen oscillatorOutput;
    private OctaveOperands octaveOperand = OctaveOperands.EIGHT;
    private float detuneCent;
    private final int[] voicePlayingMidi = new int[128]; // each index corresponds to a MIDI-note, stores voice-indexes

    /**
     * Setup internal chain structure.
     * @author Joel Eriksson Sinclair
     */
    public OscillatorController() {
        voiceOutputGlide = new Glide(AudioContext.getDefaultContext(), 0.5f, 50);
        voiceOutput = new Gain(1, voiceOutputGlide);

        VoiceNormalizer voiceGainNormalizer = new VoiceNormalizer(voiceCount);
        voiceOutput.addDependent(voiceGainNormalizer);

        // Instantiate voice objects and setup chain.
        voices = new OscillatorVoice[voiceCount];
        for (int i = 0; i < voiceCount; i++) {
            OscillatorVoice voice = new OscillatorVoice(Buffer.SINE);

            voiceGainNormalizer.setInGain(voice.getNaturalGain(), i);
            voiceGainNormalizer.setOutGain(voice.getNormalizedGainGlide(), i);

            voiceOutput.addInput(voice.getDelay().getOutput());
            voices[i] = voice;
        }

        oscillatorOutput = new Add(1, voiceOutput);
    }

    /**
     * @param midiNote Midi-note to be played.
     * @author Joel Eriksson Sinclair
     */
    public void noteOn(MidiNote midiNote, int velocity) {
        voicePlayingMidi[midiNote.ordinal()] = nextVoice; // This only allows 1 voice per note-press..
        float freq = midiNote.getFrequency();

        freq = applyOctaveOffset(freq);
        float realFrequency = freq;
        freq = applyDetuning(freq);

        float maxGain = velocity / 127f;
        float sustainGain = maxGain * SynthaxADSR.getSustainValue();
        voices[nextVoice].playFreq(freq, maxGain, SynthaxADSR.getAttackValue(), sustainGain, SynthaxADSR.getDecayValue(), realFrequency);

        nextVoice = ++nextVoice % voiceCount;
    }

    /**
     * noteOn for sequencer
     */
    public void noteOn(MidiNote midiNote, int velocity, float detuneCent){
        voicePlayingMidi[midiNote.ordinal()] = nextVoice; // This only allows 1 voice per note-press..
        float freq = midiNote.getFrequency();

        freq = applyDetuning(freq, detuneCent);

        freq = applyOctaveOffset(freq);
        float realFrequency = freq;
        freq = applyDetuning(freq);

        float maxGain = velocity / 127f;
        float sustainGain = maxGain * SynthaxADSR.getSustainValue();
        voices[nextVoice].playFreq(freq, maxGain, SynthaxADSR.getAttackValue(), sustainGain, SynthaxADSR.getDecayValue(), realFrequency);

        nextVoice = ++nextVoice % voiceCount;
    }

    public void noteOff(MidiNote midiNote) {
        int voiceIndex = voicePlayingMidi[midiNote.ordinal()];
        voices[voiceIndex].stopPlay(SynthaxADSR.getReleaseValue());
    }

    // FIXME: 2022-04-07 Bypassing an Mult Oscillator makes it so no sound reaches the output. (Multiplying with the 0-buffer).
    public void bypassOscillator(boolean onOff) {
        for (int i = 0; i < voiceCount; i++) {
            voices[i].bypass(onOff);
        }
    }

    public void setWaveform(Waveforms wf) {
        for (int i = 0; i < voiceCount; i++) {
            voices[i].setWavePlayerBuffer(wf.getBuffer());
        }
    }

    //region GUI forwarding (click to open/collapse)
    public void setOctaveOperand(OctaveOperands octaveOperand) {
        updateOctaveOffset(this.octaveOperand, octaveOperand);
        this.octaveOperand = octaveOperand;
    }

    public void setLFODepth(float depth) {
        for (OscillatorVoice voice : voices) {
            voice.getOscillatorLFO().setDepth(depth);
        }
    }

    public void setLFORate(float rate) {
        for (OscillatorVoice voice : voices) {
            voice.getOscillatorLFO().setRate(rate);
        }
    }

    public void setDetuneCent(float detuneCent) {
        this.detuneCent = detuneCent;

        for (OscillatorVoice voice : voices) {
            voice.updateDetuneValue(detuneCent);
        }
    }

    public void setGain(float gain) {
        voiceOutputGlide.setValue(gain);
        System.out.println("OscillatorController.setGain() = " + gain);
    }
    //endregion GUI forwarding

    //region CombineMode Output (click to open/collapse)
    public void setOutputType(CombineMode combineMode){
        UGen newOutput = null;

        switch (combineMode){
            case ADD -> {
                newOutput = new Add(1, voiceOutput);
            }
            case MULT -> {
                newOutput = new Mult(1, voiceOutput);
            }
        }
        if(newOutput != null){
            oscillatorOutput = newOutput;
            OscillatorManager.getInstance().setupInOuts(this);
        }
    }

    public UGen getOscillatorOutput(){
        return oscillatorOutput;
    }

    public void setInput(UGen input){
        oscillatorOutput.clearInputConnections();
        if(input != null){
            oscillatorOutput.addInput(input);
        }
    }
    //endregion CombineMode Output

    //region frequency-altering-helpers (click to open/collapse)

    private float applyOctaveOffset(float frequency) {
        switch (octaveOperand) {
            case TWO -> {
                return frequency * OctaveOperands.TWO.getValue();
            }
            case FOUR -> {
                return frequency * OctaveOperands.FOUR.getValue();
            }
            case EIGHT -> {
                return frequency * OctaveOperands.EIGHT.getValue();
            }
            case SIXTEEN -> {
                return frequency * OctaveOperands.SIXTEEN.getValue();
            }
            case THIRTYTWO -> {
                return  frequency * OctaveOperands.THIRTYTWO.getValue();
            }
        }
        return frequency;
    }

    /**
     * Updates the played frequency if the user is changing the octaveoperand during playback.
     */
    private void updateOctaveOffset(OctaveOperands oldValue, OctaveOperands newValue) {
        if (oldValue.getOperandValue() > newValue.getOperandValue()) {
            for (OscillatorVoice voice : voices) {
                float freq = voice.getOscillatorLFO().getPlayedFrequency();
                voice.getOscillatorLFO().setPlayedFrequency(freq * 0.5f);
            }
        } else if (oldValue.getOperandValue() < newValue.getOperandValue()) {
            for (OscillatorVoice voice : voices) {
                float freq = voice.getOscillatorLFO().getPlayedFrequency();
                voice.getOscillatorLFO().setPlayedFrequency(freq * 2);
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
        for(OscillatorVoice voice : voices) {
            voice.getDelay().setFeedbackDuration(feedBackDuration);
        }
    }

    public void setDelayTime(float delayTime) {
        for (OscillatorVoice voice : voices) {
            voice.getDelay().setDelayTime(delayTime);
        }
    }

    public void setDelayDecay(float decayValue) {
        for (OscillatorVoice voice : voices) {
            voice.getDelay().setDecay(decayValue);
        }
    }

    public void setDelayLevel(float levelValue) {
        for (OscillatorVoice voice : voices) {
            voice.getDelay().setLevel(levelValue);
        }
    }

    public void setDelayActive() {
        for (OscillatorVoice voice : voices) {
            voice.getDelay().setActive();
        }
    }
    //endregion delay-setters
}
