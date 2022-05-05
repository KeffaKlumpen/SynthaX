package com.synthax.model.sequencer;

import com.synthax.model.enums.MidiNote;

public class SequencerStep {
    private MidiNote midiNote = MidiNote.F4;
    private float detuneCent = 0;
    private int velocity = 127;
    private boolean isOn;


    public void play() {
        if (isOn) {
            System.out.println("Play");

        }
    }
}
