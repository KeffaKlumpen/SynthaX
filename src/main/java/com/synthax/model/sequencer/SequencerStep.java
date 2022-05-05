package com.synthax.model.sequencer;

import com.synthax.model.enums.MidiNote;
import com.synthax.util.BasicMath;

public class SequencerStep {
    private MidiNote midiNote = MidiNote.F4;
    private float detuneCent = 0;
    private int velocity = 127;
    private boolean isOn;
    private Sequencer sequencer;

    public SequencerStep(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    public void play() {
        if (isOn) {
            sequencer.playNote(midiNote, velocity, detuneCent);
        }
    }

    public void stop() {
        if (isOn) {
            sequencer.stopNote(midiNote);
        }
    }

    public void setMidiNote(MidiNote midiNote) {
        this.midiNote = midiNote;
    }

    public void setDetuneCent(float detuneCent) {
        this.detuneCent = detuneCent;
    }

    public void setVelocity(float velocity) {
        float f = BasicMath.map(velocity, 0,1,0,127);
        this.velocity = (int)f;
    }

    public void setIsOn(boolean b) {
        isOn = b;
    }
}
