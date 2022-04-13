package com.synthax.SynthaX.controls;

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

        if (lastMousePos > mousePos && knobValue.getValue() < knobMaxValue) {
            knobValue.setValue(knobValue.getValue() + 0.01);
            knob.setRotate(knob.getRotate() + 3);

        } else if (lastMousePos < mousePos && knobValue.getValue() > knobMinValue){
            knobValue.setValue(knobValue.getValue() - 0.01);
            knob.setRotate(knob.getRotate() - 3);
        }

        lastMousePos = mousePos;
    }

    public FloatProperty knobValueProperty() {
        return knobValue;
    }

}
