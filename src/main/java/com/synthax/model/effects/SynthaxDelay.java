package com.synthax.model.effects;

import com.synthax.util.HelperMath;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;

/**
 * Class that handles the delay effect
 * @author Teodor Wegestål
 * @author Viktor Lenberg
 */
public class SynthaxDelay {
    private final float maxDelayTime = 3000.0f;
    private float feedbackDuration = 100.0f;

    private float cachedLevelValue;
    private float cachedDelayTime;
    private float cachedDecayValue;
    private float cachedFeedbackDuration;
    private boolean isActive = false;

    private TapIn delayIn;
    private TapOut delayOut;
    private Glide decayGlide;
    private Envelope delayFeedbackEnvelope;
    private Glide levelGlide;
    private Gain output;

    public SynthaxDelay(UGen filterOutput) {
        Gain synthGain = new Gain(AudioContext.getDefaultContext(), 1, 1.0f);
        synthGain.addInput(filterOutput);

        delayIn = new TapIn(AudioContext.getDefaultContext(), maxDelayTime);
        delayIn.addInput(synthGain);

        //this object controls the time of the delay
        delayOut = new TapOut(AudioContext.getDefaultContext(), delayIn, 100.0f);

        //this Gain object controls the decay of the delay
        decayGlide = new Glide(AudioContext.getDefaultContext(), 0.0f);
        Gain delayGain = new Gain(AudioContext.getDefaultContext(), 1, decayGlide);
        delayGain.addInput(delayOut);

        //this Envelope object controls the feedback duration of the delay
        delayFeedbackEnvelope = new Envelope(AudioContext.getDefaultContext(), 0.0f);
        Gain feedBackGain = new Gain(AudioContext.getDefaultContext(), 1, delayFeedbackEnvelope);
        feedBackGain.addInput(delayGain);

        delayIn.addInput(feedBackGain);

        //this Gain object controls the level of the delay
        levelGlide = new Glide(AudioContext.getDefaultContext(), 0.0f, 20f);
        Gain finalDelayGain = new Gain(AudioContext.getDefaultContext(), 1, levelGlide);
        finalDelayGain.addInput(delayGain);

        output = new Gain(AudioContext.getDefaultContext(), 1, 1.0f);
        output.addInput(synthGain);
        output.addInput(finalDelayGain);
    }

    public Gain getOutput() {
        return output;
    }

    public void setActive() {
        isActive = !isActive;
        if(!isActive) {
            cachedLevelValue = levelGlide.getValue();
            levelGlide.setValue(0.0f);

            cachedDelayTime = delayOut.getDelay();
            delayOut.setDelay(0.0f);

            cachedDecayValue = decayGlide.getValue();
            decayGlide.setValue(0.0f);

            cachedFeedbackDuration = feedbackDuration;
            feedbackDuration = 0.0f;
        } else {
            levelGlide.setValue(cachedLevelValue);
            delayOut.setDelay(cachedDelayTime);
            decayGlide.setValue(cachedDecayValue);
            feedbackDuration = cachedFeedbackDuration;
        }
    }

    public void setDelayTime(float delayTime) {
        if (!isActive) {
            cachedDelayTime = HelperMath.map(delayTime, 0, 1, 100, 1000);
        } else {
            delayOut.setDelay(HelperMath.map(delayTime, 0, 1, 100, 1000));
        }
    }

    public void setDecay(float decayValue) {
        if (!isActive) {
            cachedDecayValue = decayValue;
        } else {
            decayGlide.setValue(decayValue);
        }
    }

    public void setLevel(float levelValue) {
        if (!isActive) {
            cachedLevelValue = levelValue;
        } else {
            levelGlide.setValue(levelValue);
        }
    }

    public void setFeedbackDuration(float feedbackDuration) {
        if (!isActive) {
            cachedFeedbackDuration = HelperMath.map(feedbackDuration, 0, 1, 100, 2500);
        } else {
            this.feedbackDuration = HelperMath.map(feedbackDuration, 0, 1, 100, 2500);
        }
    }

    public Envelope getEnvelope() {
        return delayFeedbackEnvelope;
    }

    public float getFeedbackDuration() {
        return feedbackDuration;
    }
}
