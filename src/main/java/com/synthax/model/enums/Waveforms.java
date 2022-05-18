package com.synthax.model.enums;

import net.beadsproject.beads.data.Buffer;

/**
 * Enumeration of the different waveforms to be used in the application
 * @author Viktor Lenberg
 * @author Axel Nilsson
 * @author Luke Eales
 */

public enum Waveforms {
    SINE(Buffer.SINE, 0),
    SQUARE(Buffer.SQUARE, 1),
    SAWTOOTH(Buffer.SAW, 2),
    TRIANGLE(Buffer.TRIANGLE, 3);

    private Buffer buffer;
    private int knobValue;
    /**
     * Sets the appropriate Java Beads-Buffer to the field variable Buffer
     * @param buffer provided by each enumeration
     */
    Waveforms(Buffer buffer, int knobValue) {
        this.buffer = buffer;
        this.knobValue = knobValue;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public int getKnobValue() {
        return knobValue;
    }
}
