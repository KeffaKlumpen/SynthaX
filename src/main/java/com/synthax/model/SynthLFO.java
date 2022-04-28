package com.synthax.model;

import com.synthax.model.enums.Waveforms;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.*;

public class SynthLFO {
    private WavePlayer lfo;
    private float rate = 20f;
    private Waveforms waveform = Waveforms.SINE;
    private float depth = 0f;

    public SynthLFO() {
        lfo = new WavePlayer(AudioContext.getDefaultContext(), rate, waveform.getBuffer());



        //gammelt bös
        /*
        WavePlayer lfo = new WavePlayer(ac, 20f, Buffer.SINE);

        Add gumma = new Add(ac,1,1);
        Mult grabben = new Mult(ac, 1, 0.5f);
        gumma.addInput(lfo);
        grabben.addInput(gumma);
        Gain gubben = new Gain(ac, 1, grabben);

        gubben.addInput(oscillator);

         */
    }

    public WavePlayer getLfo() {
        return lfo;
    }

    public void setLfo(WavePlayer lfo) {
        this.lfo = lfo;
    }

    public Waveforms getWaveform() {
        return waveform;
    }

    public void setWaveform(Waveforms waveform) {
        this.waveform = waveform;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }
}
