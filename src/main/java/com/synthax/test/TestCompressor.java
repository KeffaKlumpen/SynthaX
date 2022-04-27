package com.synthax.test;

import com.synthax.model.enums.MidiNote;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Compressor;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

import java.util.Arrays;

public class TestCompressor {

    private Gain masterGain;
    private Function normalizer;
    private UGen normalizeUgen;
    private AudioContext ac;

    TestCompressor() {
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        masterGain = new Gain(ac, 1, 1f);

        int oscCount = 3;

        WavePlayer[] wps = new WavePlayer[oscCount];

        for (int i = 0; i < oscCount; i++) {
            // wps[i] = new WavePlayer(MidiNote.values()[57 + (i * 4)].getFrequency(), Buffer.SINE);
            if(i == 0){
                wps[i] = new WavePlayer(MidiNote.C4.getFrequency(), Buffer.SINE);
            }
            if(i == 1){
                wps[i] = new WavePlayer(MidiNote.E4.getFrequency(), Buffer.SINE);
            }
            if(i == 2){
                wps[i] = new WavePlayer(MidiNote.G4.getFrequency(), Buffer.SINE);
            }
            Gain gain = new Gain(1, 1f);
            gain.addInput(wps[i]);
            masterGain.addInput(gain);
        }

        normalizeUgen = new UGen(ac, 1, 1) {
            @Override
            public void calculateBuffer() {
                float[] bi = bufIn[0];
                float[] bo = bufOut[0];

                float peak = -1000f;
                for (int i = 0; i < bufferSize; i++) {
                    float b = Math.abs(bi[i]);
                    if(b > peak){
                        peak = b;
                    }
                }

                // System.out.println(peak);

                for (int i = 0; i < bufferSize; i++) {
                    if(peak == 0f){
                        bo[i] = bi[i];
                    }
                    else {
                        bo[i] = bi[i] / peak;
                    }
                }

                // System.out.println(Arrays.toString(bo));
            }
        };

        normalizeUgen.addInput(masterGain);

        ac.out.addInput(normalizeUgen);
        ac.start();

        debug();
    }

    private void debug() {
        final int[] count = {0};
        Thread debugger = new Thread(new Runnable() {
            @Override
            public void run() {
                while (count[0] < 100000) {
                    float[] ob = ac.out.getOutBuffer(0);
                    float peak = -100f;
                    for (int i = 0; i < ob.length; i++) {
                        float b = Math.abs(ob[i]);
                        if (b > peak) {
                            peak = b;
                        }
                    }
                    System.out.println(Arrays.toString(ob));
                    count[0]++;
                }
            }
        });
        debugger.start();
    }

    public static void main(String[] args) {
        new TestCompressor();
    }
}
