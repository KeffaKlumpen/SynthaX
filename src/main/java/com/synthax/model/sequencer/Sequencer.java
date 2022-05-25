package com.synthax.model.sequencer;

import com.synthax.controller.SynthaxController;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.SequencerMode;
import com.synthax.util.HelperMath;

import java.util.Random;

/**
 * This class handles the sequencer
 * It creates, starts and stops the Thread that controls the playing of sound
 * This class also controls the time interval between steps
 * @author Luke Eales
 * @author Axel Nilsson
 */

public class Sequencer implements Runnable {
    private final SynthaxController synthaxController;
    private final SequencerStep[] steps = new SequencerStep[16];
    private Thread thread;
    private SequencerMode sequencerMode = SequencerMode.loop;
    private boolean bollenModeAsc = true;
    private boolean running;
    private int count = 0;
    private int msBetweenBeats = 100;
    private int nSteps = 16;

    public Sequencer(SynthaxController sc) {
        synthaxController = sc;
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

    public void startRickRoll() {
        count = 0;
        thread = new Thread(new RickRoll());
        running = true;
        thread.start();
    }

    public void setSequencerMode(SequencerMode sequencerMode) {
        this.sequencerMode = sequencerMode;
    }

    public void setOnOff(int i, boolean on) {
        steps[i].setIsOn(on);
    }

    public void setNSteps(int nSteps) {
        this.nSteps = nSteps;
    }

    public SequencerMode getSequencerMode() {
        return sequencerMode;
    }

    public int getNSteps() {
        return nSteps;
    }

    public void setBPM(float rate) {
        float temp = HelperMath.map(rate, 0, 1, 60, 240);
        temp = 60000 / (temp * 4);
        msBetweenBeats = (int)temp;
    }

    public float getRate() {
        float bpm = 60000f / (msBetweenBeats * 4f);
        return HelperMath.map(bpm, 60, 240, 0, 1);
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

    public SequencerStep[] getSteps() {
        return steps;
    }

    public boolean isRunning() {
        return running;
    }

    public Thread getThread() {
        return thread;
    }

    public void playNote(MidiNote midiNote, int velocity, float detuneCent) {
        synthaxController.noteOn(midiNote, velocity, detuneCent);
    }

    public void stopNote(MidiNote midiNote) {
        synthaxController.noteOff(midiNote);
    }

    // This whole thing should run on a thread that isn't main...
    public void saveSequencer() throws InterruptedException {
        Sequencer myThis = this;

        Thread saver = new Thread(new Runnable() {
            @Override
            public void run() {

                if(thread != null) {
                    try {
                        myThis.thread.join(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(thread != null && thread.isAlive()) {
                    System.err.println("CANT SAVE WHILE SEQUENCER IS RUNNING!");
                    return;
                }

                // SeqPresetLoader.getInstance().savePresetToFile(myThis, null); // FIXME: 2022-05-12 Pass in name from GUI
            }
        });
        saver.start();
    }

    public void loadSequencer() throws InterruptedException {
        Sequencer myThis = this;

        Thread loader = new Thread(new Runnable() {
            @Override
            public void run() {

                if(thread != null) {
                    try {
                        myThis.thread.join(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(thread != null && thread.isAlive()) {
                    System.err.println("CANT LOAD WHILE SEQUENCER IS RUNNING!");
                    return;
                }
                // SeqPresetLoader.getInstance().loadFromPreset(myThis);

                for (int i = 0; i < steps.length; i++) {
                    SequencerStep step = steps[i];
                    synthaxController.updateSeqStepGUI(i, step.isOn(), step.getVelocity(), step.getDetuneCent(), step.getMidiNote());
                }
            }
        });
        loader.start();
    }

    @Override
    public void run() {
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
            switch (sequencerMode) {
                case loop -> loopMode();
                case bollen -> bollenMode();
                case reverse -> reverseMode();
                case random -> randomMode();
                default -> loopMode();
            }
            if (count == 16) {
                count = 0;
            }
        }
    }
    
    private void loopMode() {
        count = (count + 1) % nSteps;
    }
    
    private void bollenMode() {
        if (count == 0) {
            bollenModeAsc = true;
        } else if (count == nSteps-1) {
            bollenModeAsc = false;
        }
        if (bollenModeAsc) {
            count++;
        } else {
            count--;
        }
    }
    
    private void reverseMode() {
        count--;
        if (count == -1) {
            count = nSteps-1;
        }
    }
    
    private void randomMode() {
        Random random = new Random();
        count = random.nextInt(nSteps);
    }

    private class RickRoll implements Runnable {
        @Override
        public void run() {
            int x = 0;
            synthaxController.setUpSteps(x);
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
                loopMode();
                if (count == 15) {
                    x = (x + 1) % 4;
                    synthaxController.setUpSteps(x);
                }
            }
        }
    }
}
