/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.util;

import com.synthax.model.enums.MidiNote;
import javafx.scene.input.KeyCode;

/**
 * @author Joel Eriksson Sinclair
 * @author Luke Eales
 */
public class MidiHelpers {

    // 60 = C = 261.63f
    // 62 = D = 293.66f
    // 64 = E = 329.63f
    public static float midiToFreq(int noteNumber){
        return switch (noteNumber) {
            case 55 -> 196.00f;
            case 56 -> 207.65f;
            case 57 -> 220.00f;
            case 58 -> 233.08f;
            case 59 -> 246.94f;
            case 60 -> 261.63f;
            case 61 -> 277.18f;
            case 62 -> 293.66f;
            case 63 -> 311.13f;
            case 64 -> 329.63f;
            case 65 -> 349.23f;
            case 66 -> 369.99f;
            case 67 -> 392.00f;
            case 68 -> 415.30f;
            default -> 0f;
        };
    }

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
