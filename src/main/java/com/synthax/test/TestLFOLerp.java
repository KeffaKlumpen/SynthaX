package com.synthax.test;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.util.Scanner;

public class TestLFOLerp {

    private Gain masterGain;
    private AudioContext ac;

    TestLFOLerp() {
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        masterGain = new Gain(ac, 1, .2f);

        // v0
        WavePlayer lfo = new WavePlayer(ac, 2f, Buffer.SQUARE);
        WavePlayer carrier = new WavePlayer(ac, 440f, Buffer.SINE);
        Add add = new Add(1, 1);
        Mult v0 = new Mult(1, 0.5f);

        add.addInput(lfo);
        v0.addInput(add);

        // v1
        Static v1 = new Static(1f);

        Static depth = new Static(0f);

        // v0 + t * (v1 - v0)
        Mult v0neg = new Mult(1, -1f);
        v0neg.addInput(v0);

        Add v1negv0 = new Add(1, v1);
        v1negv0.addInput(v0neg);

        Mult txv1negv0 = new Mult(1, depth);
        txv1negv0.addInput(v1negv0);

        Add f = new Add(1, v0);
        f.addInput(txv1negv0);

        //Glide glide = new Glide(0f, 50f);
        //glide.addInput(f);
        Gain gain = new Gain(1, f);
        gain.addInput(carrier);

        masterGain.addInput(gain);
        ac.out.addInput(masterGain);
        ac.start();

        Scanner input = new Scanner(System.in);
        while (true) {
            depth.setValue(input.nextFloat());
        }
    }

    private void debug() {
        final int[] count = {0};
        Thread debugger = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                }

                /*
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
                 */
            }
        });
        debugger.start();
    }

    public static void main(String[] args) {
        new TestLFOLerp();
    }
}
