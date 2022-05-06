package com.synthax.model.controls;

import com.synthax.model.enums.MidiNote;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;

public class KnobBehaviorSeqFreq implements EventHandler<MouseEvent> {
    private Button knob;
    private int knobMaxValue = 108;
    private int knobMinValue = 21;
    private double rotation = 0;
    private double lastMousePos;
    private ArrayList<MidiNote> notes = MidiNote.getArrayList();
    private IntegerProperty noteProperty = new SimpleIntegerProperty(this, "midiNoteIndex", 0);
    private MidiNote midiNote;

    public KnobBehaviorSeqFreq(Button knob, MidiNote midiNote) {
        this.knob = knob;
        this.midiNote = midiNote;
        knob.setRotate(midiNote.getRotation());
        this.noteProperty.setValue(notes.indexOf(midiNote));

    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        double mousePos = mouseEvent.getScreenY();
        if (lastMousePos > mousePos) {
            rotation++;
        } else if (lastMousePos < mousePos) {
            rotation--;
        }
        int oldValue = noteProperty.getValue();
        if (rotation == 2 && oldValue < knobMaxValue) {
            this.midiNote = notes.get(oldValue + 1);
            knob.setRotate(midiNote.getRotation());
            noteProperty.setValue(notes.indexOf(midiNote));
            rotation = 0;

        } else if (rotation == -2 && oldValue > knobMinValue){
            this.midiNote = notes.get(oldValue - 1);
            knob.setRotate(midiNote.getRotation());
            noteProperty.setValue(notes.indexOf(midiNote));
            rotation = 0;
        }
        lastMousePos = mousePos;
    }

    public IntegerProperty knobValueProperty() {
        return noteProperty;
    }

    public void setNote(MidiNote midiNote) {
        this.midiNote = midiNote;
        knob.setRotate(midiNote.getRotation());
        noteProperty.setValue(notes.indexOf(midiNote));
    }

    public String getNoteName() {
        return midiNote.name();
    }

}
