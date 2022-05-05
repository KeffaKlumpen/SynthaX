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
        Scanner input = new Scanner(System.in);

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

        int filterCount = input.nextInt();
        BiquadFilter[] filters = new BiquadFilter[filterCount];
        for (int i = 0; i < filterCount; i++) {
            BiquadFilter filter = new BiquadFilter(1, BiquadFilter.BUTTERWORTH_HP);
            filter.setFrequency(MidiNote.C3.getFrequency());

            // set up chaining
            if(i == 0) {
                filter.addInput(oscTotal);
            }
            else {
                filter.addInput(filters[i - 1]);
            }

            filters[i] = filter;
        }

        // lp to remove buzz
        BiquadFilter lp = new BiquadFilter(1, BiquadFilter.BUTTERWORTH_LP);
        lp.setFrequency(2000);
        lp.addInput(filters[filterCount - 1]);

        masterGain.addInput(filters[filterCount - 1]);
        ac.out.addInput(masterGain);
        ac.start();

        while (true) {
            float freq = input.nextFloat();
            for (int i = 0; i < filterCount; i++) {
                filters[i].setFrequency(freq);
            }
        }
    }

    public static void main(String[] args) {
        new TestEQFilters();
    }
}
