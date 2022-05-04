/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.controller;

import com.synthax.model.ADSRValues;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.oscillator.VoiceNormalizer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;

public class NoiseController {
    private final NoiseVoice[] voices;
    private final int voiceCount = 16;
    private int nextVoice = 0;
    private final Gain voiceOutput;
    private final Glide voiceOutputGlide;
    private final int[] voicePlayingMidi = new int[128];
    private float savedGain = 0.5f;

    public NoiseController() {
        voiceOutputGlide = new Glide(0f, 50f);
        voiceOutput = new Gain(1, voiceOutputGlide);

        VoiceNormalizer voiceGainNormalizer = new VoiceNormalizer(voiceCount);
        voiceOutput.addDependent(voiceGainNormalizer);

        voices = new NoiseVoice[voiceCount];
        for (int i = 0; i < voiceCount; i++) {
            NoiseVoice voice = new NoiseVoice();

            voiceGainNormalizer.setInGain(voice.getNaturalGain(), i);
            voiceGainNormalizer.setOutGain(voice.getNormGainGlide(), i);

            voiceOutput.addInput(voice.getNormalizedGain());
            voices[i] = voice;
        }
    }

    public void noteOn(MidiNote midiNote, int velocity) {
        voicePlayingMidi[midiNote.ordinal()] = nextVoice;

        float maxGain = velocity / 127f;
        float sustainGain = maxGain * ADSRValues.getSustainValue();
        voices[nextVoice].playNoise(maxGain, ADSRValues.getAttackValue(), sustainGain, ADSRValues.getDecayValue());

        nextVoice = ++nextVoice % voiceCount;
    }

    public void noteOff(MidiNote midiNote) {
        int voiceIndex = voicePlayingMidi[midiNote.ordinal()];
        voices[voiceIndex].stopPlay(ADSRValues.getReleaseValue());
    }

    public Gain getVoiceOutput() {
        return voiceOutput;
    }

    public void setGain(float gain) {
        voiceOutputGlide.setValue(gain);
    }

    public void setActive(boolean isActive) {
        if(isActive) {
            setGain(savedGain);
        }
        else {
            savedGain = voiceOutputGlide.getTargetValue();
            setGain(0f);
        }
    }
}
