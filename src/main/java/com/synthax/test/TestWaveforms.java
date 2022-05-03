/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.test;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

import java.util.Scanner;

public class TestWaveforms {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);
        Gain masterGain = new Gain(1, .2f);

        Buffer buffer = Buffer.SINE;
        System.out.println("Choose the desired Waveform: ");
        System.out.println("1: SINE");
        System.out.println("2: SAW");
        System.out.println("3: SQUARE");
        System.out.println("4: TRIANGLE");

        try {
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice){
                case 1 -> buffer = Buffer.SINE;
                case 2 -> buffer = Buffer.SAW;
                case 3 -> buffer = Buffer.SQUARE;
                case 4 -> buffer = Buffer.TRIANGLE;
            }
        }
        catch (NumberFormatException e){

        }


        WavePlayer osc = new WavePlayer(ac, 392.00f, buffer);
        Envelope adsr = new Envelope();
        Gain oscOutput = new Gain(1, adsr);
        oscOutput.addInput(osc);

        adsr.clear();
        adsr.addSegment(1f, 500);
        adsr.addSegment(1f, 50);

        masterGain.addInput(oscOutput);

        // Send to audio-device
        ac.out.addInput(masterGain);
        ac.start();
    }
}
