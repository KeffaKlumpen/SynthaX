/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.synthax_beads.ChainableUGens;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Add;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 *      ///// OSC 1
 *         WavePlayer osc1wp = new WavePlayer(ac, 150.f, Buffer.SINE);
 *         Gain gain1 = new Gain(ac, 1, 1f);
 *         gain1.addInput(osc1wp);
 *         // "osc1" represents the output from this first Oscillator.
 *         Add osc1 = new Add(ac, 1, gain1);
 *         //osc1.addInput(??) --- since osc1 is the first in the line, we do not add any input here
 *      ///// OSC 1 END
 */

public class Oscillator extends ChainableUGen {
    @FXML private Slider freqSlider;
    @FXML private Slider gainSlider;

    private WavePlayer wavePlayer;
    private Gain gain;

    @Override
    public void setup(){
        AudioContext ac = AudioContext.getDefaultContext();

        wavePlayer = new WavePlayer(ac, 150f, Buffer.SINE);
        gain = new Gain(ac, 1, 1f);
        gain.addInput(wavePlayer);
        output = new Add(ac, 1, gain);

        System.out.println("Setup complete");
    }

    @FXML
    protected void setFreq() {
        float f = (float)freqSlider.getValue();
        wavePlayer.setFrequency(f);
        System.out.println("new freq: " + wavePlayer.getFrequency());
    }

    @FXML
    protected void setGain() {
        gain.setGain((float)gainSlider.getValue());
    }
}
