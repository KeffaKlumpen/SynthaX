package com.synthax.SynthaX;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Envelope;

public class ADSR {
    private Envelope envelope;
    private float attackValue;
    private float decayValue;
    private float sustainValue = 1;
    private float releaseValue;
    private float peakGain = 0.5f;

    public ADSR (AudioContext ac) {
        envelope = new Envelope(ac, 0.0f);
    }

    public void attackDecay() {
        envelope.clear();
        envelope.addSegment(peakGain, attackValue);
        envelope.addSegment(sustainValue, decayValue);
    }

    public void release() {
        envelope.clear();
        envelope.addSegment(0.0f, releaseValue);
    }

    public void setAttackValue(float attackValue) {
        this.attackValue = attackValue;
    }

    public void setDecayValue(float decayValue) {
        this.decayValue = decayValue;
    }

    public void setSustainValue(float sustainValue) {
        this.sustainValue = sustainValue;
    }

    public float getSustainValue() {
        return sustainValue;
    }

    public void setReleaseValue(float releaseValue) {
        this.releaseValue = releaseValue;
    }

    public void setPeakGain(float peakGain) {
        this.peakGain = peakGain;
    }

    public Envelope getEnvelope() {
        return envelope;
    }
}
