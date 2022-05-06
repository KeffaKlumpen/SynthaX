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
import com.synthax.model.sequencer.SequencerMode;
import com.synthax.model.enums.Waveforms;
import com.synthax.util.MidiHelpers;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for synthesizer main view
 *
 * @author Axel Nilsson
 * @author Luke Eales
 */
public class SynthaxView implements Initializable {
    //region FXML variables
    @FXML private Button randomize;
    @FXML private Spinner<SequencerMode> sequencerMode;
    @FXML private VBox oscillatorChainView;
    @FXML private Button knobNoiseGain;
    @FXML private ToggleSwitch tglSwitchNoise;
    @FXML private Button knobDelayFeedback;
    @FXML private Button knobDelayTime;
    @FXML private Button knobDelayDecay;
    @FXML private Button knobDelayLevel;
    @FXML private ToggleSwitch tglSwitchDelay;
    @FXML private Button knobReverbSize;
    @FXML private Button knobReverbTone;
    @FXML private Button knobReverbDW;
    @FXML private ToggleSwitch tglSwitchReverb;
    @FXML private Button knobLFODepth;
    @FXML private Button knobLFORate;
    @FXML private Button knobLFOWaveForm;
    @FXML private ToggleSwitch tglSwitchLFO;
    @FXML private Button knobFilterHPCutoff;
    @FXML private Button knobFilterLPCutoff;
    @FXML private Button knobEQ1Gain;
    @FXML private Button knobEQ1Freq;
    @FXML private Button knobEQ1Range;
    @FXML private Button knobEQ2Gain;
    @FXML private Button knobEQ2Freq;
    @FXML private Button knobEQ2Range;
    @FXML private Button knobEQ3Gain;
    @FXML private Button knobEQ3Freq;
    @FXML private Button knobEQ3Range;
    @FXML private ToggleSwitch tglSwitchEQ1;
    @FXML private ToggleSwitch tglSwitchEQ2;
    @FXML private ToggleSwitch tglSwitchEQ3;
    @FXML private ToggleSwitch tglSwitchFilterLP;
    @FXML private ToggleSwitch tglSwitchFilterHP;
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
    //endregion
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
    @FXML private Spinner<Integer> spinnerSteps;
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


    private final HashMap<KeyCode, AtomicBoolean> keyStatus = new HashMap<>();
    private Button[] sequencerFreqKnobs;
    private Button[] sequencerDetuneKnobs;
    private Button[] sequencerGainKnobs;
    private Button[] EQGain;
    private Button[] EQFreq;
    private Button[] EQRange;
    private ToggleButton[] sequencerSteps;
    private KnobBehavior[] knobBehaviorsGain = new KnobBehavior[16];
    private KnobBehaviorDetune[] knobBehaviorDetunes = new KnobBehaviorDetune[16];
    private KnobBehaviorSeqFreq[] knobBehaviorSeqFreqs = new KnobBehaviorSeqFreq[16];
    private XYChart.Data<Number, Number> point1ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point2ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point3ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point4ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point5ADSR = new XYChart.Data<>();

    private final SynthaxController synthaxController;
    private final int attackMax = 3000;
    private final int decayMax = 1500;
    private final int releaseMax = 2000;



    public SynthaxView() {
        synthaxController = new SynthaxController(this);
    }
    @FXML
    public void randomizeSequencer() {
        synthaxController.randomize(sequencerSteps.length);
    }
    public void setSequencerStepsOnOff(boolean on, int index) {
        sequencerSteps[index].setSelected(on);
    }
    public void setSequencerFreqKnobs(MidiNote note, int index) {
        knobBehaviorSeqFreqs[index].setNote(note);
    }

    @FXML
    public void onActionAddOscillator() {
        try {
            URL fxmlLocation = MainApplication.class.getResource("view/Oscillator-v.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Node oscillatorRoot = fxmlLoader.load();
            OscillatorView oscillatorView = fxmlLoader.getController();
            OscillatorController oscillatorController = oscillatorView.getOscillatorController();

            synthaxController.addOscillator(oscillatorController);

            oscillatorView.getBtnRemoveOscillator().setOnAction(event -> {
                synthaxController.removeOscillator(oscillatorController);
                oscillatorChainView.getChildren().remove(oscillatorRoot);
            });
            oscillatorView.getBtnMoveDown().setOnAction(event -> {
                Node[] childList = oscillatorChainView.getChildren().toArray(new Node[0]);
                int oscIndex = oscillatorChainView.getChildren().indexOf(oscillatorRoot);

                if (oscIndex < childList.length - 1) {
                    Node nextOsc = childList[oscIndex + 1];
                    childList[oscIndex + 1] = oscillatorRoot;
                    childList[oscIndex] = nextOsc;

                    oscillatorChainView.getChildren().setAll(childList);

                    synthaxController.moveOscillatorDown(oscillatorController);
                }
            });
            oscillatorView.getBtnMoveUp().setOnAction(event -> {
                Node[] childList = oscillatorChainView.getChildren().toArray(new Node[0]);
                int oscIndex = oscillatorChainView.getChildren().indexOf(oscillatorRoot);

                if (oscIndex > 0) {
                    Node prevOsc = childList[oscIndex - 1];
                    childList[oscIndex - 1] = oscillatorRoot;
                    childList[oscIndex] = prevOsc;

                    oscillatorChainView.getChildren().setAll(childList);

                    synthaxController.moveOscillatorUp(oscillatorController);
                }
            });

            oscillatorChainView.getChildren().add(oscillatorRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSeqButtonOrange(int i) {
        Platform.runLater(() -> {
            sequencerSteps[i].setStyle("-fx-background-color: #f78000");
        });
    }

    public void setSeqButtonGray(int i) {
        Platform.runLater(()-> {
            sequencerSteps[i].setStyle("-fx-background-color: #78736b");

        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSequencerArrays();
        initFilterArrays();
        initKeyHash();
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

    private void initFilterArrays() {
        EQFreq = new Button[] {
                knobEQ1Freq,
                knobEQ2Freq,
                knobEQ3Freq};
        EQGain = new Button[] {
                knobEQ1Gain,
                knobEQ2Gain,
                knobEQ3Gain};
        EQRange = new Button[] {
                knobEQ1Range,
                knobEQ2Range,
                knobEQ3Range};
    }

    private void initSequencerArrays() {
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
    private void initSS() {
        for (int i = 0; i < sequencerFreqKnobs.length; i++) {
            int finali = i;
            knobBehaviorSeqFreqs[i] = new KnobBehaviorSeqFreq(sequencerFreqKnobs[i], MidiNote.F4);
            sequencerFreqKnobs[i].setOnMouseDragged(knobBehaviorSeqFreqs[i]);
            knobBehaviorSeqFreqs[i].knobValueProperty().addListener((v, oldValue, newValue) -> {
                if (sequencerSteps[finali].isSelected()) {
                    sequencerSteps[finali].textProperty().setValue(knobBehaviorSeqFreqs[finali].getNoteName());
                }
                synthaxController.setSeqMidiNote(finali, MidiNote.values()[newValue.intValue()]);
            });
        }

        for (int i = 0; i < sequencerDetuneKnobs.length; i++) {
            int finali = i;
            knobBehaviorDetunes[i] = new KnobBehaviorDetune(sequencerDetuneKnobs[i]);
            sequencerDetuneKnobs[i].setOnMouseDragged(knobBehaviorDetunes[i]);
            knobBehaviorDetunes[i].knobValueProperty().addListener((v, oldValue, newValue) -> {
                synthaxController.setSeqDetuneCent(finali, newValue.floatValue());
            });
        }

        for (int i = 0; i < sequencerGainKnobs.length; i++) {
            int finalI = i;
            knobBehaviorsGain[i] = new KnobBehavior(sequencerGainKnobs[i]);
            knobBehaviorsGain[i].setValueRotation(150, 1.0f);
            sequencerGainKnobs[i].setOnMouseDragged(knobBehaviorsGain[i]);
            knobBehaviorsGain[i].knobValueProperty().addListener((v, oldValue, newValue) -> {
                synthaxController.setSeqVelocity(finalI, newValue.floatValue());
            });
        }

        KnobBehavior bKnobSSRate = new KnobBehavior(knobSSRate);
        bKnobSSRate.setValueRotation(0, 0.5f);
        knobSSRate.setOnMouseDragged(bKnobSSRate);
        bKnobSSRate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqBPM(newValue.floatValue());
        });

        for (int i = 0; i < sequencerSteps.length; i++) {
            int finalI = i;
            sequencerSteps[i].selectedProperty().addListener((v, oldValue, newValue) -> {
                if (newValue) {
                    sequencerSteps[finalI].textProperty().setValue(knobBehaviorSeqFreqs[finalI].getNoteName());
                } else {
                    sequencerSteps[finalI].textProperty().setValue("Off");
                }
                synthaxController.setStepOnOff(finalI, newValue);
            });
        }
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
        SpinnerValueFactory<Integer> spf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,16,16);
        spinnerSteps.setValueFactory(spf);
        spinnerSteps.valueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqNSteps(newValue);
        });

        SpinnerValueFactory<SequencerMode> spfMode = new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(SequencerMode.values()));
        sequencerMode.setValueFactory(spfMode);
        sequencerMode.valueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSequencerMode(newValue);
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
            synthaxController.setDelayActive(newValue);
        });

        KnobBehavior bKnobDelayFeedback = new KnobBehavior(knobDelayFeedback);
        knobDelayFeedback.setOnMouseDragged(bKnobDelayFeedback);
        bKnobDelayFeedback.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setDelayFeedback(newValue.floatValue());
        });

        KnobBehavior bKnobDelayTime = new KnobBehavior(knobDelayTime);
        knobDelayTime.setOnMouseDragged(bKnobDelayTime);
        bKnobDelayTime.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setDelayTime(newValue.floatValue());
        });

        KnobBehavior bKnobDelayDecay = new KnobBehavior(knobDelayDecay);
        knobDelayDecay.setOnMouseDragged(bKnobDelayDecay);
        bKnobDelayDecay.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setDelayDecay(newValue.floatValue());
        });

        KnobBehavior bKnobDelayLevel = new KnobBehavior(knobDelayLevel);
        knobDelayLevel.setOnMouseDragged(bKnobDelayLevel);
        bKnobDelayLevel.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setDelayLevel(newValue.floatValue());
        });
    }

    private void initReverb() {
        tglSwitchReverb.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setReverbActive(newValue);
        });

        KnobBehavior bKnobReverbSize = new KnobBehavior(knobReverbSize);
        knobReverbSize.setOnMouseDragged(bKnobReverbSize);
        bKnobReverbSize.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setReverbSize(newValue.floatValue());
        });

        KnobBehavior bKnobReverbDecay = new KnobBehavior(knobReverbTone);
        knobReverbTone.setOnMouseDragged(bKnobReverbDecay);
        bKnobReverbDecay.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setReverbTone(newValue.floatValue());
        });

        KnobBehavior bKnobReverbDW = new KnobBehavior(knobReverbDW);
        knobReverbDW.setOnMouseDragged(bKnobReverbDW);
        bKnobReverbDW.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setReverbDryWet(newValue.floatValue());
        });
    }

    private void initLFO() {
        tglSwitchLFO.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setLFOActive(newValue.booleanValue());
        });

        KnobBehavior bKnobLFODepth = new KnobBehavior(knobLFODepth);
        knobLFODepth.setOnMouseDragged(bKnobLFODepth);
        bKnobLFODepth.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setLFODepth(newValue.floatValue());
        });

        KnobBehavior bKnobLFORate = new KnobBehavior(knobLFORate);
        knobLFORate.setOnMouseDragged(bKnobLFORate);
        bKnobLFORate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setLFORate(newValue.floatValue());
        });

        KnobBehaviorWave bKnobLFOWaveform = new KnobBehaviorWave(knobLFOWaveForm);
        knobLFOWaveForm.setOnMouseDragged(bKnobLFOWaveform);
        bKnobLFOWaveform.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setLFOWaveform(Waveforms.values()[newValue.intValue()]);
        });
    }

    private void initFilter() {
        tglSwitchFilterHP.selectedProperty().addListener((v, oldValue, newValue) -> synthaxController.setHPActive(newValue));
        tglSwitchFilterLP.selectedProperty().addListener((v, oldValue, newValue) -> synthaxController.setLPActive(newValue));
        tglSwitchEQ1.selectedProperty().addListener((v, oldValue, newValue) -> {

        });
        tglSwitchEQ2.selectedProperty().addListener((v, oldValue, newValue) -> {

        });
        tglSwitchEQ3.selectedProperty().addListener((v, oldValue, newValue) -> {

        });

        for (int i = 0; i < EQGain.length; i++) {
            int finalI = i;
            KnobBehaviorDetune b = new KnobBehaviorDetune(EQGain[i]);
            EQGain[i].setOnMouseDragged(b);
            b.knobValueProperty().addListener((v, oldValue, newValue) -> {

            });
        }
        for (int i = 0; i < EQRange.length; i++) {
            int finalI = i;
            KnobBehavior b = new KnobBehavior(EQRange[i]);
            EQGain[i].setOnMouseDragged(b);
            b.knobValueProperty().addListener((v, oldValue, newValue) -> {
                System.out.println("hej");
            });
        }
        for (int i = 0; i < EQFreq.length; i++) {
            int finalI = i;
            KnobBehavior b = new KnobBehavior(EQFreq[i]);
            EQFreq[i].setOnMouseDragged(b);
            b.knobValueProperty().addListener((v, oldValue, newValue) -> {

            });
        }

        KnobBehavior bKnobFilterHPCutoff = new KnobBehavior(knobFilterHPCutoff);
        knobFilterHPCutoff.setOnMouseDragged(bKnobFilterHPCutoff);
        bKnobFilterHPCutoff.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setHPCutoff(newValue.floatValue());
        });

        KnobBehavior bKnobFilterLPCutoff = new KnobBehavior(knobFilterLPCutoff);
        knobFilterLPCutoff.setOnMouseDragged(bKnobFilterLPCutoff);
        bKnobFilterLPCutoff.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setLPCutoff(newValue.floatValue());
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
    private void initKeyHash() {
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
