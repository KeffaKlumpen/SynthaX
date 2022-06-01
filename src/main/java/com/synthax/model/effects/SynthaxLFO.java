package com.synthax.model.effects;

import com.synthax.model.enums.Waveforms;
import com.synthax.util.HelperMath;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

/**
 * An LFO that modifies the amplitude of an incoming signal as determined by the depth and rate of the LFO.
 * Specify the rate and depth with the setRate()- and setDepth()-methods.
 * @author Joel Eriksson Sinclair
 */
public class SynthaxLFO {
    private static final float MIN_RATE = 0.1f;
    private static final float MAX_RATE = 20f;
    private static final float DEPTH_DISABLE = 0f;

    private final WavePlayer lfo;
    private final Static depth;
    private final Gain output;

    private float savedDepth = DEPTH_DISABLE;
    private boolean isActive = false;

    public SynthaxLFO() {
        AudioContext ac = AudioContext.getDefaultContext();
        depth = new Static(DEPTH_DISABLE);
        lfo = new WavePlayer(ac, MIN_RATE, Buffer.SINE);

        // v0
        Static v0 = new Static(1f);

        // v1
        // adjust the lfo-modulator from (-1 to 1) >> (0-1)
        Add add = new Add(1, 1);
        add.addInput(lfo);
        Mult v1 = new Mult(1, 0.5f);
        v1.addInput(add);

        // v0 + t * (v1 - v0)
        Mult v0neg = new Mult(1, -1f);
        v0neg.addInput(v0);

        Add v1negv0 = new Add(1, v1);
        v1negv0.addInput(v0neg);

        Mult txv1negv0 = new Mult(1, depth); // depth = t = lerp amount
        txv1negv0.addInput(v1negv0);

        Add f = new Add(1, v0);
        f.addInput(txv1negv0);

        // Send result of "v0 + t * (v1 - v0)" to output.
        output = new Gain(1, f);
    }

    public Gain getOutput() {
        return output;
    }

    public void setInput(UGen source) {
        output.clearInputConnections();
        output.addInput(source);
    }

    public void setWaveform(Waveforms waveform) {
        lfo.setBuffer(waveform.getBuffer());
    }

    public void setRate(float rate) {
        float mapped = HelperMath.map(rate, 0f, 1f, MIN_RATE, MAX_RATE);
        lfo.setFrequency(mapped);
    }

    public void setDepth(float depth) {
        if(isActive) {
            this.depth.setValue(depth);
        } else {
            savedDepth = depth;
        }
    }

    public void setActive() {
        isActive = !isActive;

        if(isActive) {
            depth.setValue(savedDepth);
        }
        else {
            savedDepth = depth.getValue();
            depth.setValue(DEPTH_DISABLE);
        }
    }
}
