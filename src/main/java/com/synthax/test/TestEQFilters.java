/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.test;

import com.synthax.model.enums.MidiNote;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.util.Scanner;

public class TestEQFilters {
    private Gain masterGain;
    private AudioContext ac;

    TestEQFilters() {
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        masterGain = new Gain(ac, 1, .2f);

        float oscGain = 0.5f;
        WavePlayer osc1 = new WavePlayer(MidiNote.C4.getFrequency(), Buffer.SQUARE);
        Gain gain1 = new Gain(1, oscGain);
        gain1.addInput(osc1);
        WavePlayer osc2 = new WavePlayer(MidiNote.C3.getFrequency(), Buffer.SQUARE);
        Gain gain2 = new Gain(1, oscGain);
        gain2.addInput(osc2);

        Gain oscTotal = new Gain(1, 1f);
        oscTotal.addInput(gain1);
        oscTotal.addInput(gain2);

        BiquadFilter filter = new BiquadFilter(1, BiquadFilter.Type.LP);
        filter.addInput(oscTotal);
        filter.setFrequency(MidiNote.C3.getFrequency());

        masterGain.addInput(filter);
        ac.out.addInput(masterGain);
        ac.start();

        Scanner input = new Scanner(System.in);
        while (true) {
            filter.setQ(input.nextFloat());
        }
    }

    public static void main(String[] args) {
        new TestEQFilters();
    }
}
