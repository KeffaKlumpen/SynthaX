package com.synthax.util;

import com.synthax.model.enums.MidiNote;

/**
 * @author Joel Eriksson Sinclair
 * @author Luke Eales
 */
public class MidiHelpers {
    public static final int MAX_VELOCITY_VALUE = 127;
    public static final int MIDI_NOTE_COUNT = 128;

    public static MidiNote stringToMidi(String code) {
        switch (code) {
            case "a" -> {return MidiNote.C4;}
            case "w" -> {return MidiNote.Db4;}
            case "s" -> {return MidiNote.D4;}
            case "e" -> {return MidiNote.Eb4;}
            case "d" -> {return MidiNote.E4;}
            case "f" -> {return MidiNote.F4;}
            case "t" -> {return MidiNote.Gb4;}
            case "g" -> {return MidiNote.G4;}
            case "y" -> {return MidiNote.Ab4;}
            case "h" -> {return MidiNote.A4;}
            case "u" -> {return MidiNote.Bb4;}
            case "j" -> {return MidiNote.B4;}
            case "k" -> {return MidiNote.C5;}
            case "l" -> {return MidiNote.D5;}
            case "o" -> {return MidiNote.Db5;}
            case "ö" -> {return MidiNote.E5;}
            case "ä" -> {return MidiNote.F5;}
            case "p" -> {return MidiNote.Eb5;}
        }
        return MidiNote.getValues()[0];
    }
}
