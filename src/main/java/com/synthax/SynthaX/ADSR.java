package com.synthax.SynthaX;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Envelope;

/**
 * Class for controlling the amplitude through an ADSR-envelope.
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 */
public class ADSR {
    private Envelope envelope;
    private float attackValue = 1;
    private float decayValue = 1;
    private float sustainValue = 1;
    private float releaseValue = 1;
    private float peakGain = 0.5f;

    public ADSR (AudioContext ac) {
        envelope = new Envelope(ac, 0.0f);
    }

    /**
     * Routes the playing signal through the attack, and decay segments of the envelope.
     * @author Viktor Lenberg
     * @author Teodor Wegestål
     */
    public void attackDecay() {
        envelope.clear();
        envelope.addSegment(peakGain, attackValue);
        envelope.addSegment(peakGain * sustainValue, decayValue);
    }

    /**
     * Routes the playing signal through the release segment of the envelope.
     * @author Viktor Lenberg
     * @author Teodor Wegestål
     */
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
