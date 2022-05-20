package com.synthax.view.controls;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Class that represents the behavior of a rotating Waveform knob.
 * Rotates the knob 90 degrees which result in a feeling of "snapping" to the desired Waveform.
 * @author Axel Nilsson
 * @author Luke Eales
 */
public class KnobBehaviorWave implements EventHandler<MouseEvent> {

    private Button knob;
    private float knobMaxValue = 3;
    private float knobMinValue = 0;
    private double lastMousePos;
    private double snapValue;
    private IntegerProperty knobValue = new SimpleIntegerProperty(this,"rotation", 0);

    public KnobBehaviorWave(Button knob) {
        this.knob = knob;
        knob.setRotate(225);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        double mousePos = mouseEvent.getScreenY();

        if (lastMousePos > mousePos) {
            snapValue++;
        } else if (lastMousePos < mousePos) {
            snapValue--;
        }
        if (snapValue == 30) {
            knob.setRotate(knob.getRotate()+90);
            knobValue.setValue((knobValue.getValue() + 1) % 4);
            snapValue = 0;
        } else if (snapValue == -30) {
            knob.setRotate(knob.getRotate()-90);
            int currKnobValue = knobValue.getValue();
            currKnobValue = currKnobValue - 1 < 0 ? 3 : currKnobValue - 1;
            knobValue.setValue(currKnobValue);
            snapValue = 0;
        }

        lastMousePos = mousePos;
    }

    public IntegerProperty knobValueProperty() {
        return knobValue;
    }
}
