/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.util;

import javafx.scene.input.KeyCode;

/**
 * @author Joel Eriksson Sinclair
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

    public static int charToMidi(char c){
        return switch (c){
            case 'A' -> 57;
            case 'B' -> 59;
            case 'C' -> 60;
            case 'D' -> 62;
            case 'E' -> 64;
            case 'F' -> 65;
            case 'G' -> 67;
            default -> -1;
        };
    }

    public static char keyCodeToChar(KeyCode keyCode){
        if(keyCode == KeyCode.A){
            return 'C';
        }
        else if (keyCode == KeyCode.S){
            return 'D';
        }
        else if (keyCode == KeyCode.D){
            return 'E';
        }
        else if (keyCode == KeyCode.F){
            return 'F';
        }
        else if (keyCode == KeyCode.G){
            return 'G';
        }
        return '?';
    }
}
