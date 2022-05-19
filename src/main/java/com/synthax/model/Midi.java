package com.synthax.model;

import com.synthax.controller.OscillatorManager;
import com.synthax.model.enums.MidiNote;

import javax.sound.midi.*;
import java.util.List;

/**
 * Class that handles the MIDI-device connection
 * @author Teodor Wegestål
 * @author Luke Eales
 * @author Viktor Lenberg
 * @author Joel Eriksson Sinclair
 */
public class Midi {
    private MidiDevice midiDevice;

    /**
     * Searches computer for plugged in MIDI transmitters
     * When found a MIDI-device is handed a MIDI Receiver
     * MIDI device is opened to be able to transmit MIDI signals to the application
     */
    public Midi() {
        connectMidi();
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
        return midiDevice.isOpen();
    }

    /**
     * Class implementing the Receiver interface
     */
    public class MidiReceiver implements Receiver {
        OscillatorManager oscillatorManager = OscillatorManager.getInstance();
        /**
         * Overriding the send method to be able to receive MIDI-messages
         * and extract the relevant data
         * @param msg the MIDI message to send
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
            System.out.println("shutup");
            //TODO call this on System.exit
                if (midiDevice.isOpen()) {
                    midiDevice.close();
                }
        }
    }
}
