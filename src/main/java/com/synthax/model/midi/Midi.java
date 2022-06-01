package com.synthax.model.midi;

import com.synthax.controller.OscillatorManager;
import com.synthax.controller.SynthaxController;
import com.synthax.model.enums.MidiNote;
import com.synthax.util.HelperMath;

import javax.sound.midi.*;

/**
 * Class that handles the MIDI-device connection
 *
 * @author Teodor Wegestål
 * @author Luke Eales
 * @author Viktor Lenberg
 * @author Joel Eriksson Sinclair
 */
public class Midi {
    private MidiDevice midiDevice;
    private SynthaxController synthaxController;

    /**
     * Searches computer for plugged in MIDI transmitters
     * When found a MIDI-device is handed a MIDI Receiver
     * MIDI device is opened to be able to transmit MIDI signals to the application
     */
    public Midi(SynthaxController synthaxController) {
        this.synthaxController = synthaxController;
    }

    public boolean connectMidi() {
        if (midiDevice != null) {
            midiDevice.close();
        }

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try {
                midiDevice = MidiSystem.getMidiDevice(info);
                Transmitter transmitter = midiDevice.getTransmitter();
                transmitter.setReceiver(new MidiReceiver());
                if (!midiDevice.getDeviceInfo().getName().equalsIgnoreCase("Real Time Sequencer")) {
                    midiDevice.open();
                }
            } catch (MidiUnavailableException e) {
                System.err.println("unavailable");
            }
        }
        boolean isOpen = midiDevice.isOpen();
        if (isOpen) {
            new Thread(new MidiConnection()).start();
        }
        return isOpen;
    }

    public boolean midiConnected() {
        return midiDevice.isOpen();
    }

    /**
     * Class implementing the Receiver interface
     */
    private class MidiReceiver implements Receiver {
        OscillatorManager oscillatorManager = OscillatorManager.getInstance();

        /**
         * Overriding the send method to be able to receive MIDI-messages
         * and extract the relevant data
         *
         * @param msg       the MIDI message to send
         * @param timeStamp the time-stamp for the message, in microseconds
         */

        public void send(MidiMessage msg, long timeStamp) {
            ShortMessage sm;
            try {
                sm = (ShortMessage) msg;
            } catch (ClassCastException e) {
                return;
            }
            int data1 = sm.getData1();
            int data2 = sm.getData2();
            int status = msg.getStatus();

            if (status == ShortMessage.NOTE_OFF) {
                oscillatorManager.noteOff(MidiNote.values()[data1]);
            } else if (status == ShortMessage.NOTE_ON) {
                if (data1 >= 21 && data1 <= 108) {
                    if (data2 > 0) {
                        oscillatorManager.noteOn(MidiNote.values()[data1], data2);
                    } else {
                        oscillatorManager.noteOff(MidiNote.values()[data1]);
                    }
                }
            } else if (status == ShortMessage.PITCH_BEND) {
                //TODO: anropa en egen metod för pitchbend.
                // manager -> osccontroller ->
                float detuneCent = HelperMath.map(data2, 0, 127, -200, 200);
                oscillatorManager.applyPitchBend(detuneCent);
            }
        }

        public void close() {
            //TODO call this on System.exit
            if (midiDevice.isOpen()) {
                midiDevice.close();
            }
        }
    }

    private class MidiConnection implements Runnable {
        @Override
        public void run() {
            boolean running = true;
            while (running) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Solves the issue if someone abruptly disconnects their midikeyboard
                try {
                    MidiSystem.getTransmitter();
                } catch (MidiUnavailableException e) {
                    synthaxController.updateMidiLabel(false);
                    running = false;
                    midiDevice.close();
                }
            }
        }
    }
}
