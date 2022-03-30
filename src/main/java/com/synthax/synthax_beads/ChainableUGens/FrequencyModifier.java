/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.synthax_beads.ChainableUGens;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Mult;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * ///// FM Oscillator
        WavePlayer freqModulator = new WavePlayer(ac, 2, Buffer.SINE);
        Mult freqMod = new Mult(ac, 1, freqModulator);
        freqMod.addInput(osc1); // route gain1 through the freqModlic class FrequencyModifier implements ChainableUGen {
        // "freqMod" is the output of this FM Osc effect.
        ///// FM Oscillator END private UGen output; //Add or Mult
  */
public class FrequencyModifier extends ChainableUGen {
    private WavePlayer freqModulator;

    @Override
    public void setup(){
        AudioContext ac = AudioContext.getDefaultContext();
        freqModulator = new WavePlayer(ac, 2, Buffer.SINE);
        output = new Mult(ac, 1, freqModulator);
    }
}
