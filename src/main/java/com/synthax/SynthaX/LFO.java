package com.synthax.SynthaX;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.WavePlayer;

public class LFO {
    private WavePlayer lfo;

    public LFO(AudioContext ac) {
        lfo = new WavePlayer(ac, 20f, Buffer.SINE);
    }


}
