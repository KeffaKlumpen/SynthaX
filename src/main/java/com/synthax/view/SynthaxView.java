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
import com.synthax.model.enums.SequencerMode;
import com.synthax.model.enums.Waveforms;
import com.synthax.util.HelperMath;
import com.synthax.util.MidiHelpers;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for synthesizer main view
 * This class contains all the GUI components and their values
 * Handles events when GUI components are interacted with and forwards data
 *
 * @author Axel Nilsson
 * @author Luke Eales
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 * @author Joel Eriksson Sinclair
 */
public class SynthaxView implements Initializable {
    //region FXML variables
    @FXML private VBox oscillatorChainView;
    @FXML private ImageView imgClickToAdd;
    @FXML private Button knobNoiseGain;
    @FXML private ToggleSwitch tglSwitchNoise;
    @FXML private Button knobDelayFeedback;
    @FXML private Button knobDelayTime;
    @FXML private Button knobDelayDecay;
    @FXML private Button knobDelayLevel;
    @FXML private ToggleSwitch tglSwitchDelay;
    @FXML private Button knobReverbSize;
    @FXML private Button knobReverbTone;
    @FXML private Button knobReverbAmount;
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
    @FXML private AnchorPane mainPane = new AnchorPane();
    @FXML private Slider sliderAttack;
    @FXML private Slider sliderDecay;
    @FXML private Slider sliderSustain;
    @FXML private Slider sliderRelease;
    @FXML private Slider sliderMasterGain;
    @FXML private NumberAxis xAxis = new NumberAxis();
    @FXML private NumberAxis yAxis = new NumberAxis();
    @FXML private LineChart<Number, Number> lineChartADSR = new LineChart<Number, Number>(xAxis, yAxis);
    @FXML private Button btnHelp;
    @FXML private Button btnSearchMidiDevice;
    @FXML private Label lblNotConnected;
    @FXML private Label lblConnected;
    @FXML private Button btnSavePreset;
    @FXML private Button btnLoadPreset;
    @FXML private Spinner<String> spinnerPresets;
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
    @FXML private Spinner<String> sequencerMode;
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
    @FXML private Button btnRandomize;
    @FXML private CheckBox cBoXRandomFreq;
    @FXML private CheckBox cBoxRandomOnOff;
    @FXML private CheckBox cBoxRandomGain;
    @FXML private Button btnResetKnobs;
    @FXML private ImageView ricky;
    //endregion

    @FXML private Button samplePlayerStart;


    private final HashMap<KeyCode, AtomicBoolean> keyStatus = new HashMap<>();
    private KnobBehavior bKnobSSRate;
    private Button[] arrSeqFreqKnobs;
    private Button[] arrSeqDetuneKnobs;
    private Button[] arrSeqGainKnobs;
    private Button[] arrEQGainKnobs;
    private Button[] arrEQFreqKnobs;
    private Button[] arrEQRangeKnobs;
    private ToggleButton[] arrSeqStepsOnOff;
    private KnobBehavior[] arrKnobBehaviorGain = new KnobBehavior[16];
    private KnobBehaviorDetune[] arrKnobBehaviorDetune = new KnobBehaviorDetune[16];
    private KnobBehaviorSeqFreq[] arrKnobBehaviorFreq = new KnobBehaviorSeqFreq[16];
    private XYChart.Data<Number, Number> point1ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point2ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point3ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point4ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point5ADSR = new XYChart.Data<>();
    private PopOver popOverHelp;

    private final SynthaxController synthaxController;
    private final int attackMax = 3000;
    private final int decayMax = 1500;
    private final int releaseMax = 2000;
    private int easterCounter = 0;



    public SynthaxView() {
        synthaxController = new SynthaxController(this);
    }

    public void setSequencerStepsOnOff(boolean on, int index) {
        arrSeqStepsOnOff[index].setSelected(on);
    }

    public void setSequencerFreqKnobs(MidiNote note, int index) {
        arrKnobBehaviorFreq[index].setNote(note);
    }

    public void setSequencerGain(float value, int index) {
        arrKnobBehaviorGain[index].setValueRotation(value);
    }

    @FXML
    public void onActionRandomize() {
        synthaxController.randomize(arrSeqStepsOnOff.length);
    }

    /**
     * Adds an oscillator to the GUI and moves the "Add-Oscillator" image to the bottom of the list
     */
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

                if (oscIndex < childList.length - 2) {
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
            oscillatorChainView.getChildren().remove(imgClickToAdd);
            oscillatorChainView.getChildren().add(oscillatorRoot);
            oscillatorChainView.getChildren().add(imgClickToAdd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onActionResetKnobs() {
        synthaxController.sequencerOff();

        if (easterCounter++ != 4) {
            for (int i = 0; i < arrKnobBehaviorGain.length; i++) {
                arrKnobBehaviorFreq[i].setNote(MidiNote.F4);
                arrKnobBehaviorDetune[i].resetKnob();
                arrKnobBehaviorGain[i].setValueRotation(1f);
                arrSeqStepsOnOff[i].setSelected(false);
            }
            SSStartStop.setText("Start");
            SSStartStop.setStyle("-fx-text-fill: #d6d1c9");
            if (ricky != null) {
                ricky.setImage(null);
            }
        } else {
            Image image = new Image(String.valueOf(new File(String.valueOf(MainApplication.class.getResource("Images/Ricky.gif")))));
            ricky = new ImageView(image);
            ricky.setX(10);
            ricky.setY(220);
            mainPane.getChildren().add(ricky);
            SSStartStop.setText("Stop");
            SSStartStop.setStyle("-fx-text-fill: #f78000");
            bKnobSSRate.setValueRotation(0.38f);
            spinnerSteps.increment(16);
            synthaxController.startRickRoll();
            easterCounter = 0;
        }
    }

    public void updateSeqStep(int i, boolean isOn, int velocity, float detuneCent, MidiNote midiNote) {
        Platform.runLater(() -> {
            arrSeqStepsOnOff[i].setSelected(isOn);
            float fVelocity = HelperMath.map(velocity, 0, 127, 0f, 1f);
            arrKnobBehaviorGain[i].setValueRotation(fVelocity);
            arrKnobBehaviorDetune[i].setValueRotation(detuneCent);
            arrKnobBehaviorFreq[i].setNote(midiNote);
        });
    }

    public void fakeSequencerStartStopClick() {
        System.out.println("clicked.");
        Platform.runLater(() -> {
            SSStartStop.onMousePressedProperty().get().handle(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        });
    }

    @FXML
    public void onActionHelp() {
        if (popOverHelp == null || !popOverHelp.isShowing()) {
            ImageView iv = new ImageView(new Image(MainApplication.class.getResource("Images/helpwindowthinA.png").toExternalForm()));
            iv.setFitWidth(900);
            iv.setFitHeight(300);
            popOverHelp = new PopOver(iv);
            popOverHelp.setTitle("");
            popOverHelp.setDetachable(false);
            popOverHelp.setHeaderAlwaysVisible(true);
            popOverHelp.show(btnHelp);
            popOverHelp.getRoot().getStylesheets().add(MainApplication.class.getResource("skins.css").toExternalForm());
        }
    }

    @FXML
    public void onActionSearchMidiDevice() {
        lblNotConnected.setVisible(!lblNotConnected.isVisible());
        lblConnected.setVisible(!lblConnected.isVisible());
    }

    public void setUpSteps(int x) {
        Platform.runLater(() -> {
            if (x == 0) {
                for (int i = 0; i < arrKnobBehaviorGain.length; i++) {
                    arrKnobBehaviorDetune[i].resetKnob();
                    arrKnobBehaviorGain[i].setValueRotation(1f);
                    arrSeqStepsOnOff[i].setSelected((i <= 4) || (i == 7 || i == 10));
                }
                arrKnobBehaviorFreq[0].setNote(MidiNote.A3);
                arrKnobBehaviorFreq[1].setNote(MidiNote.B3);
                arrKnobBehaviorFreq[2].setNote(MidiNote.D4);
                arrKnobBehaviorFreq[3].setNote(MidiNote.B3);
                arrKnobBehaviorFreq[4].setNote(MidiNote.Gb5);
                arrKnobBehaviorFreq[7].setNote(MidiNote.Gb5);
                arrKnobBehaviorFreq[10].setNote(MidiNote.E5);
            } else if (x == 1) {
                arrKnobBehaviorFreq[4].setNote(MidiNote.E5);
                arrKnobBehaviorFreq[7].setNote(MidiNote.E5);
                arrKnobBehaviorFreq[10].setNote(MidiNote.D5);
                arrKnobBehaviorFreq[11].setNote(MidiNote.Db5);
                arrKnobBehaviorFreq[12].setNote(MidiNote.B4);
                arrSeqStepsOnOff[11].setSelected(true);
                arrSeqStepsOnOff[12].setSelected(true);
            } else if (x == 2) {
                arrSeqStepsOnOff[11].setSelected(false);
                arrSeqStepsOnOff[12].setSelected(false);
                arrSeqStepsOnOff[14].setSelected(true);
                arrKnobBehaviorFreq[4].setNote(MidiNote.D5);
                arrKnobBehaviorFreq[10].setNote(MidiNote.Db5);
                arrKnobBehaviorFreq[14].setNote(MidiNote.A3);
            } else if (x == 3) {
                for (int i = 0; i < arrKnobBehaviorFreq.length; i++) {
                    arrSeqStepsOnOff[i].setSelected((i == 2) || (i >= 4 && i < 8) || (i >= 8));
                }
                arrKnobBehaviorFreq[2].setNote(MidiNote.A3);
                for (int i = 4; i < 8; i++) {
                    arrKnobBehaviorFreq[i].setNote(MidiNote.E4);
                }
                for (int i = 8; i < arrKnobBehaviorFreq.length; i++) {
                    arrKnobBehaviorFreq[i].setNote(MidiNote.D4);
                }
            }
        });
    }

    public void setSeqButtonOrange(int i) {
        Platform.runLater(() -> {
            arrSeqStepsOnOff[i].setStyle("-fx-background-color: #f78000");
        });
    }

    public void setSeqButtonGray(int i) {
        Platform.runLater(()-> {
            arrSeqStepsOnOff[i].setStyle("-fx-background-color: #78736b");

        });
    }

    /**
     * Initialize method
     * Sets values, behaviour and adds listeners to GUI components
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        samplePlayerStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view/sampleplayer-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Johnny Trummas trummaskin, går på smör o margarin");
                    stage.setScene(scene);
                    stage.show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
        initStepSequencer();
        initMasterGain();
        onActionAddOscillator();
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
    //endregion Methods for updating linechart

    //region initialize methods (click to open/collapse)

    private void initMasterGain() {
        sliderMasterGain.valueProperty().addListener((observableValue, number, t1) -> synthaxController.setMasterGain(t1.floatValue()));
    }

    private void initFilterArrays() {
        arrEQFreqKnobs = new Button[] {
                knobEQ1Freq,
                knobEQ2Freq,
                knobEQ3Freq};
        arrEQGainKnobs = new Button[] {
                knobEQ1Gain,
                knobEQ2Gain,
                knobEQ3Gain};
        arrEQRangeKnobs = new Button[] {
                knobEQ1Range,
                knobEQ2Range,
                knobEQ3Range};
    }

    private void initSequencerArrays() {
        arrSeqStepsOnOff = new ToggleButton[] {
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
        arrSeqFreqKnobs = new Button[] {
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
        arrSeqDetuneKnobs = new Button[] {
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
        arrSeqGainKnobs = new Button[] {
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
    private void initStepSequencer() {
        for (int i = 0; i < arrSeqFreqKnobs.length; i++) {
            int finali = i;
            arrKnobBehaviorFreq[i] = new KnobBehaviorSeqFreq(arrSeqFreqKnobs[i], MidiNote.F4);
            arrSeqFreqKnobs[i].setOnMouseDragged(arrKnobBehaviorFreq[i]);
            arrKnobBehaviorFreq[i].knobValueProperty().addListener((v, oldValue, newValue) -> {
                if (arrSeqStepsOnOff[finali].isSelected()) {
                    arrSeqStepsOnOff[finali].textProperty().setValue(arrKnobBehaviorFreq[finali].getNoteName());
                }
                synthaxController.setSeqMidiNote(finali, MidiNote.values()[newValue.intValue()]);
            });
        }

        for (int i = 0; i < arrSeqDetuneKnobs.length; i++) {
            int finali = i;
            arrKnobBehaviorDetune[i] = new KnobBehaviorDetune(arrSeqDetuneKnobs[i]);
            arrSeqDetuneKnobs[i].setOnMouseDragged(arrKnobBehaviorDetune[i]);
            arrKnobBehaviorDetune[i].knobValueProperty().addListener((v, oldValue, newValue) -> {
                synthaxController.setSeqDetuneCent(finali, newValue.floatValue());
            });
        }

        for (int i = 0; i < arrSeqGainKnobs.length; i++) {
            int finalI = i;
            arrKnobBehaviorGain[i] = new KnobBehavior(arrSeqGainKnobs[i]);
            arrKnobBehaviorGain[i].setValueRotation(1.0f);
            arrSeqGainKnobs[i].setOnMouseDragged(arrKnobBehaviorGain[i]);
            arrKnobBehaviorGain[i].knobValueProperty().addListener((v, oldValue, newValue) -> {
                synthaxController.setSeqVelocity(finalI, newValue.floatValue());
            });
        }

        bKnobSSRate = new KnobBehavior(knobSSRate);
        bKnobSSRate.setValueRotation(0.5f);
        knobSSRate.setOnMouseDragged(bKnobSSRate);
        bKnobSSRate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqBPM(newValue.floatValue());
        });

        for (int i = 0; i < arrSeqStepsOnOff.length; i++) {
            int finalI = i;
            arrSeqStepsOnOff[i].selectedProperty().addListener((v, oldValue, newValue) -> {
                if (newValue) {
                    arrSeqStepsOnOff[finalI].textProperty().setValue(arrKnobBehaviorFreq[finalI].getNoteName());
                } else {
                    arrSeqStepsOnOff[finalI].textProperty().setValue("Off");
                }
                synthaxController.setStepOnOff(finalI, newValue);
            });
        }
        SSStartStop.setOnMousePressed(l -> {
            if (!synthaxController.sequencerIsRunning()) {
                synthaxController.sequencerOn();
                SSStartStop.setText("Stop");
                SSStartStop.setStyle("-fx-text-fill: #f78000");
            } else {
                synthaxController.sequencerOff();
                SSStartStop.setText("Start");
                SSStartStop.setStyle("-fx-text-fill: #d6d1c9");
                if (ricky != null) {
                    ricky.setImage(null);
                }
            }
        });
        SpinnerValueFactory<Integer> spf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,16,16);
        spinnerSteps.setValueFactory(spf);
        spinnerSteps.valueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqNSteps(newValue);
        });

        SpinnerValueFactory<String> spfMode = new SpinnerValueFactory.ListSpinnerValueFactory<String>(SequencerMode.getNames());
        spfMode.setWrapAround(true);
        sequencerMode.setValueFactory(spfMode);
        sequencerMode.valueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSequencerMode(SequencerMode.getMode(newValue));
        });
        cBoXRandomFreq.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setRandomFreq(newValue);
        });
        cBoxRandomGain.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setRandomGain(newValue);
        });
        cBoxRandomOnOff.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setRandomOnOff(newValue);
        });

    }

    private void initNoise() {
        tglSwitchNoise.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setNoiseActive(newValue);
        });

        KnobBehavior bKnobNoiseGain = new KnobBehavior(knobNoiseGain);
        knobNoiseGain.setOnMouseDragged(bKnobNoiseGain);
        bKnobNoiseGain.setValueRotation(0.5f);
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

        KnobBehavior bKnobReverbAmount = new KnobBehavior(knobReverbAmount);
        knobReverbAmount.setOnMouseDragged(bKnobReverbAmount);
        bKnobReverbAmount.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setReverbAmount(newValue.floatValue());
        });
    }

    private void initLFO() {
        tglSwitchLFO.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setLFOActive(newValue);
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
            synthaxController.setEQActive(0, newValue);
        });
        tglSwitchEQ2.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setEQActive(1, newValue);
        });
        tglSwitchEQ3.selectedProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setEQActive(2, newValue);
        });

        for (int i = 0; i < arrEQGainKnobs.length; i++) {
            int finalI = i;
            KnobBehaviorDetune b = new KnobBehaviorDetune(arrEQGainKnobs[i]);
            arrEQGainKnobs[i].setOnMouseDragged(b);
            b.knobValueProperty().addListener((v, oldValue, newValue) -> {
                synthaxController.setEQGain(finalI, newValue.floatValue());
            });
        }
        for (int i = 0; i < arrEQRangeKnobs.length; i++) {
            int finalI = i;
            KnobBehavior b = new KnobBehavior(arrEQRangeKnobs[i]);
            arrEQRangeKnobs[i].setOnMouseDragged(b);
            b.knobValueProperty().addListener((v, oldValue, newValue) -> {

                synthaxController.setEQRange(finalI, newValue.floatValue());
            });
        }
        for (int i = 0; i < arrEQFreqKnobs.length; i++) {
            int finalI = i;
            KnobBehavior b = new KnobBehavior(arrEQFreqKnobs[i]);
            arrEQFreqKnobs[i].setOnMouseDragged(b);
            b.knobValueProperty().addListener((v, oldValue, newValue) -> {
                synthaxController.setEQFreq(finalI, newValue.floatValue());
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
                    synthaxController.noteOn(note, 127);
                }
            }
        });
        mainPane.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            if (keyStatus.containsKey(keyCode)) {
                if (keyStatus.get(keyCode).compareAndSet(true, false)) {
                    MidiNote note = MidiHelpers.keyCodeToMidi(keyCode);
                    synthaxController.noteOff(note);
                }
            }
        });
    }
    //endregion initialize methods
}
