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

import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * BAJS
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
    @FXML private LineChart<Number, Number> lineChartADSR = new LineChart<Number, Number>(xAxis,yAxis);
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
    //endregion



    private XYChart.Data<Number, Number> point1ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point2ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point3ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point4ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point5ADSR = new XYChart.Data<>();

    private final SynthaxController synthaxController;
    private final int attackMax = 3000;
    private final int decayMax = 1500;
    private final int releaseMax = 2000;

    private final Map<KeyCode, AtomicBoolean> keyStatus = Map.of(KeyCode.A, new AtomicBoolean(false),
            KeyCode.S, new AtomicBoolean(false),
            KeyCode.D, new AtomicBoolean(false),
            KeyCode.F, new AtomicBoolean(false),
            KeyCode.G, new AtomicBoolean(false),
            KeyCode.H, new AtomicBoolean(false));

    public SynthaxView() {
        synthaxController = new SynthaxController();
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

                if(oscIndex < childList.length - 1){
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

                if(oscIndex > 0){
                    Node prevOsc = childList[oscIndex - 1];
                    childList[oscIndex - 1] = oscillatorView;
                    childList[oscIndex] = prevOsc;

                    oscillatorChainView.getChildren().setAll(childList);

                    synthaxController.moveOscillatorUp(oscillatorController);
                }
            });

            oscillatorChainView.getChildren().add(oscillatorView);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
     * @author Axel Nilsson
     * @author Luke Eales
     */
    //region Methods for updating linechart (click to open/collapse)
    private void onAttackDrag() {
        point2ADSR.setXValue((sliderAttack.getValue()/attackMax)*10d);
        point3ADSR.setXValue((sliderAttack.getValue()/attackMax)*10d+(sliderDecay.getValue()/decayMax)*10d);
    }

    private void onDecayDrag() {
        point3ADSR.setXValue((sliderAttack.getValue()/attackMax)*10d+(sliderDecay.getValue()/decayMax)*10d);
    }
    private void onSustainDrag() {
        point3ADSR.setYValue(sliderSustain.getValue()*30d);
        point4ADSR.setYValue(sliderSustain.getValue()*30d);
    }
    private void onReleaseDrag() {
        point4ADSR.setXValue(40d-(sliderRelease.getValue()/releaseMax)*10d);
    }
    //endregion

    //region initialize methods (click to open/collapse)
    private void initSS() {
        KnobBehaviorSeqFreq bKnobSS0Freq = new KnobBehaviorSeqFreq(knobSS0freq, MidiNote.C4);
        knobSS0freq.setOnMouseDragged(bKnobSS0Freq);
        bKnobSS0Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS0FineTune = new KnobBehaviorDetune(knobSS0FineTune);
        knobSS0FineTune.setOnMouseDragged(bKnobSS0FineTune);
        bKnobSS0FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS0Gain = new KnobBehavior(knobSS0Gain);
        knobSS0Gain.setOnMouseDragged(bKnobSS0Gain);
        bKnobSS0Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS1Freq = new KnobBehaviorSeqFreq(knobSS1freq, MidiNote.Db4);
        knobSS1freq.setOnMouseDragged(bKnobSS1Freq);
        bKnobSS1Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS1FineTune = new KnobBehaviorDetune(knobSS1FineTune);
        knobSS1FineTune.setOnMouseDragged(bKnobSS1FineTune);
        bKnobSS1FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS1Gain = new KnobBehavior(knobSS1Gain);
        knobSS1Gain.setOnMouseDragged(bKnobSS1Gain);
        bKnobSS1Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS2Freq = new KnobBehaviorSeqFreq(knobSS2freq, MidiNote.D4);
        knobSS2freq.setOnMouseDragged(bKnobSS2Freq);
        bKnobSS2Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS2FineTune = new KnobBehaviorDetune(knobSS2FineTune);
        knobSS2FineTune.setOnMouseDragged(bKnobSS2FineTune);
        bKnobSS2FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS2Gain = new KnobBehavior(knobSS2Gain);
        knobSS2Gain.setOnMouseDragged(bKnobSS2Gain);
        bKnobSS2Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS3Freq = new KnobBehaviorSeqFreq(knobSS3freq, MidiNote.Eb4);
        knobSS3freq.setOnMouseDragged(bKnobSS3Freq);
        bKnobSS3Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS3FineTune = new KnobBehaviorDetune(knobSS3FineTune);
        knobSS3FineTune.setOnMouseDragged(bKnobSS3FineTune);
        bKnobSS3FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS3Gain = new KnobBehavior(knobSS3Gain);
        knobSS3Gain.setOnMouseDragged(bKnobSS3Gain);
        bKnobSS3Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS4Freq = new KnobBehaviorSeqFreq(knobSS4freq, MidiNote.E4);
        knobSS4freq.setOnMouseDragged(bKnobSS4Freq);
        bKnobSS4Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS4FineTune = new KnobBehaviorDetune(knobSS4FineTune);
        knobSS4FineTune.setOnMouseDragged(bKnobSS4FineTune);
        bKnobSS4FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS4Gain = new KnobBehavior(knobSS4Gain);
        knobSS4Gain.setOnMouseDragged(bKnobSS4Gain);
        bKnobSS4Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS5Freq = new KnobBehaviorSeqFreq(knobSS5freq, MidiNote.F4);
        knobSS5freq.setOnMouseDragged(bKnobSS5Freq);
        bKnobSS5Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS5FineTune = new KnobBehaviorDetune(knobSS5FineTune);
        knobSS5FineTune.setOnMouseDragged(bKnobSS5FineTune);
        bKnobSS5FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS5Gain = new KnobBehavior(knobSS5Gain);
        knobSS5Gain.setOnMouseDragged(bKnobSS5Gain);
        bKnobSS5Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS6Freq = new KnobBehaviorSeqFreq(knobSS6freq, MidiNote.Gb4);
        knobSS6freq.setOnMouseDragged(bKnobSS6Freq);
        bKnobSS6Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS6FineTune = new KnobBehaviorDetune(knobSS6FineTune);
        knobSS6FineTune.setOnMouseDragged(bKnobSS6FineTune);
        bKnobSS6FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS6Gain = new KnobBehavior(knobSS6Gain);
        knobSS6Gain.setOnMouseDragged(bKnobSS6Gain);
        bKnobSS6Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS7Freq = new KnobBehaviorSeqFreq(knobSS7freq, MidiNote.G4);
        knobSS7freq.setOnMouseDragged(bKnobSS7Freq);
        bKnobSS7Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });

        KnobBehaviorDetune bKnobSS7FineTune = new KnobBehaviorDetune(knobSS7FineTune);
        knobSS7FineTune.setOnMouseDragged(bKnobSS7FineTune);
        bKnobSS7FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS7Gain = new KnobBehavior(knobSS7Gain);
        knobSS7Gain.setOnMouseDragged(bKnobSS7Gain);
        bKnobSS7Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS8Freq = new KnobBehaviorSeqFreq(knobSS8freq, MidiNote.Ab4);
        knobSS8freq.setOnMouseDragged(bKnobSS8Freq);
        bKnobSS8Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS8FineTune = new KnobBehaviorDetune(knobSS8FineTune);
        knobSS8FineTune.setOnMouseDragged(bKnobSS8FineTune);
        bKnobSS8FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS8Gain = new KnobBehavior(knobSS8Gain);
        knobSS8Gain.setOnMouseDragged(bKnobSS8Gain);
        bKnobSS8Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS9Freq = new KnobBehaviorSeqFreq(knobSS9freq, MidiNote.A4);
        knobSS9freq.setOnMouseDragged(bKnobSS9Freq);
        bKnobSS9Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS9FineTune = new KnobBehaviorDetune(knobSS9FineTune);
        knobSS9FineTune.setOnMouseDragged(bKnobSS9FineTune);
        bKnobSS9FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS9Gain = new KnobBehavior(knobSS9Gain);
        knobSS9Gain.setOnMouseDragged(bKnobSS9Gain);
        bKnobSS9Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS10Freq = new KnobBehaviorSeqFreq(knobSS10freq, MidiNote.Bb4);
        knobSS10freq.setOnMouseDragged(bKnobSS10Freq);
        bKnobSS10Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS10FineTune = new KnobBehaviorDetune(knobSS10FineTune);
        knobSS10FineTune.setOnMouseDragged(bKnobSS10FineTune);
        bKnobSS10FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS10Gain = new KnobBehavior(knobSS10Gain);
        knobSS10Gain.setOnMouseDragged(bKnobSS10Gain);
        bKnobSS10Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS11Freq = new KnobBehaviorSeqFreq(knobSS11freq, MidiNote.B4);
        knobSS11freq.setOnMouseDragged(bKnobSS11Freq);
        bKnobSS11Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS11FineTune = new KnobBehaviorDetune(knobSS11FineTune);
        knobSS11FineTune.setOnMouseDragged(bKnobSS11FineTune);
        bKnobSS11FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS11Gain = new KnobBehavior(knobSS11Gain);
        knobSS11Gain.setOnMouseDragged(bKnobSS11Gain);
        bKnobSS11Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS12Freq = new KnobBehaviorSeqFreq(knobSS12freq, MidiNote.C5);
        knobSS12freq.setOnMouseDragged(bKnobSS12Freq);
        bKnobSS12Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS12FineTune = new KnobBehaviorDetune(knobSS12FineTune);
        knobSS12FineTune.setOnMouseDragged(bKnobSS12FineTune);
        bKnobSS12FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS12Gain = new KnobBehavior(knobSS12Gain);
        knobSS12Gain.setOnMouseDragged(bKnobSS12Gain);
        bKnobSS12Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS13Freq = new KnobBehaviorSeqFreq(knobSS13freq, MidiNote.Db5);
        knobSS13freq.setOnMouseDragged(bKnobSS13Freq);
        bKnobSS13Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS13FineTune = new KnobBehaviorDetune(knobSS13FineTune);
        knobSS13FineTune.setOnMouseDragged(bKnobSS13FineTune);
        bKnobSS13FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS13Gain = new KnobBehavior(knobSS13Gain);
        knobSS13Gain.setOnMouseDragged(bKnobSS13Gain);
        bKnobSS13Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS14Freq = new KnobBehaviorSeqFreq(knobSS14freq, MidiNote.D5);
        knobSS14freq.setOnMouseDragged(bKnobSS14Freq);
        bKnobSS14Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS14FineTune = new KnobBehaviorDetune(knobSS14FineTune);
        knobSS14FineTune.setOnMouseDragged(bKnobSS14FineTune);
        bKnobSS14FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS14Gain = new KnobBehavior(knobSS14Gain);
        knobSS14Gain.setOnMouseDragged(bKnobSS14Gain);
        bKnobSS14Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorSeqFreq bKnobSS15Freq = new KnobBehaviorSeqFreq(knobSS15freq, MidiNote.Eb5);
        knobSS15freq.setOnMouseDragged(bKnobSS15Freq);
        bKnobSS15Freq.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehaviorDetune bKnobSS15FineTune = new KnobBehaviorDetune(knobSS15FineTune);
        knobSS15FineTune.setOnMouseDragged(bKnobSS15FineTune);
        bKnobSS15FineTune.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
        KnobBehavior bKnobSS15Gain = new KnobBehavior(knobSS15Gain);
        knobSS15Gain.setOnMouseDragged(bKnobSS15Gain);
        bKnobSS15Gain.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });

        KnobBehavior bKnobSSRate = new KnobBehavior(knobSSRate);
        knobSSRate.setOnMouseDragged(bKnobSSRate);
        bKnobSSRate.knobValueProperty().addListener((v, oldValue, newValue) -> {

        });
    }
    private void initNoise() {
        tglSwitchNoise.selectedProperty().addListener((v, oldValue, newValue) -> {

        });

        KnobBehavior bKnobNoiseGain = new KnobBehavior(knobNoiseGain);
        knobNoiseGain.setOnMouseDragged(bKnobNoiseGain);
        bKnobNoiseGain.setValueZero();
        bKnobNoiseGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here.
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

    public void initKeyBoardListeners() {
        mainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                // If it's a valid key, send a noteOn message.
                if(keyStatus.containsKey(keyCode)){
                    if(keyStatus.get(keyCode).compareAndSet(false, true)){
                        MidiNote note = MidiHelpers.keyCodeToMidi(keyCode);
                        System.out.println("++++" + note.name());
                        synthaxController.noteOn(note, 127);
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
                        synthaxController.noteOff(note);
                    }
                }
            }
        });
    }
    //endregion
}
