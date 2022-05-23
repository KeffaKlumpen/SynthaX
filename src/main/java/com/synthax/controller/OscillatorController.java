package com.synthax.controller;

import com.synthax.model.oscillator.VoiceNormalizer;
import com.synthax.model.enums.Waveforms;
import com.synthax.model.SynthaxADSR;
import com.synthax.model.enums.CombineMode;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.OctaveOperands;
import com.synthax.util.MidiHelpers;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.util.ArrayList;
import java.util.HashMap;

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
    private int voiceCount = 3;
    private OscillatorVoice[] voices;
    private final Gain voiceOutput;
    private final Glide voiceOutputGlide;

    private boolean[] voiceAvailability;
    private final HashMap<Integer, NotifierThread> notifierThreadHashMap = new HashMap<>();
    private final ArrayList<Integer> voiceHistory = new ArrayList<>();
    private final int[] voicePlayingMidi = new int[MidiHelpers.MIDI_NOTE_COUNT]; //stores voice-index playing note

    private OctaveOperands octaveOperand = OctaveOperands.EIGHT;
    private float detuneCent;

    private UGen oscillatorOutput;

    /**
     * Setup internal chain structure.
     * @author Joel Eriksson Sinclair
     */
    public OscillatorController() {
        AudioContext ac = AudioContext.getDefaultContext();
        voiceOutputGlide = new Glide(ac, 0.5f, 50);
        voiceOutput = new Gain(ac,1, voiceOutputGlide);

        setVoiceCount(voiceCount);

        oscillatorOutput = new Add(ac,1, voiceOutput);
    }

    public void setVoiceCount(int newVoiceCount) {
        // clear conncetion
        voiceOutput.clearDependents();
        voiceOutput.clearInputConnections();

        // stop old stuff
        if(voices != null) {
            for (int i = 0; i < voiceCount; i++) {
                voices[i].stopPlay();
            }
        }
        voiceHistory.clear();
        for (NotifierThread notifierThread : notifierThreadHashMap.values()) {
            notifierThread.cancelNotification();
        }
        notifierThreadHashMap.clear();

        // set new variable
        voiceCount = newVoiceCount;

        VoiceNormalizer voiceGainNormalizer = new VoiceNormalizer(voiceCount);
        voiceOutput.addDependent(voiceGainNormalizer);

        // Instantiate voice objects and setup chain.
        voices = new OscillatorVoice[voiceCount];
        voiceAvailability = new boolean[voiceCount];
        for (int i = 0; i < voiceCount; i++) {
            OscillatorVoice voice = new OscillatorVoice(Buffer.SINE, this, i);

            voiceGainNormalizer.setInGain(voice.getNaturalGain(), i);
            voiceGainNormalizer.setOutGain(voice.getNormalizedGainGlide(), i);

            voiceOutput.addInput(voice.getDelay().getOutput());
            voices[i] = voice;
            voiceAvailability[i] = true;
        }
    }


    public synchronized void notifyAvailableVoice(int voiceIndex, float delay) {
        NotifierThread notifierThread = new NotifierThread(voiceIndex, delay);
        if(!notifierThreadHashMap.containsKey(voiceIndex)) {
            notifierThreadHashMap.put(voiceIndex, notifierThread);
            notifierThread.start();
        }
    }

    private synchronized void setVoiceUnavailable(int voiceIndex) {
        voiceAvailability[voiceIndex] = false;
        voiceHistory.add(voiceIndex);
    }

    private synchronized void setVoiceAvailable(int voiceIndex) {
        voiceAvailability[voiceIndex] = true;
        voiceHistory.remove((Object)voiceIndex);
        NotifierThread notifierThread = notifierThreadHashMap.remove(voiceIndex);
        if(notifierThread != null) {
            notifierThread.cancelNotification();
        }
    }

    class NotifierThread extends Thread {
        private final int voiceIndex;
        private final float delay;

        private boolean canceled = false;

        public NotifierThread(int voiceIndex, float delay) {
            this.voiceIndex = voiceIndex;
            this.delay = delay;
        }

        public void cancelNotification() {
            canceled = true;
        }

        @Override
        public void run() {
            try {
                sleep((long)delay);
                if(!canceled) {
                    setVoiceAvailable(voiceIndex);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private synchronized int getBestVoice() {
        int voiceToSteal = -1;

        for (int i = 0; i < voiceCount; i++) {
            if(voiceAvailability[i]) {
                voiceToSteal = i;
                break;
            }
        }

        // no available, steal the oldest!
        if(voiceToSteal < 0) {
            voiceToSteal = voiceHistory.remove(0);
        }

        return voiceToSteal;
    }

    /**
     * Select a suitable voice and tells it to play the given MIDI-note.
     * @param midiNote Midi-note to be played.
     */
    public void noteOn(MidiNote midiNote, int velocity) {
        // Try to get availableVoice, else get oldestVoice.
        int voiceIndex = getBestVoice();

        voicePlayingMidi[midiNote.ordinal()] = voiceIndex;
        float detunedFrequency = midiNote.getFrequency();

        detunedFrequency = applyOctaveOffset(detunedFrequency);
        detunedFrequency = applyDetuning(detunedFrequency);

        float maxGain = velocity / (float) MidiHelpers.MAX_VELOCITY_VALUE;
        float sustainGain = maxGain * SynthaxADSR.getSustainValue();
        voices[voiceIndex].noteOn(midiNote, detunedFrequency, maxGain, SynthaxADSR.getAttackValue(), sustainGain, SynthaxADSR.getDecayValue());
        setVoiceUnavailable(voiceIndex);
    }

    /**
     * Special case with sequencer-step detuning.
     * Select a suitable voice and tells it to play the given MIDI-note.
     * @param midiNote Midi-note to be played.
     */
    public void noteOn(MidiNote midiNote, int velocity, float detuneCent) {
        // Try to get availableVoice, else get oldestVoice.
        int voiceIndex = getBestVoice();

        voicePlayingMidi[midiNote.ordinal()] = voiceIndex; // This only allows 1 voice per note-press..
        float freq = midiNote.getFrequency();

        freq = applyDetuning(freq, detuneCent);

        freq = applyOctaveOffset(freq);
        freq = applyDetuning(freq);

        float maxGain = velocity / (float) MidiHelpers.MAX_VELOCITY_VALUE;
        float sustainGain = maxGain * SynthaxADSR.getSustainValue();
        voices[voiceIndex].noteOn(midiNote, freq, maxGain, SynthaxADSR.getAttackValue(), sustainGain, SynthaxADSR.getDecayValue());
        setVoiceUnavailable(voiceIndex);
    }

    public void noteOff(MidiNote midiNote) {
        int voiceIndex = voicePlayingMidi[midiNote.ordinal()];
        if(voiceIndex < voiceCount) {
            voices[voiceIndex].noteOff(midiNote, SynthaxADSR.getReleaseValue());
        }
        else {
            System.err.println("noteOff on a voice that does not exist anymore.");
        }
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
    }
    //endregion GUI forwarding

    //region CombineMode Output (click to open/collapse)
    public void setOutputType(CombineMode combineMode){
        UGen newOutput = null;

        switch (combineMode){
            case ADD -> newOutput = new Add(1, voiceOutput);
            case MULT -> newOutput = new Mult(1, voiceOutput);
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
