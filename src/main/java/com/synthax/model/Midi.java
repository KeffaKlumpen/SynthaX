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
    OscillatorManager oscillatorManager = OscillatorManager.getInstance();
    MidiDevice midiDevice;
    MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
    public Midi() {
        for (MidiDevice.Info info : info) {
            try {
                midiDevice = MidiSystem.getMidiDevice(info);
                List<Transmitter> transmitters = midiDevice.getTransmitters();
                for (Transmitter transmitter : transmitters) {
                    transmitter.setReceiver(new MidiReceiver());
                }
                Transmitter trans = midiDevice.getTransmitter();
                trans.setReceiver(new MidiReceiver());
                midiDevice.open();
            } catch (MidiUnavailableException e) {
                System.err.println("unavailable");
            }
        }
    }


    public class MidiReceiver implements Receiver {

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
        }
    }

}
