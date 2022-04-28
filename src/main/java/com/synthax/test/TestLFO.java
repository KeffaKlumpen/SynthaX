package com.synthax.test;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.util.Scanner;

public class TestLFO {

    public TestLFO() {
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);


        // FREAQ ETT

        WavePlayer lfo = new WavePlayer(ac, 4f, Buffer.SINE);

        Function frequencyModulation = new Function(lfo) {
            @Override
            public float calculate() {
                return (x[0] * 50f) + 440.0f;
            }
        };

        WavePlayer oscillator = new WavePlayer(ac, frequencyModulation, Buffer.SINE);

        Gain masterGain = new Gain(ac, 1, 0.5f);

        masterGain.addInput(oscillator);

        ac.out.addInput(masterGain);
        ac.start();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            float freq = scanner.nextFloat();
            lfo.setFrequency(freq);
        }



        //FREAKY DEAKY 02

        /*
        Glide modulatorFrequency = new Glide(ac, 0.1f, 30);
        WavePlayer modulator = new WavePlayer(ac, modulatorFrequency, Buffer.SINE);

        Function frequencyModulation = new Function(modulator) {
            @Override
            public float calculate() {
                return (x[0] * 300.0f) + 440f;
            }
        };

        WavePlayer carrier = new WavePlayer(ac, frequencyModulation, Buffer.SINE);

        Gain g = new Gain(ac, 1, 0.5f);

        g.addInput(carrier);

        ac.out.addInput(g);
        ac.start();
        */





/*
        Glide modulatorFrequency = new Glide(ac, 20,30);
        WavePlayer modulator = new WavePlayer(ac, modulatorFrequency, Buffer.SINE);

        Function frequencyModulation = new Function(modulator) {

            @Override
            public float calculate() {
                return (x[0] * 100.0f) + 200; //mouseY;
            }
        };

        WavePlayer carrier = new WavePlayer(ac, frequencyModulation, Buffer.SINE);

        Envelope gainEnvelope = new Envelope(ac, 0.0f);

        Gain synthGain = new Gain(ac, 1, gainEnvelope);

        synthGain.addInput(carrier);

        ac.out.addInput(synthGain);
        ac.start();

        //modulatorFrequency.setValue();

        gainEnvelope.addSegment(0.8f, 50);
        gainEnvelope.addSegment(0.0f, 300);


 */
        /*

        WavePlayer oscillator = new WavePlayer(ac, 440f, Buffer.SINE);

        WavePlayer lfo = new WavePlayer(ac, 1f, Buffer.SINE);


        Function frequencyModulation = new Function(lfo) {
            @Override
            public float calculate() {
                return (x[0] * 0.5f) + 1 * 0.5f;
            }
        };




        Add gumma = new Add(ac,1,1);
        Mult grabben = new Mult(ac, 1, 0.5f);
        gumma.addInput(lfo);
        grabben.addInput(gumma);


        Gain gubben = new Gain(ac, 1, gumma);

        gubben.addInput(oscillator);
        ac.out.addInput(gubben);
        ac.start();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            float freq = scanner.nextFloat();
            lfo.setFrequency(freq);
        }
        */
    }

    public static void main(String[] args) {
        new TestLFO();
    }
}
