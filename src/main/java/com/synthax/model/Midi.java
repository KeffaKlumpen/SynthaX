package com.synthax.model;

import com.synthax.controller.OscillatorManager;
import com.synthax.controller.SynthaxController;
import com.synthax.model.enums.MidiNote;

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
                midiDevice.open();
            } catch (MidiUnavailableException e) {
                System.err.println("unavailable");
            }
        }
        boolean isOpen = midiDevice.isOpen();
        if (isOpen) {
            //new Thread(new MidiConnection()).start();
        }
        return isOpen;
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
            ShortMessage sm = (ShortMessage) msg;
            int data1 = sm.getData1();
            int data2 = sm.getData2();
            int status = msg.getStatus();

            if (status == ShortMessage.NOTE_OFF) {
                oscillatorManager.noteOff(MidiNote.values()[data1]);
            } else if (status == ShortMessage.NOTE_ON) {
                if (data2 > 0) {
                    oscillatorManager.noteOn(MidiNote.values()[data1], data2);
                } else {
                    oscillatorManager.noteOff(MidiNote.values()[data1]);
                }
            } else if (status == ShortMessage.PITCH_BEND) {
                //TODO: anropa en egen metod för pitchbend.
            }
        }

        public void close() {
            //TODO call this on System.exit
            if (midiDevice.isOpen()) {
                midiDevice.close();
            }
        }
    }
    //TODO Try and fix updating the midi label
    private class MidiConnection implements Runnable {
        @Override
        public void run() {
            boolean running = true;
            while (running) {
                System.out.println("Im running");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
