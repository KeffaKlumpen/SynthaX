package com.synthax.test;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.util.Scanner;

public class TestDelay {

    public TestDelay() {
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        //DELAY_01:
        delay01(ac);
        //randomSynthFrånHemsidan(ac);


    }

    private void randomSynthFrånHemsidan(AudioContext ac) {
        WavePlayer toneGenerator = new WavePlayer(ac, 440.0f, Buffer.SINE);
        Envelope gainEnvelope = new Envelope(ac, 0.0f);
        Gain gain = new Gain(ac, 1, gainEnvelope);
        gain.addInput(toneGenerator);

        TapIn delayIn = new TapIn(ac, 500.0f);
        delayIn.addInput(gain);

        TapOut delayOut = new TapOut(ac, delayIn, 125.0f);
        Gain delayGain = new Gain(ac, 1, 0.5f);
        delayGain.addInput(delayOut);
        delayIn.addInput(delayGain);
    }

    public void delay01(AudioContext ac) {
        Glide modulatorFrequency = new Glide(ac, 20, 30);
        WavePlayer modulator = new WavePlayer(ac, modulatorFrequency, Buffer.SINE);

        Function frequencyModulation = new Function(modulator) {
            @Override
            public float calculate() {
                return (x[0] * 100.0f) + 440f; //+mouseY
            }
        };

        WavePlayer carrier = new WavePlayer(ac, 440f, Buffer.SINE);

        Envelope gainEnvelope = new Envelope(ac, 0.0f);

        Gain synthGain = new Gain(ac, 1, gainEnvelope);

        synthGain.addInput(carrier);

        TapIn delayIn = new TapIn(ac, 2000.0f);

        delayIn.addInput(synthGain);


        TapOut delayOut = new TapOut(ac, delayIn, 299); //delayvärdet här styr time

        Gain delayGain = new Gain(ac, 1, 0.6f); //gainvärdet här styr amplitud på ekot

        //OLLIES SKIT
        Glide myGlide = new Glide(ac, 0);
        Gain myGain = new Gain(ac, 1, myGlide);
        myGain.addInput(delayGain);
        delayIn.addInput(myGain);


        delayGain.addInput(delayOut);

        //to feed the delay back into itself, uncomment this line:
        delayIn.addInput(delayGain);

        ac.out.addInput(synthGain);
        ac.out.addInput(delayGain);
        ac.start();


        int kuken = 0;
        while (true) {
            gainEnvelope.addSegment(0.8f, 10);
            gainEnvelope.addSegment(0, 300);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Scanner scn = new Scanner(System.in);
            myGlide.setValue(scn.nextFloat());
            kuken++;
        }
    }

    public static void main(String[] args) {
        new TestDelay();
    }
}
