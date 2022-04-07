package com.synthax.SynthaX;



import com.synthax.SynthaX.controls.Knob;
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
    private Synth synth;
    private boolean playin;

    private double rotation = 0;
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
            oscillator.setup();

            synth.addToChain(oscillator);

            oscillatorChainView.getChildren().add(oscillatorView);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void onActionPlay() {
        if (!playin) {
            synth.keyPressed();
        } else {
            synth.keyReleased();
        }
        playin = !playin;
    }

    public void removeOscillator(Oscillator osc) {
        //remove oscillator from synth and from window
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        knob2.getStyleClass().add("knob");
        knob2.setOnMouseDragged(new Knob(knob2));
        //lineChartMain.getStyleClass().add("lineChartMain");
        mainPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.K) {
                synth.keyPressed();
            } else if (event.getCode() == KeyCode.A) {
                if (keyHeld.compareAndSet(false, true)) {
                    synth.setFrequency('C');
                    synth.keyPressed();
                }
            } else if (event.getCode() == KeyCode.S) {
                if (keyHeld.compareAndSet(false, true)) {
                    synth.setFrequency('D');
                    synth.keyPressed();
                }
            } else if (event.getCode() == KeyCode.D) {
                if (keyHeld.compareAndSet(false, true)) {
                    synth.setFrequency('E');
                    synth.keyPressed();
                }
            }
        });
        mainPane.setOnKeyReleased(event -> {
            keyHeld.set(false);
            synth.keyReleased();
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
    }
}
