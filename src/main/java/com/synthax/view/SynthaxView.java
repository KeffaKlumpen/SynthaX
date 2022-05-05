package com.synthax.view;

import com.synthax.model.controls.KnobBehavior;

import com.synthax.model.controls.KnobBehaviorDetune;
import com.synthax.model.controls.KnobBehaviorWave;
import com.synthax.MainApplication;
import com.synthax.controller.OscillatorController;
import com.synthax.controller.SynthaxController;
import com.synthax.model.ADSRValues;
import com.synthax.model.controls.KnobBehaviorSeqFreq;
import com.synthax.model.enums.MidiNote;
import com.synthax.util.MidiHelpers;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for synthesizer main view
 *
 * @author Axel Nilsson
 * @author Luke Eales
 */
public class SynthaxView implements Initializable {
    @FXML private VBox oscillatorChainView;
    @FXML private Button knobNoiseGain;
    @FXML private ToggleSwitch tglSwitchNoise;
    @FXML private Button knobDelayFeedback;
    @FXML private Button knobDelayTime;
    @FXML private Button knobDelayLevel;
    @FXML private ToggleSwitch tglSwitchDelay;
    @FXML private Button knobReverbSize;
    @FXML private Button knobReverbDecay;
    @FXML private Button knobReverbDW;
    @FXML private ToggleSwitch tglSwitchReverb;
    @FXML private Button knobLFODepth;
    @FXML private Button knobLFORate;
    @FXML private Button knobLFOWaveForm;
    @FXML private ToggleSwitch tglSwitchLFO;
    @FXML private Button knobFilterRange;
    @FXML private Button knobFilterFreq;
    @FXML private Button knobFilterHPCutoff;
    @FXML private Button knobFilterHPSlope;
    @FXML private Button knobFilterLPCutoff;
    @FXML private Button knobFilterLPSlope;
    @FXML private ToggleSwitch tglSwitchFilterLP;
    @FXML private ToggleSwitch tglSwitchFilterHP;
    @FXML private ToggleSwitch tglSwitchFilterNotch;
    @FXML private Button btnAddOscillator;
    @FXML private AnchorPane mainPane = new AnchorPane();
    @FXML private Slider sliderAttack;
    @FXML private Slider sliderDecay;
    @FXML private Slider sliderSustain;
    @FXML private Slider sliderRelease;
    @FXML private Slider sliderMasterGain;
    @FXML private NumberAxis xAxis = new NumberAxis();
    @FXML private NumberAxis yAxis = new NumberAxis();
    @FXML private LineChart<Number, Number> lineChartADSR = new LineChart<Number, Number>(xAxis, yAxis);
    //region Step sequencer buttons
    @FXML private Button knobSS0freq;
    @FXML private Button knobSS0FineTune;
    @FXML private Button knobSS0Gain;
    @FXML private Button knobSS1freq;
    @FXML private Button knobSS1FineTune;
    @FXML private Button knobSS1Gain;
    @FXML private Button knobSS2freq;
    @FXML private Button knobSS2FineTune;
    @FXML private Button knobSS2Gain;
    @FXML private Button knobSS3freq;
    @FXML private Button knobSS3FineTune;
    @FXML private Button knobSS3Gain;
    @FXML private Button knobSS4freq;
    @FXML private Button knobSS4FineTune;
    @FXML private Button knobSS4Gain;
    @FXML private Button knobSS5freq;
    @FXML private Button knobSS5FineTune;
    @FXML private Button knobSS5Gain;
    @FXML private Button knobSS6freq;
    @FXML private Button knobSS6FineTune;
    @FXML private Button knobSS6Gain;
    @FXML private Button knobSS7freq;
    @FXML private Button knobSS7FineTune;
    @FXML private Button knobSS7Gain;
    @FXML private Button knobSS8freq;
    @FXML private Button knobSS8FineTune;
    @FXML private Button knobSS8Gain;
    @FXML private Button knobSS9freq;
    @FXML private Button knobSS9FineTune;
    @FXML private Button knobSS9Gain;
    @FXML private Button knobSS10freq;
    @FXML private Button knobSS10FineTune;
    @FXML private Button knobSS10Gain;
    @FXML private Button knobSS11freq;
    @FXML private Button knobSS11FineTune;
    @FXML private Button knobSS11Gain;
    @FXML private Button knobSS12freq;
    @FXML private Button knobSS12FineTune;
    @FXML private Button knobSS12Gain;
    @FXML private Button knobSS13freq;
    @FXML private Button knobSS13FineTune;
    @FXML private Button knobSS13Gain;
    @FXML private Button knobSS14freq;
    @FXML private Button knobSS14FineTune;
    @FXML private Button knobSS14Gain;
    @FXML private Button knobSS15freq;
    @FXML private Button knobSS15FineTune;
    @FXML private Button knobSS15Gain;
    @FXML private Button SSStartStop;
    @FXML private Button knobSSRate;
    @FXML private ToggleButton btnStepOnOff0;
    @FXML private ToggleButton btnStepOnOff1;
    @FXML private ToggleButton btnStepOnOff2;
    @FXML private ToggleButton btnStepOnOff3;
    @FXML private ToggleButton btnStepOnOff4;
    @FXML private ToggleButton btnStepOnOff5;
    @FXML private ToggleButton btnStepOnOff6;
    @FXML private ToggleButton btnStepOnOff7;
    @FXML private ToggleButton btnStepOnOff8;
    @FXML private ToggleButton btnStepOnOff9;
    @FXML private ToggleButton btnStepOnOff10;
    @FXML private ToggleButton btnStepOnOff11;
    @FXML private ToggleButton btnStepOnOff12;
    @FXML private ToggleButton btnStepOnOff13;
    @FXML private ToggleButton btnStepOnOff14;
    @FXML private ToggleButton btnStepOnOff15;
    //endregion

    private Button[] sequencerFreqKnobs;
    private Button[] sequencerDetuneKnobs;
    private Button[] sequencerGainKnobs;
    private ToggleButton[] sequencerSteps;
    private XYChart.Data<Number, Number> point1ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point2ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point3ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point4ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point5ADSR = new XYChart.Data<>();

    private final SynthaxController synthaxController;
    private final int attackMax = 3000;
    private final int decayMax = 1500;
    private final int releaseMax = 2000;

    private final HashMap<KeyCode, AtomicBoolean> keyStatus = new HashMap<>();

    public SynthaxView() {
        synthaxController = new SynthaxController(this);
    }

    @FXML
    public void onActionAddOscillator() {
        try {
            URL fxmlLocation = MainApplication.class.getResource("view/Oscillator-v.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Node oscillatorView = fxmlLoader.load();
            OscillatorController oscillatorController = fxmlLoader.getController();

            synthaxController.addOscillator(oscillatorController);

            oscillatorController.getBtnRemoveOscillator().setOnAction(event -> {
                synthaxController.removeOscillator(oscillatorController);
                oscillatorChainView.getChildren().remove(oscillatorView);
            });
            oscillatorController.getBtnMoveDown().setOnAction(event -> {
                Node[] childList = oscillatorChainView.getChildren().toArray(new Node[0]);
                int oscIndex = oscillatorChainView.getChildren().indexOf(oscillatorView);

                if (oscIndex < childList.length - 1) {
                    Node nextOsc = childList[oscIndex + 1];
                    childList[oscIndex + 1] = oscillatorView;
                    childList[oscIndex] = nextOsc;

                    oscillatorChainView.getChildren().setAll(childList);

                    synthaxController.moveOscillatorDown(oscillatorController);
                }
            });
            oscillatorController.getBtnMoveUp().setOnAction(event -> {
                Node[] childList = oscillatorChainView.getChildren().toArray(new Node[0]);
                int oscIndex = oscillatorChainView.getChildren().indexOf(oscillatorView);

                if (oscIndex > 0) {
                    Node prevOsc = childList[oscIndex - 1];
                    childList[oscIndex - 1] = oscillatorView;
                    childList[oscIndex] = prevOsc;

                    oscillatorChainView.getChildren().setAll(childList);

                    synthaxController.moveOscillatorUp(oscillatorController);
                }
            });

            oscillatorChainView.getChildren().add(oscillatorView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSeqButtonOrange(int i) {
        Platform.runLater(() -> {
            switch (i) {
                case 0 -> btnStepOnOff0.setStyle("-fx-background-color: #f78000");
                case 1 -> btnStepOnOff1.setStyle("-fx-background-color: #f78000");
                case 2 -> btnStepOnOff2.setStyle("-fx-background-color: #f78000");
                case 3 -> btnStepOnOff3.setStyle("-fx-background-color: #f78000");
                case 4 -> btnStepOnOff4.setStyle("-fx-background-color: #f78000");
                case 5 -> btnStepOnOff5.setStyle("-fx-background-color: #f78000");
                case 6 -> btnStepOnOff6.setStyle("-fx-background-color: #f78000");
                case 7 -> btnStepOnOff7.setStyle("-fx-background-color: #f78000");
                case 8 -> btnStepOnOff8.setStyle("-fx-background-color: #f78000");
                case 9 -> btnStepOnOff9.setStyle("-fx-background-color: #f78000");
                case 10 -> btnStepOnOff10.setStyle("-fx-background-color: #f78000");
                case 11 -> btnStepOnOff11.setStyle("-fx-background-color: #f78000");
                case 12 -> btnStepOnOff12.setStyle("-fx-background-color: #f78000");
                case 13 -> btnStepOnOff13.setStyle("-fx-background-color: #f78000");
                case 14 -> btnStepOnOff14.setStyle("-fx-background-color: #f78000");
                case 15 -> btnStepOnOff15.setStyle("-fx-background-color: #f78000");
            }
        });
    }

    public void setSeqButtonGray(int i) {
        Platform.runLater(()-> {
            switch (i) {
                case 0 -> btnStepOnOff0.setStyle("-fx-background-color: #78736b");
                case 1 -> btnStepOnOff1.setStyle("-fx-background-color: #78736b");
                case 2 -> btnStepOnOff2.setStyle("-fx-background-color: #78736b");
                case 3 -> btnStepOnOff3.setStyle("-fx-background-color: #78736b");
                case 4 -> btnStepOnOff4.setStyle("-fx-background-color: #78736b");
                case 5 -> btnStepOnOff5.setStyle("-fx-background-color: #78736b");
                case 6 -> btnStepOnOff6.setStyle("-fx-background-color: #78736b");
                case 7 -> btnStepOnOff7.setStyle("-fx-background-color: #78736b");
                case 8 -> btnStepOnOff8.setStyle("-fx-background-color: #78736b");
                case 9 -> btnStepOnOff9.setStyle("-fx-background-color: #78736b");
                case 10 -> btnStepOnOff10.setStyle("-fx-background-color: #78736b");
                case 11 -> btnStepOnOff11.setStyle("-fx-background-color: #78736b");
                case 12 -> btnStepOnOff12.setStyle("-fx-background-color: #78736b");
                case 13 -> btnStepOnOff13.setStyle("-fx-background-color: #78736b");
                case 14 -> btnStepOnOff14.setStyle("-fx-background-color: #78736b");
                case 15 -> btnStepOnOff15.setStyle("-fx-background-color: #78736b");
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initButtonArrays();
        initKeyStatus();
        initNoise();
        initADSR();
        initFilter();
        initKeyBoardListeners();
        initDelay();
        initLFO();
        initReverb();
        initSS();
        sliderMasterGain.valueProperty().addListener((observableValue, number, t1) -> synthaxController.setMasterGain(t1.floatValue()));
    }

    private void initButtonArrays() {
        sequencerSteps = new ToggleButton[] {
                btnStepOnOff0,
                btnStepOnOff1,
                btnStepOnOff2,
                btnStepOnOff3,
                btnStepOnOff4,
                btnStepOnOff5,
                btnStepOnOff6,
                btnStepOnOff7,
                btnStepOnOff8,
                btnStepOnOff9,
                btnStepOnOff10,
                btnStepOnOff11,
                btnStepOnOff12,
                btnStepOnOff13,
                btnStepOnOff14,
                btnStepOnOff15};
        sequencerFreqKnobs = new Button[] {
                knobSS0freq,
                knobSS1freq,
                knobSS2freq,
                knobSS3freq,
                knobSS4freq,
                knobSS5freq,
                knobSS6freq,
                knobSS7freq,
                knobSS8freq,
                knobSS9freq,
                knobSS10freq,
                knobSS11freq,
                knobSS12freq,
                knobSS13freq,
                knobSS14freq,
                knobSS15freq};
        sequencerDetuneKnobs = new Button[] {
                knobSS0FineTune,
                knobSS1FineTune,
                knobSS2FineTune,
                knobSS3FineTune,
                knobSS4FineTune,
                knobSS5FineTune,
                knobSS6FineTune,
                knobSS7FineTune,
                knobSS8FineTune,
                knobSS9FineTune,
                knobSS10FineTune,
                knobSS11FineTune,
                knobSS12FineTune,
                knobSS13FineTune,
                knobSS14FineTune,
                knobSS15FineTune};
        sequencerGainKnobs = new Button[] {
                knobSS0Gain,
                knobSS1Gain,
                knobSS2Gain,
                knobSS3Gain,
                knobSS4Gain,
                knobSS5Gain,
                knobSS6Gain,
                knobSS7Gain,
                knobSS8Gain,
                knobSS9Gain,
                knobSS10Gain,
                knobSS11Gain,
                knobSS12Gain,
                knobSS13Gain,
                knobSS14Gain,
                knobSS15Gain};
    }

    private void setupLineChart() {
        XYChart.Series<Number, Number> seriesADSR = new XYChart.Series<>();

        point1ADSR.setXValue(0d);
        point1ADSR.setYValue(0d);
        point2ADSR.setXValue(0d);
        point2ADSR.setYValue(30d);
        point3ADSR.setXValue(0d);
        point3ADSR.setYValue(30d);
        point4ADSR.setXValue(40d);
        point4ADSR.setYValue(30d);
        point5ADSR.setXValue(40d);
        point5ADSR.setYValue(0d);
        seriesADSR.getData().add(point1ADSR);
        seriesADSR.getData().add(point2ADSR);
        seriesADSR.getData().add(point3ADSR);
        seriesADSR.getData().add(point4ADSR);
        seriesADSR.getData().add(point5ADSR);
        lineChartADSR.getData().add(seriesADSR);
    }

    /**
     * Shifting the points in ADSR linechart to reflect the slider values.
     *
     * @author Axel Nilsson
     * @author Luke Eales
     */
    //region Methods for updating linechart (click to open/collapse)
    private void onAttackDrag() {
        point2ADSR.setXValue((sliderAttack.getValue() / attackMax) * 10d);
        point3ADSR.setXValue((sliderAttack.getValue() / attackMax) * 10d + (sliderDecay.getValue() / decayMax) * 10d);
    }

    private void onDecayDrag() {
        point3ADSR.setXValue((sliderAttack.getValue() / attackMax) * 10d + (sliderDecay.getValue() / decayMax) * 10d);
    }

    private void onSustainDrag() {
        point3ADSR.setYValue(sliderSustain.getValue() * 30d);
        point4ADSR.setYValue(sliderSustain.getValue() * 30d);
    }

    private void onReleaseDrag() {
        point4ADSR.setXValue(40d - (sliderRelease.getValue() / releaseMax) * 10d);
    }
    //endregion

    //region initialize methods (click to open/collapse)
    private void setupSeqButton(Button knobFreq, int i, ) {
        KnobBehaviorSeqFreq bKnobSeqFreq = new KnobBehaviorSeqFreq(knobFreq, MidiNote.F4);
        knobFreq.setOnMouseDragged(bKnobSeqFreq);
        bKnobSeqFreq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (sequencerSteps[i].isSelected()) {
                sequencerSteps[i].textProperty().setValue(bKnobSeqFreq.getNoteName());
            }
            synthaxController.setSeqMidiNote(i, MidiNote.values()[newValue.intValue()]);
        });

    }

    private void initSS() {
        /*KnobBehaviorSeqFreq bKnobSS0Freq = new KnobBehaviorSeqFreq(knobSS0freq, MidiNote.F4);
        knobSS0freq.setOnMouseDragged(bKnobSS0Freq);
        bKnobSS0Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff0.isSelected()) {
                btnStepOnOff0.textProperty().setValue(bKnobSS0Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(0, MidiNote.values()[newValue.intValue()]);
        });

         */
        setupSeqButton(sequencerFreqKnobs[0], 0);
        KnobBehaviorDetune bKnobSS0FineTune = new KnobBehaviorDetune(knobSS0FineTune);
        knobSS0FineTune.setOnMouseDragged(bKnobSS0FineTune);
        bKnobSS0FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(0, newValue.floatValue());
        });
        KnobBehavior bKnobSS0Gain = new KnobBehavior(knobSS0Gain);
        bKnobSS0Gain.setValueRotation(150, 1.0f);
        knobSS0Gain.setOnMouseDragged(bKnobSS0Gain);
        bKnobSS0Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(0, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS1Freq = new KnobBehaviorSeqFreq(knobSS1freq, MidiNote.F4);
        knobSS1freq.setOnMouseDragged(bKnobSS1Freq);
        bKnobSS1Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff1.isSelected()) {
                btnStepOnOff1.textProperty().setValue(bKnobSS1Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(1, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS1FineTune = new KnobBehaviorDetune(knobSS1FineTune);
        knobSS1FineTune.setOnMouseDragged(bKnobSS1FineTune);
        bKnobSS1FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(1, newValue.floatValue());
        });
        KnobBehavior bKnobSS1Gain = new KnobBehavior(knobSS1Gain);
        bKnobSS1Gain.setValueRotation(150, 1.0f);
        knobSS1Gain.setOnMouseDragged(bKnobSS1Gain);
        bKnobSS1Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(1, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS2Freq = new KnobBehaviorSeqFreq(knobSS2freq, MidiNote.F4);
        knobSS2freq.setOnMouseDragged(bKnobSS2Freq);
        bKnobSS2Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff2.isSelected()) {
                btnStepOnOff2.textProperty().setValue(bKnobSS2Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(2, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS2FineTune = new KnobBehaviorDetune(knobSS2FineTune);
        knobSS2FineTune.setOnMouseDragged(bKnobSS2FineTune);
        bKnobSS2FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(2, newValue.floatValue());
        });
        KnobBehavior bKnobSS2Gain = new KnobBehavior(knobSS2Gain);
        bKnobSS2Gain.setValueRotation(150, 1.0f);
        knobSS2Gain.setOnMouseDragged(bKnobSS2Gain);
        bKnobSS2Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(2, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS3Freq = new KnobBehaviorSeqFreq(knobSS3freq, MidiNote.F4);
        knobSS3freq.setOnMouseDragged(bKnobSS3Freq);
        bKnobSS3Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff3.isSelected()) {
                btnStepOnOff3.textProperty().setValue(bKnobSS3Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(3, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS3FineTune = new KnobBehaviorDetune(knobSS3FineTune);
        knobSS3FineTune.setOnMouseDragged(bKnobSS3FineTune);
        bKnobSS3FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(3, newValue.floatValue());
        });
        KnobBehavior bKnobSS3Gain = new KnobBehavior(knobSS3Gain);
        bKnobSS3Gain.setValueRotation(150, 1.0f);
        knobSS3Gain.setOnMouseDragged(bKnobSS3Gain);
        bKnobSS3Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(3, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS4Freq = new KnobBehaviorSeqFreq(knobSS4freq, MidiNote.F4);
        knobSS4freq.setOnMouseDragged(bKnobSS4Freq);
        bKnobSS4Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff4.isSelected()) {
                btnStepOnOff4.textProperty().setValue(bKnobSS4Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(4, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS4FineTune = new KnobBehaviorDetune(knobSS4FineTune);
        knobSS4FineTune.setOnMouseDragged(bKnobSS4FineTune);
        bKnobSS4FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(4, newValue.floatValue());
        });
        KnobBehavior bKnobSS4Gain = new KnobBehavior(knobSS4Gain);
        bKnobSS4Gain.setValueRotation(150, 1.0f);
        knobSS4Gain.setOnMouseDragged(bKnobSS4Gain);
        bKnobSS4Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(4, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS5Freq = new KnobBehaviorSeqFreq(knobSS5freq, MidiNote.F4);
        knobSS5freq.setOnMouseDragged(bKnobSS5Freq);
        bKnobSS5Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff5.isSelected()) {
                btnStepOnOff5.textProperty().setValue(bKnobSS5Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(5, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS5FineTune = new KnobBehaviorDetune(knobSS5FineTune);
        knobSS5FineTune.setOnMouseDragged(bKnobSS5FineTune);
        bKnobSS5FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(5, newValue.floatValue());
        });
        KnobBehavior bKnobSS5Gain = new KnobBehavior(knobSS5Gain);
        bKnobSS5Gain.setValueRotation(150, 1.0f);
        knobSS5Gain.setOnMouseDragged(bKnobSS5Gain);
        bKnobSS5Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(5, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS6Freq = new KnobBehaviorSeqFreq(knobSS6freq, MidiNote.F4);
        knobSS6freq.setOnMouseDragged(bKnobSS6Freq);
        bKnobSS6Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff6.isSelected()) {
                btnStepOnOff6.textProperty().setValue(bKnobSS6Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(6, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS6FineTune = new KnobBehaviorDetune(knobSS6FineTune);
        knobSS6FineTune.setOnMouseDragged(bKnobSS6FineTune);
        bKnobSS6FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(6, newValue.floatValue());
        });
        KnobBehavior bKnobSS6Gain = new KnobBehavior(knobSS6Gain);
        knobSS6Gain.setOnMouseDragged(bKnobSS6Gain);
        bKnobSS6Gain.setValueRotation(150, 1.0f);
        bKnobSS6Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(6, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS7Freq = new KnobBehaviorSeqFreq(knobSS7freq, MidiNote.F4);
        knobSS7freq.setOnMouseDragged(bKnobSS7Freq);
        bKnobSS7Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff7.isSelected()) {
                btnStepOnOff7.textProperty().setValue(bKnobSS7Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(7, MidiNote.values()[newValue.intValue()]);
        });

        KnobBehaviorDetune bKnobSS7FineTune = new KnobBehaviorDetune(knobSS7FineTune);
        knobSS7FineTune.setOnMouseDragged(bKnobSS7FineTune);
        bKnobSS7FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(7, newValue.floatValue());
        });
        KnobBehavior bKnobSS7Gain = new KnobBehavior(knobSS7Gain);
        bKnobSS7Gain.setValueRotation(150, 1.0f);
        knobSS7Gain.setOnMouseDragged(bKnobSS7Gain);
        bKnobSS7Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(7, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS8Freq = new KnobBehaviorSeqFreq(knobSS8freq, MidiNote.F4);
        knobSS8freq.setOnMouseDragged(bKnobSS8Freq);
        bKnobSS8Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff8.isSelected()) {
                btnStepOnOff8.textProperty().setValue(bKnobSS8Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(8, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS8FineTune = new KnobBehaviorDetune(knobSS8FineTune);
        knobSS8FineTune.setOnMouseDragged(bKnobSS8FineTune);
        bKnobSS8FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(8, newValue.floatValue());
        });
        KnobBehavior bKnobSS8Gain = new KnobBehavior(knobSS8Gain);
        bKnobSS8Gain.setValueRotation(150, 1.0f);
        knobSS8Gain.setOnMouseDragged(bKnobSS8Gain);
        bKnobSS8Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(8, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS9Freq = new KnobBehaviorSeqFreq(knobSS9freq, MidiNote.F4);
        knobSS9freq.setOnMouseDragged(bKnobSS9Freq);
        bKnobSS9Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff9.isSelected()) {
                btnStepOnOff9.textProperty().setValue(bKnobSS9Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(9, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS9FineTune = new KnobBehaviorDetune(knobSS9FineTune);
        knobSS9FineTune.setOnMouseDragged(bKnobSS9FineTune);
        bKnobSS9FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(9, newValue.floatValue());
        });
        KnobBehavior bKnobSS9Gain = new KnobBehavior(knobSS9Gain);
        bKnobSS9Gain.setValueRotation(150, 1.0f);
        knobSS9Gain.setOnMouseDragged(bKnobSS9Gain);
        bKnobSS9Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(9, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS10Freq = new KnobBehaviorSeqFreq(knobSS10freq, MidiNote.F4);
        knobSS10freq.setOnMouseDragged(bKnobSS10Freq);
        bKnobSS10Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff10.isSelected()) {
                btnStepOnOff10.textProperty().setValue(bKnobSS10Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(10, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS10FineTune = new KnobBehaviorDetune(knobSS10FineTune);
        knobSS10FineTune.setOnMouseDragged(bKnobSS10FineTune);
        bKnobSS10FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(10, newValue.floatValue());
        });
        KnobBehavior bKnobSS10Gain = new KnobBehavior(knobSS10Gain);
        bKnobSS10Gain.setValueRotation(150, 1.0f);
        knobSS10Gain.setOnMouseDragged(bKnobSS10Gain);
        bKnobSS10Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(10, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS11Freq = new KnobBehaviorSeqFreq(knobSS11freq, MidiNote.F4);
        knobSS11freq.setOnMouseDragged(bKnobSS11Freq);
        bKnobSS11Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff11.isSelected()) {
                btnStepOnOff11.textProperty().setValue(bKnobSS11Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(11, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS11FineTune = new KnobBehaviorDetune(knobSS11FineTune);
        knobSS11FineTune.setOnMouseDragged(bKnobSS11FineTune);
        bKnobSS11FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(11, newValue.floatValue());
        });
        KnobBehavior bKnobSS11Gain = new KnobBehavior(knobSS11Gain);
        bKnobSS11Gain.setValueRotation(150, 1.0f);
        knobSS11Gain.setOnMouseDragged(bKnobSS11Gain);
        bKnobSS11Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(11, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS12Freq = new KnobBehaviorSeqFreq(knobSS12freq, MidiNote.F4);
        knobSS12freq.setOnMouseDragged(bKnobSS12Freq);
        bKnobSS12Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff12.isSelected()) {
                btnStepOnOff12.textProperty().setValue(bKnobSS12Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(12, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS12FineTune = new KnobBehaviorDetune(knobSS12FineTune);
        knobSS12FineTune.setOnMouseDragged(bKnobSS12FineTune);
        bKnobSS12FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(12, newValue.floatValue());
        });
        KnobBehavior bKnobSS12Gain = new KnobBehavior(knobSS12Gain);
        bKnobSS12Gain.setValueRotation(150, 1.0f);
        knobSS12Gain.setOnMouseDragged(bKnobSS12Gain);
        bKnobSS12Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(12, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS13Freq = new KnobBehaviorSeqFreq(knobSS13freq, MidiNote.F4);
        knobSS13freq.setOnMouseDragged(bKnobSS13Freq);
        bKnobSS13Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff13.isSelected()) {
                btnStepOnOff13.textProperty().setValue(bKnobSS13Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(13, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS13FineTune = new KnobBehaviorDetune(knobSS13FineTune);
        knobSS13FineTune.setOnMouseDragged(bKnobSS13FineTune);
        bKnobSS13FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(13, newValue.floatValue());
        });
        KnobBehavior bKnobSS13Gain = new KnobBehavior(knobSS13Gain);
        bKnobSS13Gain.setValueRotation(150, 1.0f);
        knobSS13Gain.setOnMouseDragged(bKnobSS13Gain);
        bKnobSS13Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(13, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS14Freq = new KnobBehaviorSeqFreq(knobSS14freq, MidiNote.F4);
        knobSS14freq.setOnMouseDragged(bKnobSS14Freq);
        bKnobSS14Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff14.isSelected()) {
                btnStepOnOff14.textProperty().setValue(bKnobSS14Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(14, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS14FineTune = new KnobBehaviorDetune(knobSS14FineTune);
        knobSS14FineTune.setOnMouseDragged(bKnobSS14FineTune);
        bKnobSS14FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(14, newValue.floatValue());
        });
        KnobBehavior bKnobSS14Gain = new KnobBehavior(knobSS14Gain);
        bKnobSS14Gain.setValueRotation(150, 1.0f);
        knobSS14Gain.setOnMouseDragged(bKnobSS14Gain);
        bKnobSS14Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(14, newValue.floatValue());
        });
        KnobBehaviorSeqFreq bKnobSS15Freq = new KnobBehaviorSeqFreq(knobSS15freq, MidiNote.F4);
        knobSS15freq.setOnMouseDragged(bKnobSS15Freq);
        bKnobSS15Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            if (btnStepOnOff15.isSelected()) {
                btnStepOnOff15.textProperty().setValue(bKnobSS15Freq.getNoteName());
            }
            synthaxController.setSeqMidiNote(15, MidiNote.values()[newValue.intValue()]);
        });
        KnobBehaviorDetune bKnobSS15FineTune = new KnobBehaviorDetune(knobSS15FineTune);
        knobSS15FineTune.setOnMouseDragged(bKnobSS15FineTune);
        bKnobSS15FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqDetuneCent(15, newValue.floatValue());
        });
        KnobBehavior bKnobSS15Gain = new KnobBehavior(knobSS15Gain);
        bKnobSS15Gain.setValueRotation(150, 1.0f);
        knobSS15Gain.setOnMouseDragged(bKnobSS15Gain);
        bKnobSS15Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqVelocity(15, newValue.floatValue());
        });

        KnobBehavior bKnobSSRate = new KnobBehavior(knobSSRate);
        bKnobSSRate.setValueRotation(0, 0.5f);
        knobSSRate.setOnMouseDragged(bKnobSSRate);
        bKnobSSRate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqBPM(newValue.floatValue());
        });
        btnStepOnOff0.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff0.textProperty().setValue(bKnobSS0Freq.getNoteName());
            } else {
                btnStepOnOff0.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(0, newValue);
        });
        btnStepOnOff1.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff1.textProperty().setValue(bKnobSS1Freq.getNoteName());
            } else {
                btnStepOnOff1.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(1, newValue);
        });
        btnStepOnOff2.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff2.textProperty().setValue(bKnobSS2Freq.getNoteName());
            } else {
                btnStepOnOff2.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(2, newValue);
        });
        btnStepOnOff3.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff3.textProperty().setValue(bKnobSS3Freq.getNoteName());
            } else {
                btnStepOnOff3.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(3, newValue);
        });

        btnStepOnOff4.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff4.textProperty().setValue(bKnobSS4Freq.getNoteName());
            } else {
                btnStepOnOff4.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(4, newValue);
        });
        btnStepOnOff5.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff5.textProperty().setValue(bKnobSS5Freq.getNoteName());
            } else {
                btnStepOnOff5.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(5, newValue);
        });
        btnStepOnOff6.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff6.textProperty().setValue(bKnobSS6Freq.getNoteName());
            } else {
                btnStepOnOff6.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(6, newValue);
        });
        btnStepOnOff7.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff7.textProperty().setValue(bKnobSS7Freq.getNoteName());
            } else {
                btnStepOnOff7.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(7, newValue);
        });

        btnStepOnOff8.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff8.textProperty().setValue(bKnobSS8Freq.getNoteName());
            } else {
                btnStepOnOff8.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(8, newValue);
        });
        btnStepOnOff9.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff9.textProperty().setValue(bKnobSS9Freq.getNoteName());
            } else {
                btnStepOnOff9.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(9, newValue);
        });
        btnStepOnOff10.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff10.textProperty().setValue(bKnobSS10Freq.getNoteName());
            } else {
                btnStepOnOff10.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(10, newValue);
        });
        btnStepOnOff11.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff11.textProperty().setValue(bKnobSS11Freq.getNoteName());
            } else {
                btnStepOnOff11.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(11, newValue);
        });
        btnStepOnOff12.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff12.textProperty().setValue(bKnobSS12Freq.getNoteName());
            } else {
                btnStepOnOff12.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(12, newValue);
        });
        btnStepOnOff13.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff13.textProperty().setValue(bKnobSS13Freq.getNoteName());
            } else {
                btnStepOnOff13.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(13, newValue);
        });
        btnStepOnOff14.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff14.textProperty().setValue(bKnobSS14Freq.getNoteName());
            } else {
                btnStepOnOff14.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(14, newValue);
        });
        btnStepOnOff15.selectedProperty().addListener((v, oldValue, newValue) -> {
            if (newValue) {
                btnStepOnOff15.textProperty().setValue(bKnobSS15Freq.getNoteName());
            } else {
                btnStepOnOff15.textProperty().setValue("Off");
            }
            synthaxController.setStepOnOff(15, newValue);
        });
        SSStartStop.setOnMousePressed(l -> {
            boolean running = synthaxController.sequencerIsRunning();
            if (!running) {
                synthaxController.sequencerOn();
                SSStartStop.setText("Stop");
                SSStartStop.setStyle("-fx-text-fill: #f78000");
            } else {
                synthaxController.sequencerOff();
                SSStartStop.setText("Start");
                SSStartStop.setStyle("-fx-text-fill: #d6d1c9");
            }
        });
    }

    private void initNoise() {
        tglSwitchNoise.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setNoiseActive(newValue);
        });

        KnobBehavior bKnobNoiseGain = new KnobBehavior(knobNoiseGain);
        knobNoiseGain.setOnMouseDragged(bKnobNoiseGain);
        bKnobNoiseGain.setValueRotation(0, 0.5f);
        bKnobNoiseGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setNoiseGain(newValue.floatValue());
        });
    }

    private void initDelay() {
        tglSwitchDelay.selectedProperty().addListener((v, oldValue, newValue) -> {

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

        KnobBehavior bKnobDelayLevel = new KnobBehavior(knobDelayLevel);
        knobDelayLevel.setOnMouseDragged(bKnobDelayLevel);
        bKnobDelayLevel.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });
    }

    private void initReverb() {
        tglSwitchReverb.selectedProperty().addListener((v, oldValue, newValue) -> {

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
    }

    private void initLFO() {
        tglSwitchLFO.selectedProperty().addListener((v, oldValue, newValue) -> {

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
    }

    private void initFilter() {
        tglSwitchFilterNotch.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setNotchActive(newValue);
        });

        tglSwitchFilterHP.selectedProperty().addListener(((v, oldValue, newValue) -> synthaxController.setHPActive(newValue)));
        tglSwitchFilterLP.selectedProperty().addListener(((v, oldValue, newValue) -> synthaxController.setLPActive(newValue)));

        KnobBehavior bKnobFilterHPCutoff = new KnobBehavior(knobFilterHPCutoff);
        knobFilterHPCutoff.setOnMouseDragged(bKnobFilterHPCutoff);
        bKnobFilterHPCutoff.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setHPCutoff(newValue.floatValue());
        });

        KnobBehavior bKnobFilterHPSlope = new KnobBehavior(knobFilterHPSlope);
        knobFilterHPSlope.setOnMouseDragged(bKnobFilterHPSlope);
        bKnobFilterHPSlope.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setHPSlope(newValue.floatValue());
        });

        KnobBehavior bKnobFilterFreq = new KnobBehavior(knobFilterFreq);
        knobFilterFreq.setOnMouseDragged(bKnobFilterFreq);
        bKnobFilterFreq.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setNotchFrequency(newValue.floatValue());
        });

        KnobBehavior bKnobFilterRange = new KnobBehavior(knobFilterRange);
        knobFilterRange.setOnMouseDragged(bKnobFilterRange);
        bKnobFilterRange.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setNotchRange(newValue.floatValue());
        });

        KnobBehavior bKnobFilterLPCutoff = new KnobBehavior(knobFilterLPCutoff);
        knobFilterLPCutoff.setOnMouseDragged(bKnobFilterLPCutoff);
        bKnobFilterLPCutoff.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setLPCutoff(newValue.floatValue());
        });

        KnobBehavior bKnobFilterLPSlope = new KnobBehavior(knobFilterLPSlope);
        knobFilterLPSlope.setOnMouseDragged(bKnobFilterLPSlope);
        bKnobFilterLPSlope.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setLPSlope(newValue.floatValue());
        });
    }

    private void initADSR() {
        setupLineChart();

        sliderAttack.setMax(attackMax);
        sliderAttack.setMin(10);
        sliderAttack.setBlockIncrement(50);
        sliderAttack.valueProperty().addListener((observableValue, number, t1) -> {
            ADSRValues.setAttackValue(t1.floatValue());
            onAttackDrag();
        });

        sliderDecay.setMax(1500);
        sliderDecay.setMin(10);
        sliderDecay.setBlockIncrement(50);
        sliderDecay.valueProperty().addListener((observableValue, number, t1) -> {
            ADSRValues.setDecayValue(t1.floatValue());
            onDecayDrag();
        });

        sliderSustain.setMax(1);
        sliderSustain.setValue(1);
        sliderSustain.setBlockIncrement(0.1);
        sliderSustain.valueProperty().addListener((observableValue, number, t1) -> {
            ADSRValues.setSustainValue(t1.floatValue());
            onSustainDrag();
        });

        sliderRelease.setMax(2000);
        sliderRelease.setMin(10);
        sliderRelease.setBlockIncrement(50);
        sliderRelease.valueProperty().addListener((observableValue, number, t1) -> {
            ADSRValues.setReleaseValue(t1.floatValue());
            onReleaseDrag();
        });
    }
    private void initKeyStatus() {
        keyStatus.put(KeyCode.A, new AtomicBoolean(false));
        keyStatus.put(KeyCode.W, new AtomicBoolean(false));
        keyStatus.put(KeyCode.S, new AtomicBoolean(false));
        keyStatus.put(KeyCode.E, new AtomicBoolean(false));
        keyStatus.put(KeyCode.D, new AtomicBoolean(false));
        keyStatus.put(KeyCode.F, new AtomicBoolean(false));
        keyStatus.put(KeyCode.T, new AtomicBoolean(false));
        keyStatus.put(KeyCode.G, new AtomicBoolean(false));
        keyStatus.put(KeyCode.Y, new AtomicBoolean(false));
        keyStatus.put(KeyCode.H, new AtomicBoolean(false));
        keyStatus.put(KeyCode.U, new AtomicBoolean(false));
        keyStatus.put(KeyCode.J, new AtomicBoolean(false));
        keyStatus.put(KeyCode.K, new AtomicBoolean(false));
    }
    private void initKeyBoardListeners() {
        mainPane.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            // If it's a valid key, send a noteOn message.
            if (keyStatus.containsKey(keyCode)) {
                if (keyStatus.get(keyCode).compareAndSet(false, true)) {
                    MidiNote note = MidiHelpers.keyCodeToMidi(keyCode);
                    System.out.println("++++" + note.name());
                    synthaxController.noteOn(note, 127);
                }
            }
        });
        mainPane.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            if (keyStatus.containsKey(keyCode)) {
                if (keyStatus.get(keyCode).compareAndSet(true, false)) {
                    MidiNote note = MidiHelpers.keyCodeToMidi(keyCode);
                    System.out.println("----" + note.name());
                    synthaxController.noteOff(note);
                }
            }
        });
    }
    //endregion
}
