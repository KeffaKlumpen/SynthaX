package com.synthax.controller;

import com.synthax.model.ADSRValues;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.oscillator.VoiceNormalizer;
import com.synthax.util.HelperMath;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;

/**
 * Forwards midi-messages to a number of noise generating voices to control when they are active.
 * @author Joel Eriksson Sinclair
 */
public class NoiseController {
    private final NoiseVoice[] voices;
    private final int voiceCount = 16;
    private int nextVoice = 0;
    private final Gain voiceOutput;
    private final Glide voiceOutputGlide;
    private final int[] voicePlayingMidi = new int[128];
    private float savedGain = 0.5f;
    private boolean isActive = false;

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
        float mapped = HelperMath.map(gain, 0f, 1f, 0f, 0.5f);

        if(isActive) {
            voiceOutputGlide.setValue(mapped);
        } else {
            savedGain = mapped;
        }
    }

    public void setActive(boolean newActive) {
        isActive = newActive;

        if(isActive) {
            voiceOutputGlide.setValue(savedGain);
        } else {
            savedGain = voiceOutputGlide.getTargetValue();
            voiceOutputGlide.setValue(0f);
        }
    }
}
