package com.synthax.SynthaX.controls;

import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Knob implements EventHandler<MouseEvent> {

    private Button knob;
    private float knobMaxValue = 100;
    private float knobMinValue = 0;
    private double lastMousePos;
    private FloatProperty knobValue = new SimpleFloatProperty(this,"rotation", 0);

    public Knob(Button b) {
        knob = b;
        knob.setRotate(210);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        double mousePos = mouseEvent.getScreenY();

        if (lastMousePos > mousePos && knobValue.getValue() < knobMaxValue) {
            knobValue.setValue(knobValue.getValue() + 1.0);
            knob.setRotate(knob.getRotate() + 3);

        } else if (lastMousePos < mousePos && knobValue.getValue() > knobMinValue){
            knobValue.setValue(knobValue.getValue() - 1.0);
            knob.setRotate(knob.getRotate() - 3);
        }

        lastMousePos = mousePos;
    }

    public FloatProperty knobValueProperty() {
        return knobValue;
    }

}
