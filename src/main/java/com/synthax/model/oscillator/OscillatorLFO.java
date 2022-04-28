package com.synthax.model.oscillator;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.WavePlayer;

public class OscillatorLFO {
    private WavePlayer lfo;

    public OscillatorLFO(AudioContext ac) {
        lfo = new WavePlayer(ac, 20f, Buffer.SINE);
    }


}
