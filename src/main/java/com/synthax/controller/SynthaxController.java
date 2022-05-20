package com.synthax.controller;

import com.synthax.model.SynthaxEQFilters;
import com.synthax.model.Midi;
import com.synthax.model.SynthaxLFO;
import com.synthax.model.SynthaxReverb;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.Waveforms;
import com.synthax.model.sequencer.Sequencer;
import com.synthax.model.enums.SequencerMode;
import com.synthax.model.sequencer.SequencerStep;
import com.synthax.view.SynthaxView;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.ugens.*;

import java.util.Random;

/**
 * Main controller for the synthesizer
 * Handles audio chain setup, internal structure and forwarding messages to different parts of the application
 * @author Joel Eriksson Sinclair
 * @author Luke Eales
 * @author Axel Nilsson
 * @author Teodor Wegestål
 * @author Viktor Lenberg
 */
public class SynthaxController {
    private final SynthaxView synthaxView;
    private final SeqPresetLoader seqPresetLoader;

    private final Gain masterGain;
    private final Glide masterGainGlide;
    private final SynthaxLFO synthaxLFO;
    private final OscillatorManager oscillatorManager;
    private final SynthaxEQFilters filters;
    private final Sequencer sequencer;
    private final SynthaxReverb reverb;
    private boolean randomFreq = true;
    private boolean randomGain = true;
    private boolean randomOnOff = true;
    private Midi midi;

    /**
     * Setup AudioContext, OscillatorManager and defines the chain of effects and sounds
     * before sending it to the output/system speakers
     * */
    public SynthaxController(SynthaxView synthaxView) {
        this.synthaxView = synthaxView;
        JavaSoundAudioIO jsaio = new JavaSoundAudioIO(512);
        AudioContext ac = new AudioContext(jsaio);
        AudioContext.setDefaultContext(ac);

        sequencer = new Sequencer(this);
        seqPresetLoader = new SeqPresetLoader(sequencer);

        masterGainGlide = new Glide(ac, 0.5f, 50);
        masterGain = new Gain(ac, 1, masterGainGlide);

        oscillatorManager = OscillatorManager.getInstance();
        Gain oscCombined = oscillatorManager.getFinalOutput();

        synthaxLFO = new SynthaxLFO();
        synthaxLFO.setInput(oscCombined);

        filters = new SynthaxEQFilters();
        filters.addInput(synthaxLFO.getOutput());

        reverb = new SynthaxReverb(filters.getOutput());
        masterGain.addInput(reverb.getOutput());

        // Send to audio-device
        ac.out.addInput(masterGain);
        ac.start();

        midi = new Midi(this);
        synthaxView.updateMidiLabel(midi.connectMidi());
    }

    //region OscillatorManager (click to open/collapse)
    public void moveOscillatorDown(OscillatorController oscillatorController) {
        oscillatorManager.moveOscillatorDown(oscillatorController);
    }


    public void moveOscillatorUp(OscillatorController oscillatorController) {
        oscillatorManager.moveOscillatorUp(oscillatorController);
    }

    public void addOscillator(OscillatorController oscillatorController) {
        oscillatorManager.addOscillator(oscillatorController);
    }

    public void removeOscillator(OscillatorController oscillatorController) {
        oscillatorManager.removeOscillator(oscillatorController);
    }
    //endregion OscillatorManager

    //region MIDI-handling (click to open/collapse)
    public void noteOn(MidiNote midiNote, int velocity) {
        oscillatorManager.noteOn(midiNote, velocity);
    }

    public void noteOn(MidiNote midiNote, int velocity, float detuneCent) {
        oscillatorManager.noteOn(midiNote, velocity, detuneCent);
    }
    public void noteOff(MidiNote midiNote){
        oscillatorManager.noteOff(midiNote);
    }
    //endregion MIDI-handling

    //region Filters (click to open/collapse)
    public void setHPCutoff(float cutoff) {
        filters.setHPCutoff(cutoff);
    }

    public void setHPActive() {
        filters.setHPActive();
    }

    public void setEQActive(int i) {
        filters.setEQActive(i);
    }

    public void setEQGain(int i, float newVal) {
        filters.setEQGain(i, newVal);
    }

    public void setEQFreq(int i, float newVal) {
        filters.setEQFrequency(i, newVal);
    }

    public void setEQRange(int i, float newVal) {
        filters.setEQRange(i, newVal);
    }

    public void setLPCutoff(float cutoff) {
        filters.setLPCutoff(cutoff);
    }

    public void setLPActive() {
        filters.setLPActive();
    }
    //endregion Filters

    //region LFO (click to open/collapse)
    public void setLFODepth(float depth) {
        synthaxLFO.setDepth(depth);
    }

    public void setLFORate(float rate) {
        synthaxLFO.setRate(rate);
    }

    public void setLFOWaveform(Waveforms waveform) {
        synthaxLFO.setWaveform(waveform);
    }

    public void setLFOActive() {
        synthaxLFO.setActive();
    }
    //endregion LFO

    //region Noise (click to open/collapse)
    public void setNoiseGain(float gain) {
        oscillatorManager.getNoiseController().setGain(gain);
    }

    public void setNoiseActive() {
        oscillatorManager.getNoiseController().setActive();
    }
    //endregion Noise

    //region Sequencer (click to open/collapse)
    public void setStepOnOff(int i, boolean on) {
        sequencer.setOnOff(i, on);
    }

    public void setSequencerMode(SequencerMode sequencerMode) {
        sequencer.setSequencerMode(sequencerMode);
    }

    public boolean sequencerIsRunning() {
        return sequencer.isRunning();
    }

    public void sequencerOn() {
        sequencer.start();
    }

    public void sequencerOff() {
        sequencer.stop();
    }

    public void setSeqMidiNote(int i, MidiNote midiNote) {
        sequencer.setStepMidiNote(i, midiNote);
    }

    public void setSeqDetuneCent(int i, float detuneCent) {
        sequencer.setStepDetuneCent(i, detuneCent);
    }

    public void setSeqVelocity(int i, float velocity) {
        sequencer.setStepVelocity(i, velocity);
    }

    public void setSeqBPM(float rate) {
        sequencer.setBPM(rate);
    }

    public void setSeqNSteps(int nSteps) {
        sequencer.setNSteps(nSteps);
    }

    public void setSeqButtonOrange(int i) {
        synthaxView.setSeqButtonOrange(i);
    }

    public void setSeqButtonGray(int i) {
        synthaxView.setSeqButtonGray(i);
    }

    public void setRandomFreq(boolean randomFreq) {
        this.randomFreq = randomFreq;
    }

    public void setRandomGain(boolean randomGain) {
        this.randomGain = randomGain;
    }

    public void setRandomOnOff(boolean randomOnOff) {
        this.randomOnOff = randomOnOff;
    }

    public void randomize(int length) {
        if (randomFreq) {
            randomizeFreq(length);
        }
        if (randomOnOff) {
            randomizeOnOff(length);
        }
        if (randomGain) {
            randomizeGain(length);
        }
    }
    public void randomizeOnOff(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int onOff = random.nextInt(2);
            synthaxView.setSequencerStepOnOff(onOff == 0, i);
        }
    }
    public void randomizeFreq(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int out = 0;
            for (int j = 0; j < 4; j++) {
                out += random.nextInt(88);
            }
            out /= 4;
            out += 21;
            synthaxView.setSequencerStepFreq(MidiNote.values()[out], i);
        }
    }
    public void randomizeGain(int length) {
        Random random  = new Random();
        for (int i = 0; i < length; i++) {
            int odds = random.nextInt(100);
            if (odds < 70) {
                synthaxView.setSequencerStepGain(1, i);
            } else {
                int b = random.nextInt(100);
                float f = b / 100f;
                synthaxView.setSequencerStepGain(f, i);
            }
        }
    }

    public void updateSeqStepGUI(int i, boolean isOn, int velocity, float detuneCent, MidiNote midiNote) {
        synthaxView.updateSeqStep(i, isOn, velocity, detuneCent, midiNote);
    }

    //region SequencerPreset (click to expand/collapse)
    public void onSavePreset(String currentPresetName) {
        // delegate the preset-loading to separate thread
        Thread saver = new Thread(new Runnable() {
            @Override
            public void run() {
                // If sequencer is playing, stop it and do the loading after
                Thread sequencerThread = sequencer.getThread();
                if(sequencerThread != null) {
                    synthaxView.fakeSequencerStartStopClick();
                    try {
                        sequencerThread.join(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(sequencerThread != null && sequencerThread.isAlive()) {
                    System.err.println("CANT SAVE WHILE SEQUENCER IS RUNNING!");
                    return;
                }

                seqPresetLoader.savePreset(currentPresetName);
            }
        });
        saver.start();
    }

    public void onSelectPreset(String presetName) {
        Thread loader = new Thread(() -> {
            // If sequencer is playing, stop it and do the loading after
            Thread sequencerThread = sequencer.getThread();
            if(sequencerThread != null) {
                synthaxView.fakeSequencerStartStopClick();
                try {
                    sequencerThread.join(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(sequencerThread != null && sequencerThread.isAlive()) {
                System.err.println("CANT LOAD WHILE SEQUENCER IS RUNNING!");
                return;
            }

            seqPresetLoader.loadPreset(presetName);
            updateSequencerStepsGUI();
        });
        loader.start();
    }

    public void updateSequencerPresetList() {
        String[] presetNames = seqPresetLoader.getPresetNames();
        synthaxView.setSequencerPresetList(presetNames);
    }

    public String[] getSequencerPresetList() {
        return seqPresetLoader.getPresetNames();
    }

    private void updateSequencerStepsGUI() {
        SequencerStep[] steps = sequencer.getSteps();
        for (int i = 0; i < steps.length; i++) {
            SequencerStep step = steps[i];
            updateSeqStepGUI(i, step.isOn(), step.getVelocity(), step.getDetuneCent(), step.getMidiNote());
        }
    }
    //endregion sequencer presets
    //endregion sequencer

    //region Delay (click to open/collapse)
    public void setDelayFeedback(float feedBackDuration) {
        oscillatorManager.setDelayFeedback(feedBackDuration);
    }

    public void setDelayTime(float delayTime) {
        oscillatorManager.setDelayTime(delayTime);
    }

    public void setDelayDecay(float decayValue) {
        oscillatorManager.setDelayDecay(decayValue);
    }

    public void setDelayLevel(float levelValue) {
        oscillatorManager.setDelayLevel(levelValue);
    }

    public void setDelayActive() {
        oscillatorManager.setDelayActive();
    }
    //endregion Delay
    //region Reverb (click to open/collapse)
    public void setReverbActive() {
        reverb.setActive();
    }

    public void startRickRoll() {
        sequencer.startRickRoll();
    }

    public void setUpSteps(int i) {
        synthaxView.setUpSteps(i);
    }

    public void setReverbAmount(float amount) {
        reverb.setReverbAmount(amount);
    }

    public void setReverbSize(float size) {
        reverb.setReverbSize(size);
    }

    public void setReverbTone(float tone) {
        reverb.setReverbTone(tone);
    }
    // endregion Reverb
    public void setMasterGain(float gain) {
        masterGainGlide.setValue(gain);
    }

    public boolean connectMidi() {
        return midi.connectMidi();
    }

    public void updateMidiLabel(boolean visable) {
        synthaxView.updateMidiLabel(visable);
    }
}
