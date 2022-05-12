package com.synthax.model;

import com.synthax.controller.OscillatorManager;
import com.synthax.model.enums.MidiNote;

import javax.sound.midi.*;
import java.util.List;

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
            if (sm.getStatus() == ShortMessage.NOTE_ON) {
                oscillatorManager.noteOn(MidiNote.values()[sm.getData1()], sm.getData2());
            } else {
                oscillatorManager.noteOff(MidiNote.values()[sm.getData1()]);
            }
        }

        public void close() {
        }
    }

}
