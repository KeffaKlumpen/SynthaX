package com.synthax.SynthaX.controls;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Knob implements EventHandler<MouseEvent> {
    private Button knob;
    private double rotation = 301;
    private double y;
    public Knob(Button b) {
        knob = b;
        knob.setRotate(301);
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        boolean b = y - mouseEvent.getScreenY() > 0;
        y = mouseEvent.getScreenY();

        if (!b) {

                rotation = (rotation - 2) % 360;

        } else {

                rotation = (rotation + 2) % 360;
        }

        if (rotation >= 300 && rotation < 315) {
            knob.setRotate(301);
            rotation = 301;
        } else if (rotation >= 315 && rotation < 340) {
            knob.setRotate(328);
            rotation = 328;
        } else if (rotation >= 340 || rotation < 8) {
            knob.setRotate(355);
            rotation = 355;
        } else if (rotation >= 8 && rotation < 34) {
            knob.setRotate(21);
            rotation = 21;
        } else if (rotation >= 34 && rotation < 62) {
            knob.setRotate(49);
            rotation = 49;
        } else if (rotation >= 62 && rotation < 88) {
            knob.setRotate(76);
            rotation = 76;
        } else if (rotation >= 88 && rotation < 115) {
            knob.setRotate(103);
            rotation = 103;
        } else if (rotation >= 115 && rotation < 143) {
            knob.setRotate(130);
            rotation = 130;
        } else if (rotation >= 143 && rotation < 160) {
            knob.setRotate(157);
            rotation = 157;
        } else if (rotation >= 160 && rotation < 196) {
            knob.setRotate(183);
            rotation = 183;
        } else if (rotation >= 196 && rotation < 223) {
            knob.setRotate(211);
            rotation = 211;
        } else  {
            knob.setRotate(238);
            rotation = 238;
        }
    }
}
