package com.synthax.SynthaX;



import com.synthax.SynthaX.controls.Knob;

import com.synthax.model.ADSRValues;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.EventHandler;

import com.synthax.SynthaX.oscillator.Oscillator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class SynthaxController implements Initializable {

    @FXML public VBox oscillatorChainView;
    @FXML private Button btnAddOscillator;
    @FXML private Button btnPlay;
    @FXML private AnchorPane mainPane = new AnchorPane();
    @FXML private Button knob = new Button();
    @FXML private Slider sliderAttack;
    @FXML private Slider sliderDecay;
    @FXML private Slider sliderSustain;
    @FXML private Slider sliderRelease;
    @FXML private Slider sliderMasterGain;
    @FXML private LineChart lineChartMain;

    private final Synth synth;

    private double rotation = 0.0;
    private double y = 0.0;

    @FXML private Button knob2 = new Button();



    private final AtomicBoolean keyHeld = new AtomicBoolean(false);


    public SynthaxController() {
        synth = new Synth();
    }

    @FXML
    public void onActionAddOscillator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("oscillator-view.fxml"));
            Node oscillatorView = fxmlLoader.load();
            Oscillator oscillator = fxmlLoader.getController();

            synth.addOscillator(oscillator);
            oscillator.getBtnRemoveOscillator().setOnAction(event -> {
                synth.removeOscillator(oscillator);
                oscillatorChainView.getChildren().remove(oscillatorView);
            });

            oscillator.getBtnMoveDown().setOnAction(event -> {
                synth.moveOscillatorDown(oscillator);
                //TODO: Update GUI to represent the new osc-list order.
            });
            oscillator.getBtnMoveUp().setOnAction(event -> {
                synth.moveOscillatorUp(oscillator);
                //TODO: Update GUI to represent the new osc-list order.
            });

            oscillatorChainView.getChildren().add(oscillatorView);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onActionPlay() {
        System.out.println("use A,S,D keys to play!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        knob2.getStyleClass().add("knob");
        knob2.setOnMouseDragged(new Knob(knob2));
        //lineChartMain.getStyleClass().add("lineChartMain");
        mainPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) { //C4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(261.63f);
                }
            } else if (event.getCode() == KeyCode.W) {  //C#4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(277.18f);
                }
            } else if (event.getCode() == KeyCode.S) {  //D4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(293.66f);
                }
            } else if (event.getCode() == KeyCode.E) {  //D#4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(311.13f);
                }
            } else if (event.getCode() == KeyCode.D) {  //E4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(329.63f);
                }
            } else if (event.getCode() == KeyCode.F) {  //F4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(349.23f);
                }
            } else if (event.getCode() == KeyCode.T) {  //F#4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(369.99f);
                }
            } else if (event.getCode() == KeyCode.G) {  //G4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(392.00f);
                }
            } else if (event.getCode() == KeyCode.Y) {  //G#4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(415.30f);
                }
            } else if (event.getCode() == KeyCode.H) {  //A4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(440.00f);
                }
            } else if (event.getCode() == KeyCode.U) {  //A#4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(466.16f);
                }
            } else if (event.getCode() == KeyCode.J) {  //B4
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(493.88f);
                }
            } else if (event.getCode() == KeyCode.K) {  //C5
                if (keyHeld.compareAndSet(false, true)) {
                    synth.playNote(523.25f);
                }
            }
        });

        mainPane.setOnKeyReleased(event -> {
            keyHeld.set(false);
            //synth.releaseVoice();
            synth.releaseAllVoices();
        });
        knob.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                boolean b = y - mouseEvent.getScreenY() > 0;
                y = mouseEvent.getScreenY();

                if (!b) {
                    if (rotation != 301) {
                        rotation = (rotation - 2) % 360;
                    }
                } else {
                    if (rotation != 238)
                        rotation = (rotation + 2) % 360;
                }

                if (rotation >= 300 && rotation < 315) {
                    knob.setRotate(301);
                    rotation = 301;
                } else if (rotation >= 315 && rotation < 340) {
                    knob.setRotate(328);
                } else if (rotation >= 340 || rotation < 8) {
                    knob.setRotate(355);
                } else if (rotation >= 8 && rotation < 34) {
                    knob.setRotate(21);
                } else if (rotation >= 34 && rotation < 62) {
                    knob.setRotate(49);
                } else if (rotation >= 62 && rotation < 88) {
                    knob.setRotate(76);
                } else if (rotation >= 88 && rotation < 115) {
                    knob.setRotate(103);
                } else if (rotation >= 115 && rotation < 143) {
                    knob.setRotate(130);
                } else if (rotation >= 143 && rotation < 160) {
                    knob.setRotate(157);
                } else if (rotation >= 160 && rotation < 196) {
                    knob.setRotate(183);
                } else if (rotation >= 196 && rotation < 223) {
                    knob.setRotate(211);
                } else  {
                    knob.setRotate(238);
                }
            }
        });

        sliderAttack.setMax(9000);
        sliderAttack.setMin(10);
        sliderAttack.setBlockIncrement(50);
        sliderAttack.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ADSRValues.setAttackValue(t1.floatValue());
            }
        });

        sliderDecay.setMax(9000);
        sliderDecay.setMin(10);
        sliderDecay.setBlockIncrement(50);
        sliderDecay.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ADSRValues.setDecayValue(t1.floatValue());
            }
        });

        sliderSustain.setMax(1);
        sliderSustain.setValue(1);
        sliderSustain.setBlockIncrement(0.1);
        sliderSustain.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ADSRValues.setSustainValue(t1.floatValue());
            }
        });

        sliderRelease.setMax(9000);
        sliderRelease.setMin(10);
        sliderRelease.setBlockIncrement(50);
        sliderRelease.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ADSRValues.setReleaseValue(t1.floatValue());
            }
        });

        sliderMasterGain.setMax(1);
        sliderMasterGain.setValue(0.5);
        sliderMasterGain.setBlockIncrement(0.1);
        sliderMasterGain.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                synth.setMasterGain(t1.floatValue());
            }
        });
    }
}
