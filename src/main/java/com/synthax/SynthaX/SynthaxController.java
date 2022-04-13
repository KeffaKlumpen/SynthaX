package com.synthax.SynthaX;



import com.synthax.SynthaX.controls.Knob;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.synthax.SynthaX.oscillator.Oscillator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
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
    @FXML private Label lblKnob = new Label();
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
        Knob knob2list = new Knob(knob2);
        knob2.setOnMouseDragged(knob2list);
        lblKnob.textProperty().bind(knob2list.knobStringValueProperty());
        /*knob2list.knobStringValueProperty().addListener( (v, oldValue, newValue) -> {
            System.out.println(newValue);
            lblKnob.setText(newValue);
        });*/



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

        sliderAttack.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                synth.getADSR().setAttackValue(t1.floatValue());
            }
        });

        sliderDecay.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                synth.getADSR().setDecayValue(t1.floatValue());
            }
        });

        sliderSustain.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                synth.getADSR().setSustainValue(t1.floatValue());
            }
        });

        sliderRelease.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                synth.getADSR().setReleaseValue(t1.floatValue());
            }
        });

        sliderMasterGain.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                synth.getADSR().setPeakGain(t1.floatValue());
            }
        });
    }
}
