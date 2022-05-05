package com.synthax.model;

import com.synthax.util.BasicMath;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;


public class Delay {
    private final float maxDelayTime = 3000.0f;
    private static float feedBackDuration = 100.0f;
    private float cachedLevelValue;
    private boolean isActive = false;

    private TapIn delayIn;
    private TapOut delayOut;
    private Glide delayGainGlide;
    private Envelope delayFeedbackEnvelope;
    private Glide finalDelayGainGlide;
    private Gain output;

    public Delay(UGen filterOutput) {
        Gain synthGain = new Gain(AudioContext.getDefaultContext(), 1, 1.0f);
        synthGain.addInput(filterOutput);

        delayIn = new TapIn(AudioContext.getDefaultContext(), maxDelayTime);
        delayIn.addInput(synthGain);

        //this object controls the time of the delay
        delayOut = new TapOut(AudioContext.getDefaultContext(), delayIn, 100.0f);

        //this Gain object controls the decay of the delay
        delayGainGlide = new Glide(AudioContext.getDefaultContext(), 0.0f);
        Gain delayGain = new Gain(AudioContext.getDefaultContext(), 1, delayGainGlide);
        delayGain.addInput(delayOut);

        //this Envelope object controls the feedback duration of the delay
        delayFeedbackEnvelope = new Envelope(AudioContext.getDefaultContext(), 0.0f);
        Gain feedBackGain = new Gain(AudioContext.getDefaultContext(), 1, delayFeedbackEnvelope);
        feedBackGain.addInput(delayGain);

        delayIn.addInput(feedBackGain);

        //this Gain object controls the level of the delay
        finalDelayGainGlide = new Glide(AudioContext.getDefaultContext(), 0.0f, 20f);
        Gain finalDelayGain = new Gain(AudioContext.getDefaultContext(), 1, finalDelayGainGlide);
        finalDelayGain.addInput(delayGain);

        output = new Gain(AudioContext.getDefaultContext(), 1, 1.0f);
        output.addInput(synthGain);
        output.addInput(finalDelayGain);
    }

    public Gain getOutput() {
        return output;
    }

    public void setActive(boolean active) {
        if(!active) {
            cachedLevelValue = finalDelayGainGlide.getValue();
            finalDelayGainGlide.setValue(0.0f);
            isActive = false;
        } else {
            finalDelayGainGlide.setValue(cachedLevelValue);
            isActive = true;
        }
    }

    public void setDelayTime(float delayTime) {
        delayOut.setDelay(BasicMath.map(delayTime, 0, 1, 100, 1000));
    }

    public void setDecay(float decayValue) {
        delayGainGlide.setValue(decayValue);
    }

    public void setLevel(float levelValue) {
        if (!isActive) {
            cachedLevelValue = levelValue;
        } else {
            finalDelayGainGlide.setValue(levelValue);
        }
    }

    public void setFeedBackDuration(float feedBackDuration) {
        Delay.feedBackDuration = BasicMath.map(feedBackDuration, 0, 1, 100, 2500);
    }

    public Envelope getEnvelope() {
        return delayFeedbackEnvelope;
    }

    public static float getFeedBackDuration() {
        return feedBackDuration;
    }
}
