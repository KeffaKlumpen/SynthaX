/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.test;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

import java.util.Scanner;

public class TestPolyPhonic {

    Oscillator[] oscillators;

    TestPolyPhonic(){
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        Gain masterGain = new Gain(ac, 1, 1f);

        oscillators = new Oscillator[3];
        for (int i = 0; i < 3; i++) {
            Buffer waveBuf = i == 0 ? Buffer.SINE : i == 1 ? Buffer.SAW : Buffer.TRIANGLE;
            Oscillator polyOsc = new Oscillator(waveBuf);
            masterGain.addInput(polyOsc.getOutput());
            oscillators[i] = polyOsc;
        }

        ac.out.addInput(masterGain);
        ac.start();

        System.out.println("Ready to play!");

        /*
         * How to use:
         * "m#" to toggle mute, where # is index of polyOscillator.
         * "#" to trigger release for all oscillators, # is voice index.
         * "#" to play a frequency, where # is frequency above 50hz.
         */

        Scanner sc = new Scanner(System.in);
        String key = sc.nextLine();
        while (!key.equals("")){
            try {
                if(key.charAt(0) == 'm'){
                    int index = Integer.parseInt(key.substring(1));
                    oscillators[index].toggleMute();
                    continue;
                }

                float freq = Float.parseFloat(key);

                if(freq < 50f){
                    int oscIndex = (int) freq;
                    for (int i = 0; i < 3; i++) {
                        oscillators[i].release(oscIndex);
                    }
                }
                else {
                    int voiceIndex = 0;
                    for (int i = 0; i < 3; i++) {
                        voiceIndex = oscillators[i].playFreq(freq);
                    }
                    System.out.println("I should stop " + voiceIndex + " later.");
                }

            } catch (NumberFormatException e){
                System.out.println("Not valid!");
            }

            key = sc.nextLine();
        }

        System.out.println("--QUIT--");
    }

    public static void main(String[] args) {
        new TestPolyPhonic();
    }

    class Oscillator {
        private final int voiceCount = 8;
        private int currentVoice = 0;

        private final Voice[] voices;

        public Gain getOutput() {
            return output;
        }

        private final Gain output;

        public Oscillator(Buffer waveBuf){
            output = new Gain(1, 1f);

            voices = new Voice[voiceCount];
            for (int i = 0; i < voiceCount; i++) {
                Voice osc = new Voice(waveBuf);
                output.addInput(osc.getOutput());
                printInputs(output, "polyOutput");
                voices[i] = osc;
            }
        }

        public int playFreq(float freq){
            System.out.println("polyOsc: playFreq: " + currentVoice);
            int voiceToPlay = currentVoice;
            voices[currentVoice++].playFreq(freq);
            currentVoice = currentVoice % voiceCount;
            return voiceToPlay;
        }

        public void release(int index){
            if(index >= 0 && index < voices.length){
                voices[index].stopPlay();
            }
        }

        int gain = 1;
        public void toggleMute(){
            output.setGain(++gain % 2);
        }

        /**
         * THIS IS THE ACTUAL PLAYING OSCILLATOR
         */
        class Voice {
            private final WavePlayer wp;
            private final Gain output;
            private final Envelope gainEnv;

            private static float maxGain = .2f;

            public Voice(Buffer waveBuf){
                wp = new WavePlayer(0f, waveBuf);
                gainEnv = new Envelope();
                output = new Gain(1, gainEnv);
                output.addInput(wp);
            }

            public UGen getOutput(){
                return output;
            }

            // pass through attack/delay/sustain?
            public void playFreq(float freq){
                System.out.println("osc: playFreq");
                wp.setFrequency(freq);
                gainEnv.clear();
                gainEnv.addSegment(maxGain, 200f);
                gainEnv.addSegment(maxGain * 0.5f, 200f);
            }

            // pass through release?
            public void stopPlay(){
                System.out.println("stopping osc.");
                gainEnv.addSegment(0f, 1000f);
            }
        }
    }

    public void printInputs(UGen ugen, String alias){
        System.out.println("Inputs to " + alias + ":");
        UGen[] inputs = ugen.getConnectedInputs().toArray(new UGen[0]);
        for (UGen u : inputs) {
            System.out.println(u);
        }
    }

}
