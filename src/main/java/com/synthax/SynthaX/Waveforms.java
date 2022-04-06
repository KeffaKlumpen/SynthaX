package com.synthax.SynthaX;
import net.beadsproject.beads.data.Buffer;

/**
 * Enumeration of the different waveforms to be used in the application
 * @author Viktor Lenberg
 */

public enum Waveforms {
    SINE(Buffer.SINE),
    TRIANGLE(Buffer.TRIANGLE),
    SQUARE(Buffer.SQUARE),
    SAWTOOTH(Buffer.SAW),
    NOISE(Buffer.NOISE);

    private Buffer buffer;

    /**
     * Sets the appropriate Java Beads-Buffer to the field variable Buffer
     * @param buffer provided by each enumeration
     */
    Waveforms(Buffer buffer) {
        this.buffer = buffer;
    }

    public Buffer getBuffer() {
        return buffer;
    }
}
