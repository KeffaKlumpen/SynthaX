package com.synthax.SynthaX;

import com.synthax.SynthaX.controls.KnobBehavior;

import com.synthax.SynthaX.controls.KnobBehaviorWave;
import com.synthax.model.ADSRValues;
import com.synthax.model.MidiNote;
import com.synthax.util.MidiHelpers;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
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
    @FXML private Button knob2 = new Button();
    private final Synth synth;
    private double rotation = 0.0;
    private double y = 0.0;


    //new gui components
    @FXML private Button knobNoiseGain;
    @FXML private Button knobDelayFeedback;
    @FXML private Button knobDelayTime;
    @FXML private Button knobDelayRepeat;
    @FXML private Button knobReverbSize;
    @FXML private Button knobReverbDecay;
    @FXML private Button knobReverbDW;
    @FXML private Button knobLFODepth;
    @FXML private Button knobLFORate;
    @FXML private Button knobLFOWaveForm;
    @FXML private Button knobFilterCutoff;
    @FXML private Button knobFilterResonance;
    @FXML private Button knobFilterEnvelope;


    private final Map<KeyCode, AtomicBoolean> keyStatus = Map.of(KeyCode.A, new AtomicBoolean(false),
            KeyCode.S, new AtomicBoolean(false),
            KeyCode.D, new AtomicBoolean(false),
            KeyCode.F, new AtomicBoolean(false),
            KeyCode.G, new AtomicBoolean(false));

    public SynthaxController() {
        synth = new Synth();
    }

    @FXML
    public void onActionAddOscillator() {
        try {
            URL fxmlLocation = MainApplication.class.getResource("oscillator/Oscillator-v.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Node oscillatorView = fxmlLoader.load();
            Oscillator oscillator = fxmlLoader.getController();

            synth.addOscillator(oscillator);

            oscillator.getBtnRemoveOscillator().setOnAction(event -> {
                synth.removeOscillator(oscillator);
                oscillatorChainView.getChildren().remove(oscillatorView);
            });
            oscillator.getBtnMoveDown().setOnAction(event -> {
                Node[] childList = oscillatorChainView.getChildren().toArray(new Node[0]);
                int oscIndex = oscillatorChainView.getChildren().indexOf(oscillatorView);

                if(oscIndex < childList.length - 1){
                    Node nextOsc = childList[oscIndex + 1];
                    childList[oscIndex + 1] = oscillatorView;
                    childList[oscIndex] = nextOsc;

                    oscillatorChainView.getChildren().setAll(childList);

                    synth.moveOscillatorDown(oscillator);
                }
            });
            oscillator.getBtnMoveUp().setOnAction(event -> {
                Node[] childList = oscillatorChainView.getChildren().toArray(new Node[0]);
                int oscIndex = oscillatorChainView.getChildren().indexOf(oscillatorView);

                if(oscIndex > 0){
                    Node prevOsc = childList[oscIndex - 1];
                    childList[oscIndex - 1] = oscillatorView;
                    childList[oscIndex] = prevOsc;

                    oscillatorChainView.getChildren().setAll(childList);

                    synth.moveOscillatorUp(oscillator);
                }
            });

            oscillatorChainView.getChildren().add(oscillatorView);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onActionPlay() {
        System.out.println("use A,S,D,F,G keys to play!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        KnobBehavior bKnobNoiseGain = new KnobBehavior(knobNoiseGain);
        knobNoiseGain.setOnMouseDragged(bKnobNoiseGain);
        bKnobNoiseGain.setValueZero();
        bKnobNoiseGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here.
        });

        KnobBehavior bKnobDelayFeedback = new KnobBehavior(knobDelayFeedback);
        knobDelayFeedback.setOnMouseDragged(bKnobDelayFeedback);
        bKnobDelayFeedback.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobDelayTime = new KnobBehavior(knobDelayTime);
        knobDelayTime.setOnMouseDragged(bKnobDelayTime);
        bKnobDelayTime.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobDelayRepeat = new KnobBehavior(knobDelayRepeat);
        knobDelayRepeat.setOnMouseDragged(bKnobDelayRepeat);
        bKnobDelayRepeat.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobReverbSize = new KnobBehavior(knobReverbSize);
        knobReverbSize.setOnMouseDragged(bKnobReverbSize);
        bKnobReverbSize.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobReverbDecay = new KnobBehavior(knobReverbDecay);
        knobReverbDecay.setOnMouseDragged(bKnobReverbDecay);
        bKnobReverbDecay.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobReverbDW = new KnobBehavior(knobReverbDW);
        knobReverbDW.setOnMouseDragged(bKnobReverbDW);
        bKnobReverbDW.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobLFODepth = new KnobBehavior(knobLFODepth);
        knobLFODepth.setOnMouseDragged(bKnobLFODepth);
        bKnobLFODepth.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobLFORate = new KnobBehavior(knobLFORate);
        knobLFORate.setOnMouseDragged(bKnobLFORate);
        bKnobLFORate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehaviorWave bKnobLFOWaveform = new KnobBehaviorWave(knobLFOWaveForm);
        knobLFOWaveForm.setOnMouseDragged(bKnobLFOWaveform);
        bKnobLFOWaveform.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobFilterCutoff = new KnobBehavior(knobFilterCutoff);
        knobFilterCutoff.setOnMouseDragged(bKnobFilterCutoff);
        bKnobFilterCutoff.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobFilterResonance = new KnobBehavior(knobFilterResonance);
        knobFilterResonance.setOnMouseDragged(bKnobFilterResonance);
        bKnobFilterResonance.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobFilterEnvelope = new KnobBehavior(knobFilterEnvelope);
        knobFilterEnvelope.setOnMouseDragged(bKnobFilterEnvelope);
        bKnobFilterEnvelope.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });




        //region KeyBoard playing
        mainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                // If it's a valid key, send a noteOn message.
                if(keyStatus.containsKey(keyCode)){
                    if(keyStatus.get(keyCode).compareAndSet(false, true)){
                        MidiNote note = MidiHelpers.keyCodeToMidi(keyCode);
                        System.out.println("++++" + note.name());
                        synth.noteOn(note, 128);
                    }
                }
            }
        });
        mainPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if(keyStatus.containsKey(keyCode)){
                    if(keyStatus.get(keyCode).compareAndSet(true, false)){
                        MidiNote note = MidiHelpers.keyCodeToMidi(keyCode);
                        System.out.println("----" + note.name());
                        synth.noteOff(note);
                    }
                }
            }
        });
        //endregion

        //region KnobDragging
        knob2.getStyleClass().add("knob");
        KnobBehavior knob2list = new KnobBehavior(knob2);
        knob2.setOnMouseDragged(knob2list);
        /*knob2list.knobStringValueProperty().addListener( (v, oldValue, newValue) -> {
            System.out.println(newValue);

        });*/



        //lineChartMain.getStyleClass().add("lineChartMain");

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
        //endregion

        //region ADSR sliders
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
        //endregion

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
