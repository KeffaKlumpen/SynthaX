package com.synthax.controller;

import com.synthax.model.effects.SynthaxADSR;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.oscillator.Voice;
import com.synthax.model.oscillator.VoiceNormalizer;
import com.synthax.util.HelperMath;
import com.synthax.util.MidiHelpers;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class VoiceController {
    public static final int MAX_OSC_VOICE_COUNT = 16;
    public static int VOICE_COUNT = 8;
    public static boolean MONOPHONIC_STATUS = false;

    protected int voiceCount;
    protected Voice[] voices;
    protected final Gain voiceOutput;
    protected final Glide voiceOutputGlide;

    protected boolean[] voiceAvailability;
    private final HashMap<Integer, NotifierThread> notifierThreadHashMap = new HashMap<>();
    private final ArrayList<Integer> voiceHistory = new ArrayList<>();
    private final int[] voicePlayingMidi = new int[MidiHelpers.MIDI_NOTE_COUNT]; //stores voice-index playing note

    public VoiceController(int voiceCount) {
        AudioContext ac = AudioContext.getDefaultContext();
        voiceOutputGlide = new Glide(ac, 0.5f, 50);
        voiceOutput = new Gain(ac,1, voiceOutputGlide);

        setVoiceCount(voiceCount);
    }

    public void setVoiceCount(int newVoiceCount) {
        if(newVoiceCount < 1) {
            newVoiceCount = 1;
        }

        // clear connections
        voiceOutput.clearDependents();
        voiceOutput.clearInputConnections();

        // stop old stuff
        if(voices != null) {
            for (int i = 0; i < voiceCount; i++) {
                voices[i].stopPlay();
            }
        }
        voiceHistory.clear();
        for (NotifierThread notifierThread : notifierThreadHashMap.values()) {
            notifierThread.cancelNotification();
        }
        notifierThreadHashMap.clear();

        // set new variables
        voiceCount = newVoiceCount;

        VoiceNormalizer voiceNormalizer = new VoiceNormalizer(voiceCount);
        voiceOutput.addDependent(voiceNormalizer);

        voices = new Voice[voiceCount];
        voiceAvailability = new boolean[voiceCount];
        for (int i = 0; i < voiceCount; i++) {
            Voice voice = createVoice(i);

            voiceNormalizer.setInGain(voice.getNaturalGain(), i);
            voiceNormalizer.setOutGain(voice.getNormalizedGainGlide(), i);

            voiceOutput.addInput(voice.getOutput());
            voices[i] = voice;
            voiceAvailability[i] = true;
        }
    }

    protected abstract Voice createVoice(int i);

    //region MIDI-handling
    public void noteOn(MidiNote midiNote, int velocity) {
        noteOn(midiNote, velocity, 0f);
    }

    /**
     * Handle MIDI,
     * @param midiNote Note to be played
     * @param velocity Velocity of the note to be played
     * @param detuneCent How many cents of detuning to be applied.
     */
    public void noteOn(MidiNote midiNote, int velocity, float detuneCent) {
        if(velocity < 0 || velocity > MidiHelpers.MAX_VELOCITY_VALUE) {
            velocity = HelperMath.clamp(velocity, 0, MidiHelpers.MAX_VELOCITY_VALUE);
        }

        // Try to get availableVoice, else get oldestVoice.
        int voiceIndex = getBestVoice();

        voicePlayingMidi[midiNote.ordinal()] = voiceIndex;
        setVoiceUnavailable(voiceIndex);

        float detunedFrequency = getDetunedFrequency(midiNote.getFrequency(), detuneCent);

        float maxGain = velocity / (float) MidiHelpers.MAX_VELOCITY_VALUE;
        float sustainGain = maxGain * SynthaxADSR.getSustainValue();
        voices[voiceIndex].noteOn(midiNote, detunedFrequency, maxGain, SynthaxADSR.getAttackValue(), sustainGain, SynthaxADSR.getDecayValue());
    }

    protected float getDetunedFrequency(float baseFreq, float detuneCent) {
        return baseFreq;
    }

    public void noteOff(MidiNote midiNote) {
        int voiceIndex = voicePlayingMidi[midiNote.ordinal()];
        if(voiceIndex < voiceCount) {
            voices[voiceIndex].noteOff(midiNote, SynthaxADSR.getReleaseValue());
        }
        else {
            System.err.println("noteOff on a voice that does not exist anymore.");
        }
    }
    //endregion MIDI-handling

    //region Voice Availability
    public synchronized void notifyAvailableVoice(int voiceIndex, float delay) {
        NotifierThread notifierThread = new NotifierThread(this, voiceIndex, delay);
        if(!notifierThreadHashMap.containsKey(voiceIndex)) {
            notifierThreadHashMap.put(voiceIndex, notifierThread);
            notifierThread.start();
        }
    }

    private synchronized void setVoiceUnavailable(int voiceIndex) {
        voiceAvailability[voiceIndex] = false;
        voiceHistory.add(voiceIndex);
    }

    public synchronized void setVoiceAvailable(int voiceIndex) {
        voiceAvailability[voiceIndex] = true;
        voiceHistory.remove((Object)voiceIndex);
        NotifierThread notifierThread = notifierThreadHashMap.remove(voiceIndex);
        if(notifierThread != null) {
            notifierThread.cancelNotification();
        }
    }

    protected synchronized int getBestVoice() {
        int voiceToSteal = -1;

        for (int i = 0; i < voiceCount; i++) {
            if(voiceAvailability[i]) {
                voiceToSteal = i;
                break;
            }
        }

        // no available, steal the oldest!
        if(voiceToSteal < 0) {
            voiceToSteal = voiceHistory.remove(0);
        }

        return voiceToSteal;
    }
    //endregion Voice Availability

    //region getters & setters
    public abstract void setGain(float gain);
    public abstract void setActive(boolean active);
    public abstract UGen getOutput();
    //endregion getters & setters
}
