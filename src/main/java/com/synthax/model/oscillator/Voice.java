package com.synthax.model.oscillator;

import com.synthax.controller.VoiceController;
import com.synthax.model.enums.MidiNote;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;

public abstract class Voice {
    private final VoiceController controller;
    private final int voiceIndex;

    protected final Gain naturalGain;
    private final Envelope gainEnv;
    protected final Gain normalizedGain;
    protected final Glide normalizedGainGlide;

    protected MidiNote currentNote;

    /**
     * Implementations should do naturalGain.addInput() and pass in their sound generator.
     * @param controller
     * @param voiceIndex
     */
    public Voice(VoiceController controller, int voiceIndex) {
        this.controller = controller;
        this.voiceIndex = voiceIndex;

        gainEnv = new Envelope();
        naturalGain = new Gain(1, gainEnv);

        normalizedGainGlide = new Glide(0f, 10f);
        normalizedGain = new Gain(1, normalizedGainGlide);
        normalizedGain.addInput(naturalGain);
    }

    public void noteOn(MidiNote midiNote, float detunedFrequency, float maxGain, float attackTime, float sustainGain, float decayTime) {
        currentNote = midiNote;

        gainEnv.clear();
        gainEnv.addSegment(maxGain, attackTime);
        gainEnv.addSegment(sustainGain, decayTime);

        if(sustainGain <= 0.01f) {
            float maxPlayTime = attackTime + decayTime;
            controller.notifyAvailableVoice(voiceIndex, maxPlayTime);
        }
    }

    public void noteOff(MidiNote note, float releaseTime) {
        if(note == currentNote) {
            gainEnv.clear();
            gainEnv.addSegment(0f, releaseTime);

            controller.notifyAvailableVoice(voiceIndex, releaseTime);
        }
    }

    public void stopPlay() {
        gainEnv.clear();
    }

    public abstract UGen getOutput();
    public Gain getNaturalGain() { return  naturalGain; }
    public Glide getNormalizedGainGlide() { return normalizedGainGlide; }
}
