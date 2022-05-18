package com.synthax.model.sequencer;

import com.synthax.model.enums.MidiNote;
import com.synthax.util.HelperMath;

/**
 * This class controls an individual step in the step sequencer
 * @author Luke Eales
 * @author Axel Nilsson
 */
public class SequencerStep {
    private Sequencer sequencer;
    private MidiNote midiNote = MidiNote.F4;
    private float detuneCent = 0;
    private int velocity = 127;
    private boolean isOn;

    public SequencerStep(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    public MidiNote getMidiNote() {
        return midiNote;
    }

    public float getDetuneCent() {
        return detuneCent;
    }

    public int getVelocity() {
        return velocity;
    }

    public boolean isOn() {
        return isOn;
    }


    public void play() {
        if (isOn) {
            sequencer.playNote(midiNote, velocity, detuneCent);
        }
    }

    public void stop() {
        sequencer.stopNote(midiNote);
    }

    public void setMidiNote(MidiNote midiNote) {
        stop();
        this.midiNote = midiNote;
    }

    public void setDetuneCent(float detuneCent) {
        this.detuneCent = detuneCent;
    }

    public void setVelocity(float velocity) {
        float f = HelperMath.map(velocity, 0,1,0,127);
        this.velocity = (int)f;
    }

    public void setIsOn(boolean b) {
        isOn = b;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SequencerStep{");
        sb.append("sequencer=").append(sequencer);
        sb.append(", midiNote=").append(midiNote);
        sb.append(", detuneCent=").append(detuneCent);
        sb.append(", velocity=").append(velocity);
        sb.append(", isOn=").append(isOn);
        sb.append('}');
        return sb.toString();
    }
}
