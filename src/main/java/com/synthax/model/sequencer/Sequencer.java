package com.synthax.model.sequencer;

import com.synthax.controller.SynthaxController;
import com.synthax.model.enums.MidiNote;
import com.synthax.util.BasicMath;
import com.synthax.view.SynthaxView;

public class Sequencer implements Runnable {
    private SynthaxController synthaxController;
    private SequencerStep[] steps = new SequencerStep[16];
    private int msBetweenBeats = 100;
    private boolean running;
    private Thread thread;

    public Sequencer(SynthaxController sc) {
        synthaxController = sc;
        setup();
    }

    public void start() {
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

    public void setOnOff(int i, boolean on) {
        steps[i].setIsOn(on);
    }

    public void setBPM(float rate) {
        float temp = BasicMath.map(rate, 0, 1, 60, 240);
        temp = 60000 / (temp * 4);
        msBetweenBeats = (int)temp;
    }

    public void setStepMidiNote(int i, MidiNote midiNote) {
        steps[i].setMidiNote(midiNote);
    }

    public void setStepDetuneCent(int i, float detuneCent) {
        steps[i].setDetuneCent(detuneCent);
    }

    public void setStepVelocity(int i, float velocity) {
        steps[i].setVelocity(velocity);
    }

    private void setup() {
        for (int i = 0; i < steps.length; i++) {
            steps[i] = new SequencerStep(this);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void playNote(MidiNote midiNote, int velocity, float detuneCent) {
        synthaxController.noteOn(midiNote, velocity, detuneCent);
    }

    public void stopNote(MidiNote midiNote) {
        synthaxController.noteOff(midiNote);
    }

    @Override
    public void run() {
        int count = 0;
        while (running) {
            steps[count].play();
            synthaxController.setSeqButtonOrange(count);
            try {
                Thread.sleep(msBetweenBeats);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synthaxController.setSeqButtonGray(count);
            steps[count].stop();
            count = (count + 1) % steps.length;
        }
    }
}
