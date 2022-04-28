package com.synthax.test;

import com.synthax.model.oscillator.VoiceNormalizer;
import com.synthax.model.enums.MidiNote;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

public class TestNormalizedADSR {

    private Gain masterGain;
    private AudioContext ac;

    TestNormalizedADSR() {
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        masterGain = new Gain(ac, 1, .2f);

        int oscCount = 2;

        WavePlayer[] wps = new WavePlayer[oscCount];
        Gain[] gains = new Gain[oscCount];
        Gain[] normalizedGains = new Gain[oscCount];
        Envelope[] adsrEnvs = new Envelope[oscCount];

        Gain combinedVoices = new Gain(1, 1f);

        VoiceNormalizer voiceNormalizer = new VoiceNormalizer(ac, oscCount);
        masterGain.addDependent(voiceNormalizer);

        for (int i = 0; i < oscCount; i++) {
            Buffer buf = Buffer.SAW;
            // region Setup Oscillators
            if(i == 0){
                wps[i] = new WavePlayer(MidiNote.C4.getFrequency(), buf);
            }
            if(i == 1){
                wps[i] = new WavePlayer(MidiNote.E4.getFrequency(), buf);
            }
            if(i == 2){
                wps[i] = new WavePlayer(MidiNote.G4.getFrequency(), buf);
            }
            if(i == 3){
                wps[i] = new WavePlayer(MidiNote.G3.getFrequency(), buf);
            }
            if(i == 4){
                wps[i] = new WavePlayer(MidiNote.C3.getFrequency(), buf);
            }
            if(i == 5){
                wps[i] = new WavePlayer(MidiNote.E3.getFrequency(), buf);
            }
            if(i == 6){
                wps[i] = new WavePlayer(MidiNote.G4.getFrequency(), buf);
            }
            if(i == 7){
                wps[i] = new WavePlayer(MidiNote.G3.getFrequency(), buf);
            }
            if(i > 7) {
                wps[i] = new WavePlayer(MidiNote.G4.getFrequency(), buf);
            }
            //endregion

            Envelope env = adsrEnvs[i] = new Envelope(0f);
            env.addSegment(1f, 2500f);
            // env.addSegment(0f, 1500f);
            Gain g = gains[i] = new Gain(1, env);
            g.addInput(wps[i]);
            Gain gNorm = normalizedGains[i] = new Gain(1, 1f);
            gNorm.addInput(g);

            voiceNormalizer.setInGain(g, i);
            // voiceNormalizer.setOutGain(gNorm, i); // Pass in a Glide object instead! avoids clicks.

            combinedVoices.addInput(gNorm);
        }

        masterGain.addInput(combinedVoices);
        ac.out.addInput(masterGain);
        ac.start();

        new Thread(() -> {
            while (true) {
                System.out.println(normalizedGains[0].getGain());
            }
        }).start();
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
        new TestNormalizedADSR();
    }

}
