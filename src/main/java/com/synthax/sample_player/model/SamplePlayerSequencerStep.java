package com.synthax.sample_player.model;

import java.util.ArrayList;

public class SamplePlayerSequencerStep {
    private SamplePlayerSequencer sequencer;
    boolean[] padsActivated = new boolean[9];

    public SamplePlayerSequencerStep(SamplePlayerSequencer sequencer) {
        this.sequencer = sequencer;
    }

    public void checkPads() {
        ArrayList<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < padsActivated.length; i++) {
            if (padsActivated[i]) {
                indexes.add(i);
            }
        }

        sequencer.playSamples(indexes);
    }

    public void clearStep() {
        for (int i = 0; i < padsActivated.length; i++) {
            padsActivated[i] = false;
        }
    }

    public void setPadActive(int pad, boolean active) {
        padsActivated[pad] = active;
    }
}
