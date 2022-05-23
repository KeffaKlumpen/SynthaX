package com.synthax.model.oscillator;

import com.synthax.controller.NoiseController;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;

/**
 * Generates a soundwave of white noise.
 * @author Joel Eriksson Sinclair
 */
public class NoiseVoice extends Voice {

    public NoiseVoice(NoiseController controller, int voiceIndex){
        super(controller, voiceIndex);

        Noise noiseGenerator = new Noise();
        naturalGain.addInput(noiseGenerator);
    }

    @Override
    public UGen getOutput() {
        return normalizedGain;
    }
}
