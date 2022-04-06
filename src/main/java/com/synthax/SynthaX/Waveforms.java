package com.synthax.SynthaX;
import net.beadsproject.beads.data.Buffer;

public enum Waveforms {
    SINE(Buffer.SINE),
    TRIANGLE(Buffer.TRIANGLE),
    SQUARE(Buffer.SQUARE),
    SAWTOOTH(Buffer.SAW),
    NOISE(Buffer.NOISE);

    private Buffer buffer;

    Waveforms(Buffer buffer) {
        this.buffer = buffer;
    }

    public Buffer getBuffer() {
        return buffer;
    }
}
