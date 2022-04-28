package com.synthax.model.controls;

import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Class that represents the behavior of a rotating Waveform knob.
 * Rotates the knob organically in a span of 300 degrees starting at 210 degrees.
 * @author Axel Nilsson
 * @author Luke Eales
 */
public class KnobBehavior implements EventHandler<MouseEvent> {

    private Button knob;
    private float knobMaxValue = 1;
    private float knobMinValue = 0;
    private double lastMousePos;
    private FloatProperty knobValue = new SimpleFloatProperty(this,"rotation", 0);

    public KnobBehavior(Button knob) {
        this.knob = knob;
        knob.setRotate(210);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        double mousePos = mouseEvent.getScreenY();
        Float oldValue = knobValue.getValue();
        if (lastMousePos > mousePos && oldValue < knobMaxValue) {
            knobValue.setValue(oldValue + 0.01);
            knob.setRotate(knob.getRotate() + 3);

        } else if (lastMousePos < mousePos && oldValue > knobMinValue){
            knobValue.setValue(oldValue - 0.01);
            knob.setRotate(knob.getRotate() - 3);
        }
        Float currValue = knobValue.getValue();
        if (currValue < 0.005) {
            knobValue.setValue(0);
        } else if (currValue > 0.995) {
            knobValue.setValue(1);
        }
        lastMousePos = mousePos;
    }

    public FloatProperty knobValueProperty() {
        return knobValue;
    }

    public void setValueZero() {
        knob.setRotate(0);
        knobValue.setValue(0.5);
    }

}
