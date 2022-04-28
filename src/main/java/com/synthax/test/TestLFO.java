package com.synthax.test;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

import java.util.Scanner;

public class TestLFO {

    public TestLFO() {
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);


        WavePlayer lfo = new WavePlayer(ac, 1f, Buffer.SINE);

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

    }

    public static void main(String[] args) {
        new TestLFO();
    }
}
