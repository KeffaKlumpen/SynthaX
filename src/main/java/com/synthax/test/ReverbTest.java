package com.synthax.test;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.util.Scanner;

public class ReverbTest {
    private float glideTime = 2000f;
    private float reverbDecay = 2000f;

    public ReverbTest() {
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        WavePlayer wp = new WavePlayer(ac, 440f, Buffer.SINE);
        Glide gainGlide = new Glide(ac, 0.0f, 20);
        Envelope gainEnvelope = new Envelope(ac, 0.0f);
        Gain masterGain = new Gain(ac, 1, gainEnvelope);
        masterGain.addInput(wp);

        Reverb reverb = new Reverb(ac, 1);
        Envelope reverbEnvelope = new Envelope(ac, 1.0f);
        Glide reverbGlide = new Glide(ac, 1.0f, 20f);
        Gain reverbGain = new Gain(ac, 1, reverbGlide);
        reverbGain.addInput(reverb);
        reverb.setSize(0.9f);
        reverb.setDamping(0.5f);
        //man kan osså sätta late och earlyreflexönslevels
        reverb.addInput(masterGain);


        ac.out.addInput(reverbGain);
        ac.out.addInput(masterGain);
        ac.start();

        while (true) {
            reverbEnvelope.addSegment(1.0f, 1f);
            reverbEnvelope.addSegment(0.0f, reverbDecay);
            gainEnvelope.addSegment(0.8f, 20);
            gainEnvelope.addSegment(0.8f, 500);
            gainEnvelope.addSegment(0.0f, 20);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Scanner scn = new Scanner(System.in);
            //reverb.setSize(scn.nextFloat()); // ÄNDRAR SIZE
            //reverb.setDamping(scn.nextFloat());   //ÄNDRAR TONE
            reverb.setLateReverbLevel(scn.nextFloat());
            //reverb.setEarlyReflectionsLevel(scn.nextFloat());
            //reverbGlide.setValue(scn.nextFloat());  //ändrar DRY/WET
        }
    }

    public static void main(String[] args) {
        new ReverbTest();
    }
}
