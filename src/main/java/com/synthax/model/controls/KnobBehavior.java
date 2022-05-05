package com.synthax.model.controls;

import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
            BigDecimal dec = new BigDecimal(oldValue+0.01);
            BigDecimal dd = dec.setScale(4, RoundingMode.HALF_UP);
            knobValue.setValue(dd.floatValue());
            knob.setRotate(knob.getRotate() + 3);

        } else if (lastMousePos < mousePos && oldValue > knobMinValue){
            BigDecimal dec = new BigDecimal(oldValue-0.01);
            BigDecimal dd = dec.setScale(2, RoundingMode.HALF_UP);
            knobValue.setValue(dd.floatValue());
            knob.setRotate(knob.getRotate() - 3);
        }
        System.out.println(knobValue.getValue());
        lastMousePos = mousePos;
    }

    public FloatProperty knobValueProperty() {
        return knobValue;
    }

    public void setValueRotation(double rotation, float value) {
        knob.setRotate(rotation);
        knobValue.setValue(value);
    }

}
