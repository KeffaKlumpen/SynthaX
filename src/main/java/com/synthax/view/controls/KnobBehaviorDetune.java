package com.synthax.view.controls;

import com.synthax.util.HelperMath;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Class that represents the behavior of a rotating Waveform knob.
 * Rotates the knob organically starting at 0 degrees with the possibility of being rotated
 * 150 degrees in both ways.
 * @author Axel Nilsson
 * @author Luke Eales
 */
public class KnobBehaviorDetune implements EventHandler<MouseEvent> {
    private Button knob;
    private float knobMaxValue = 50;
    private float knobMinValue = -50;
    private double lastMousePos;
    private int rotation;
    private FloatProperty knobValue = new SimpleFloatProperty(this,"rotation", 0);

    public KnobBehaviorDetune(Button knob) {
        this.knob = knob;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        double mousePos = mouseEvent.getScreenY();
        float value = knobValue.getValue();
        if (lastMousePos > mousePos) {
            rotation++;
        } else if (lastMousePos < mousePos) {
            rotation--;
        }
        if ((rotation > 10 && value < knobMaxValue) && value == 0) {
            knobValue.setValue(knobValue.getValue() + 1);
            knob.setRotate(knob.getRotate() + 3);
            rotation = 0;
        } else if ((rotation < -10 && value > knobMinValue) && value == 0) {
            knobValue.setValue(knobValue.getValue() - 1);
            knob.setRotate(knob.getRotate() - 3);
            rotation = 0;
        } else if ((rotation == 1 && value < knobMaxValue) && value != 0) {
            knobValue.setValue(knobValue.getValue() + 1);
            knob.setRotate(knob.getRotate() + 3);
            rotation = 0;
        } else if ((rotation == -1 && value > knobMinValue) && value != 0) {
            knobValue.setValue(knobValue.getValue() - 1);
            knob.setRotate(knob.getRotate() - 3);
            rotation = 0;
        }
        lastMousePos = mousePos;
    }

    public FloatProperty knobValueProperty() {
        return knobValue;
    }

    public void setRotation(float value) {
        knobValue.setValue(value);
        knob.setRotate(HelperMath.map((value+50), 0, 100, 210, 510));
        rotation = 0;
    }

    public void resetKnob() {
        knobValue.setValue(0);
        knob.setRotate(0);
        rotation = 0;
    }
}
