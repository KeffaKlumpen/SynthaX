package com.synthax.test;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.util.Scanner;

public class TestDelay {
    private float feedbackTime; //detta är en go lösning på feedbackparametern
    private Gain delayGain;
    private float delayGainValue = 0.9f;
    private Glide delayGainGlide;

    public TestDelay() {
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        //DELAY_01:
        //feedBackMedEnvelopeFunkarLiksomInteBuhu(ac);

        //randomSynthFrånHemsidan(ac);

        testaFeedbackMedTypEnSleepKanske(ac);
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

    public void feedBackMedEnvelopeFunkarLiksomInteBuhu(AudioContext ac) {
        Glide modulatorFrequency = new Glide(ac, 20, 30);
        WavePlayer modulator = new WavePlayer(ac, modulatorFrequency, Buffer.SINE);

        Function frequencyModulation = new Function(modulator) {
            @Override
            public float calculate() {
                return (x[0] * 100.0f) + 440f; //+mouseY
            }
        };

        WavePlayer carrier = new WavePlayer(ac, 440f, Buffer.TRIANGLE);

        Envelope gainEnvelope = new Envelope(ac, 0.0f);

        Gain synthGain = new Gain(ac, 1, gainEnvelope);

        synthGain.addInput(carrier);

        TapIn delayIn = new TapIn(ac, 2000.0f);

        delayIn.addInput(synthGain);


        TapOut delayOut = new TapOut(ac, delayIn, 400); //delayvärdet här styr time

        Gain delayGain = new Gain(ac, 1, 1f); //gainvärdet här styr amplitud på ekot

        //OLLIES SKIT
        /*
        Glide myGlide = new Glide(ac, 0);
        Gain myGain = new Gain(ac, 1, myGlide);
        myGain.addInput(delayGain);
        delayIn.addInput(myGain);*/

        //VÅRAN SKIT

        Envelope feedBackEnv = new Envelope(ac, 0.0f);
        Gain feedBackGain = new Gain(ac, 1, feedBackEnv);
        feedBackGain.addInput(delayGain);
        delayIn.addInput(feedBackGain);

        //END VÅRAN SKIT

        delayGain.addInput(delayOut);


        //to feed the delay back into itself, uncomment this line:
        //delayIn.addInput(delayGain);


        ac.out.addInput(synthGain);
        ac.out.addInput(delayGain);
        ac.start();

        float feedBackDuration = 2000f;
        while (true) {
            feedBackEnv.addSegment(1.0f, 1f);
            feedBackEnv.addSegment(0f, feedBackDuration);
            gainEnvelope.addSegment(0.8f, 10);
            gainEnvelope.addSegment(0, 300);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Scanner scn = new Scanner(System.in);
            feedBackDuration = scn.nextFloat();
            //delayOut.setDelay(scn.nextFloat());
        }
    }

    public void testaFeedbackMedTypEnSleepKanske(AudioContext ac) {
        Glide modulatorFrequency = new Glide(ac, 20, 30);
        WavePlayer modulator = new WavePlayer(ac, modulatorFrequency, Buffer.SINE);

        Function frequencyModulation = new Function(modulator) {
            @Override
            public float calculate() {
                return (x[0] * 100.0f) + 440f; //+mouseY
            }
        };

        WavePlayer carrier = new WavePlayer(ac, 440f, Buffer.TRIANGLE);

        Envelope gainEnvelope = new Envelope(ac, 0.0f);

        Gain synthGain = new Gain(ac, 1, gainEnvelope);

        synthGain.addInput(carrier);

        TapIn delayIn = new TapIn(ac, 2000.0f);

        delayIn.addInput(synthGain);


        TapOut delayOut = new TapOut(ac, delayIn, 400); //delayvärdet här styr time

        delayGainGlide = new Glide(ac,delayGainValue, 200.0f);
        delayGain = new Gain(ac, 1, delayGainGlide); //gainvärdet här styr amplitud på ekot

        //OLLIES SKIT
        /*
        Glide myGlide = new Glide(ac, 0);
        Gain myGain = new Gain(ac, 1, myGlide);
        myGain.addInput(delayGain);
        delayIn.addInput(myGain);*/

        delayGain.addInput(delayOut);

        Envelope delayTimeEnv = new Envelope(ac, 0f);
        Gain timeGain = new Gain(ac, 1, delayTimeEnv);
        timeGain.addInput(delayGain);

        //to feed the delay back into itself, uncomment this line:
        //delayIn.addInput(delayGain);
        delayIn.addInput(timeGain);


        // dry = synthGain
        Gain finalDelayGain = new Gain(ac, 1, 0.5f);
        finalDelayGain.addInput(delayGain);

        Gain output = new Gain(ac, 1, 1f);
        output.addInput(synthGain);
        output.addInput(finalDelayGain);

        ac.out.addInput(output);
        ac.start();

        feedbackTime = 2000f;
        while (true) {
            delayGainGlide.setValue(delayGainValue);
            // new FeedBackSleepyHead();

            gainEnvelope.addSegment(0.8f, 10);
            gainEnvelope.addSegment(0, 300);

            delayTimeEnv.addSegment(1f, 10f);
            delayTimeEnv.addSegment(1f, 12000f); // 12000 = how long before hard-cutoff
            delayTimeEnv.addSegment(0f, 10f);

            Scanner scn = new Scanner(System.in);
            feedbackTime = scn.nextFloat();
            //delayOut.setDelay(scn.nextFloat());
        }
    }

    private class FeedBackSleepyHead implements Runnable {

        public FeedBackSleepyHead() {
            new Thread(this).start();
        }
        @Override
        public void run() {
            try {
                Thread.sleep((long)feedbackTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            delayGainGlide.setValue(0.0f);
        }
    }

    public static void main(String[] args) {
        new TestDelay();
    }
}
