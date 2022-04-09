package com.synthax.SynthaX.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Knob implements EventHandler<MouseEvent> {
    private Button knob;
    //private double rotation = 0;
    private double knobValue = 0;
    private double knobMaxValue = 140;
    private double knobMinValue = 0;
    private double lastMousePos;
    private DoubleProperty rotation = new SimpleDoubleProperty(this,"rotation", 0);

    public Knob(Button b) {
        knob = b;
        knob.setRotate(210);
        rotation.addListener( (v, oldValue, newValue) -> {
            System.out.println(newValue.doubleValue());
        });
    }



    /*@Override
    public void handle(MouseEvent mouseEvent) {
        double a = lastMousePos - mouseEvent.getScreenY();
        if (a == 0) {
            mouseEvent.consume();
        }
        boolean b = lastMousePos - mouseEvent.getScreenY() < 0;
        lastMousePos = mouseEvent.getScreenY();

        if (!b && knobValue != 11) {
            rotation = rotation + 2;
        } else if (b && knobValue != 0) {
            rotation = rotation - 2;
        }

        if (rotation == 16) {
            knob.setRotate(knob.getRotate() + 25);
            knobValue++;
            rotation = 0;
        } else if (rotation == -16) {
            knob.setRotate(knob.getRotate() - 25);
            knobValue--;
            rotation = 0;
        }
    }*/

    /*
    I controllern:
    rotation.rotationProperty().addListener( (v, oldValue, newValue) -> {
        setGain(newValue);
    }

    Eller bind rotation till en annan DoubleProperty som räknar ut korrekta utvärdet och sätt lyssnaren på denna.
    */
    @Override
    public void handle(MouseEvent mouseEvent) {

        double mousePos = mouseEvent.getScreenY();

        if (lastMousePos > mousePos && knobValue < knobMaxValue) {
            rotation.setValue(rotation.getValue() + 2.0);
            knob.setRotate(knob.getRotate() + 2);
            knobValue++;
        } else if (lastMousePos < mousePos && knobValue > knobMinValue){
            rotation.setValue(rotation.getValue() - 2.0);
            knob.setRotate(knob.getRotate() - 2);
            knobValue--;
        }

        /*if (rotation.getValue() == 16) {
            knob.setRotate(knob.getRotate() + 25);
            knobValue++;
            rotation.setValue(0.0);
        } else if (rotation.getValue() == -16) {
            knob.setRotate(knob.getRotate() - 25);
            knobValue--;
            rotation.setValue(0.0);
        }*/
        lastMousePos = mousePos;
        //System.out.println(knobValue);
    }

    public DoubleProperty rotationProperty() {
        return rotation;
    }
}
