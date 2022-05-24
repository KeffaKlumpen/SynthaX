package com.synthax.sample_player.model;

import com.synthax.controller.SynthaxController;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.SequencerMode;
import com.synthax.model.sequencer.Sequencer;
import com.synthax.model.sequencer.SequencerStep;
import com.synthax.sample_player.controller.SamplePlayerController;
import com.synthax.util.HelperMath;

import java.util.ArrayList;
import java.util.Random;

public class SamplePlayerSequencer implements Runnable {
    private SamplePlayerController samplePlayerController;
    private final SamplePlayerSequencerStep[] steps = new SamplePlayerSequencerStep[16];
    private Thread thread;
    private boolean running;
    private int count = 0;
    private int msBetweenBeats = 100;
    private Pad[] pads;

    public SamplePlayerSequencer(SamplePlayerController spc, Pad[] pads) {
        samplePlayerController = spc;
        this.pads = pads;
        setup();
    }

    public void start() {
        count = 0;
        if (thread == null) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            running = false;
            thread = null;
        }
    }

    public void setBPM(float rate) {
        float temp = HelperMath.map(rate, 0, 1, 60, 240);
        temp = 60000 / (temp * 4);
        msBetweenBeats = (int)temp;
    }

    private void setup() {
        for (int i = 0; i < steps.length; i++) {
            steps[i] = new SamplePlayerSequencerStep(this);
        }
    }

    public SamplePlayerSequencerStep[] getSteps() {
        return steps;
    }

    public boolean isRunning() {
        return running;
    }

    public Thread getThread() {
        return thread;
    }

    public void playSamples(ArrayList<Integer> indexes) {
        for (Integer index : indexes) {
            pads[index].playPad();
        }
    }

    public void clearSequencer() {
        for (SamplePlayerSequencerStep step : steps) {
            step.clearStep();
        }
    }

    public void setPadActive(boolean active, int step, int pad) {
        steps[step].setPadActive(pad, active);
    }

    @Override
    public void run() {
        while (running) {
            steps[count].checkPads();
            samplePlayerController.setStepIndicatorOrange(count);
            try {
                Thread.sleep(msBetweenBeats);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            samplePlayerController.setStepIndicatorGray(count);
            count = ++count % 16;
        }
    }
}
