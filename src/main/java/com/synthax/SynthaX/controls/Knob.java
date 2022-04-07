package com.synthax.SynthaX.controls;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Knob implements EventHandler<MouseEvent> {
    private Button knob;
    private double rotation = 0;
    private int knobValue = 0;
    private double lastMousePos;
    public Knob(Button b) {
        knob = b;
        knob.setRotate(210);
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        boolean b = lastMousePos - mouseEvent.getScreenY() > 0;
        lastMousePos = mouseEvent.getScreenY();

        if (!b && knobValue != 11) {
            rotation = rotation + 2;
        } else if (b && knobValue != 0) {
            rotation = rotation - 2;
        }

        if (rotation == 30) {
            knob.setRotate(knob.getRotate() + 25);
            knobValue++;
            rotation = 0;
        } else if (rotation == -30) {
            knob.setRotate(knob.getRotate() - 25);
            knobValue--;
            rotation = 0;
        }
    }
}
