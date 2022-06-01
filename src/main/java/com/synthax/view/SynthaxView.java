package com.synthax.view;

import com.synthax.view.controls.KnobBehavior;

import com.synthax.view.controls.KnobBehaviorDetune;
import com.synthax.view.controls.KnobBehaviorWave;
import com.synthax.MainApplication;
import com.synthax.controller.OscillatorController;
import com.synthax.controller.SynthaxController;
import com.synthax.model.effects.SynthaxADSR;
import com.synthax.view.controls.KnobBehaviorSeqFreq;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.SequencerMode;
import com.synthax.model.enums.Waveforms;
import com.synthax.util.HelperMath;
import com.synthax.util.MidiHelpers;

import com.synthax.view.utils.Dialogs;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
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
    @FXML private AnchorPane mainPane = new AnchorPane();

    @FXML private VBox oscillatorChainView;
    @FXML private ImageView imgClickToAddOsc;

    @FXML private Button knobNoiseGain;

    //region Delay variables
    @FXML private Button knobDelayFeedback;
    @FXML private Button knobDelayTime;
    @FXML private Button knobDelayDecay;
    @FXML private Button knobDelayLevel;
    //endregion Delay variables

    @FXML private Button knobReverbSize;
    @FXML private Button knobReverbTone;
    @FXML private Button knobReverbAmount;

    @FXML private Button knobLFODepth;
    @FXML private Button knobLFORate;
    @FXML private Button knobLFOWaveForm;

    @FXML private Button knobFilterHPCutoff;
    @FXML private Button knobFilterLPCutoff;

    //region EQ variables
    @FXML private Button knobEQ1Gain;
    @FXML private Button knobEQ1Freq;
    @FXML private Button knobEQ1Range;
    @FXML private Button knobEQ2Gain;
    @FXML private Button knobEQ2Freq;
    @FXML private Button knobEQ2Range;
    @FXML private Button knobEQ3Gain;
    @FXML private Button knobEQ3Freq;
    @FXML private Button knobEQ3Range;
    //endregion EQ variables

    //region ADSR variables
    @FXML private Slider sliderAttack;
    @FXML private Slider sliderDecay;
    @FXML private Slider sliderSustain;
    @FXML private Slider sliderRelease;
    @FXML private Slider sliderMasterGain;
    @FXML private NumberAxis xAxis = new NumberAxis();
    @FXML private NumberAxis yAxis = new NumberAxis();
    @FXML private LineChart<Number, Number> lineChartADSR = new LineChart<Number, Number>(xAxis, yAxis);
    //endregion ADSR variables

    //region Controls below logo
    @FXML private Button btnHelp;
    @FXML private Button btnSettings;
    @FXML private Label lblNotConnected;
    @FXML private Label lblConnected;
    //endregion Controls below logo
    //endregion FXML variables

    //region Step Sequencer
    @FXML private Button SSStartStop;
    @FXML private Button knobSSRate;
    @FXML private Spinner<Integer> spinnerSteps;
    @FXML private Spinner<String> sequencerMode;
    @FXML private CheckBox cBoXRandomFreq;
    @FXML private CheckBox cBoxRandomOnOff;
    @FXML private CheckBox cBoxRandomGain;
    @FXML private ComboBox<String> cmbPresets;

    //region Step Sequencer Steps
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
    //endregion Step Sequencer Steps

    @FXML private ImageView ricky;
    //endregion Step Sequencer

    private final HashMap<String, AtomicBoolean> keyStatus = new HashMap<>();

    //region SamplePlayer Variables
    @FXML private Button samplePlayerStart;
    private SamplePlayerView samplePlayerView;
    //endregion

    //region Step Sequencer collections
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
    //endregion Step Sequencer collections

    //region ADSR graphics points
    private XYChart.Data<Number, Number> point1ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point2ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point3ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point4ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point5ADSR = new XYChart.Data<>();
    //endregion ADSR graphics points

    private PopOver popOverHelp;
    private PopOver popOverSettings;

    private final SynthaxController synthaxController;

    private int easterCounter = 0;

    public SynthaxView() {
        synthaxController = new SynthaxController(this);
    }

    public void setSequencerStepOnOff(boolean on, int index) {
        arrSeqStepsOnOff[index].setSelected(on);
    }

    public void setSequencerStepFreq(MidiNote note, int index) {
        arrKnobBehaviorFreq[index].setNote(note);
    }

    public void setSequencerStepGain(float value, int index) {
        arrKnobBehaviorGain[index].setRotation(value);
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

    public void setSequencerPresetList(String[] presetNames) {
        setSequencerPresetList(presetNames, "");
    }

    public void setSequencerPresetList(String[] presetNames, String chosenPreset) {
        Platform.runLater(() -> {
            cmbPresets.setItems(FXCollections.observableList(Arrays.stream(presetNames).toList()));
            if (chosenPreset.equals("")) {
                cmbPresets.getSelectionModel().selectFirst();
            } else {
                cmbPresets.getSelectionModel().select(chosenPreset);
            }
        });
    }

    public void updateSequencerRate(float value) {
        bKnobSSRate.setRotation(value);
    }

    public void updateMidiLabel(boolean visable) {
        Platform.runLater(() -> {
            if (visable) {
                lblConnected.setVisible(true);
                lblNotConnected.setVisible(false);
            } else {
                lblConnected.setVisible(false);
                lblNotConnected.setVisible(true);
            }
        });
    }

    //region onAction  (click to open/collapse)
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
            oscillatorChainView.getChildren().remove(imgClickToAddOsc);
            oscillatorChainView.getChildren().add(oscillatorRoot);
            oscillatorChainView.getChildren().add(imgClickToAddOsc);
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
                arrKnobBehaviorGain[i].setRotation(1f);
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
            bKnobSSRate.setRotation(0.38f);
            spinnerSteps.increment(16);
            synthaxController.startRickRoll();
            easterCounter = 0;
        }
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
    public void onActionSettings() {
        if (popOverSettings == null || !popOverSettings.isShowing()) {
            try {
                URL fxmlLocation = MainApplication.class.getResource("view/Settings-view.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
                Node settingsRoot = fxmlLoader.load();
                SettingsView settingsView = fxmlLoader.getController();
                settingsView.populatePresetsBox(synthaxController.getSequencerPresetList(), this);
                popOverSettings = new PopOver(settingsRoot);
                popOverSettings.setTitle("Settings");
                popOverSettings.setDetachable(false);
                popOverSettings.setHeaderAlwaysVisible(true);
                popOverSettings.show(btnSettings);
                popOverSettings.getStyleClass().add("popoverSettings");
                popOverSettings.getRoot().getStylesheets().add(MainApplication.class.getResource("skins.css").toExternalForm());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onActionSearchMidiDevice() {
        if (synthaxController.connectMidi()) {
            lblConnected.setVisible(true);
            lblNotConnected.setVisible(false);
        } else {
            lblConnected.setVisible(false);
            lblNotConnected.setVisible(true);
        }
    }

    @FXML
    public void onActionNoiseBypass() {
        synthaxController.setNoiseActive();
    }

    @FXML
    public void onActionLFOBypass() {
        synthaxController.setLFOActive();
    }

    @FXML
    public void onActionReverbBypass() {
        synthaxController.setReverbActive();
    }

    @FXML
    public void onActionDelayBypass() {
        synthaxController.setDelayActive();
    }

    @FXML
    public void onActionEQ1Bypass() {
        synthaxController.setEQActive(0);
    }

    @FXML
    public void onActionEQ2Bypass() {
        synthaxController.setEQActive(1);
    }

    @FXML
    public void onActionEQ3Bypass() {
        synthaxController.setEQActive(2);
    }

    @FXML
    public void onActionHPBypass() {
        synthaxController.setHPActive();
    }

    @FXML
    public void onActionLPBypass() {
        synthaxController.setLPActive();
    }

    @FXML
    public void onActionSavePreset() {
        showSavePresetDialog();
    }
    //endregion onAction

    //region forwarding from SettingsView (click to open/collapse)
    public void deletePreset(String text) {
        synthaxController.deletePreset(text);
    }

    public void updateSequencerPresetList() {
        synthaxController.updateSequencerPresetList();
    }

    public void setOscVoiceCount(int voiceCount) {
        synthaxController.setOscVoiceCount(voiceCount);
    }


    public void setOscMonophonic() {
        synthaxController.setMonophonic();
    }
    //endregion forwarding from SettingsView

    public void updateSeqStep(int i, boolean isOn, int velocity, float detuneCent, MidiNote midiNote) {
        Platform.runLater(() -> {
            arrSeqStepsOnOff[i].setSelected(isOn);
            float fVelocity = HelperMath.map(velocity, 0, 127, 0f, 1f);
            arrKnobBehaviorGain[i].setRotation(fVelocity);
            arrKnobBehaviorDetune[i].setRotation(detuneCent);
            arrKnobBehaviorFreq[i].setNote(midiNote);
        });
    }

    public void setSeqKnobRate(float rate) {
        bKnobSSRate.setRotation(rate);
    }

    public void setSequencerMode(SequencerMode mode) {
        sequencerMode.getValueFactory().setValue(mode.getName());
    }

    public void setSequencerNSteps(int nSteps) {
        spinnerSteps.getValueFactory().setValue(nSteps);
    }

    public void fakeSequencerStartStopClick() {
        Platform.runLater(() -> {
            SSStartStop.onMousePressedProperty().get().handle(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        });
    }

    public void setUpSteps(int x) {
        Platform.runLater(() -> {
            if (x == 0) {
                for (int i = 0; i < arrKnobBehaviorGain.length; i++) {
                    arrKnobBehaviorDetune[i].resetKnob();
                    arrKnobBehaviorGain[i].setRotation(1f);
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

    public void forceStartSequencer() {
        if (!synthaxController.sequencerIsRunning()) {
            synthaxController.sequencerOn();
            SSStartStop.setText("Stop");
            SSStartStop.setStyle("-fx-text-fill: #f78000");
        } else {
            synthaxController.sequencerOff();
            synthaxController.sequencerOn();
        }
    }

    public void forceStopSequencer() {
        synthaxController.sequencerOff();
        SSStartStop.setText("Start");
        SSStartStop.setStyle("-fx-text-fill: #d6d1c9");
    }
    
    public boolean sequencerIsRunning() {
        return synthaxController.sequencerIsRunning();
    }


    /**
     * Initialize method
     * Sets values, behaviour and adds listeners to GUI components
     */
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
        initStepSequencer();
        initMasterGain();
        initSamplePlayer();
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
        point2ADSR.setXValue((sliderAttack.getValue() / SynthaxADSR.ATTACK_MAX) * 10d);
        point3ADSR.setXValue((sliderAttack.getValue() / SynthaxADSR.ATTACK_MAX) * 10d +
                (sliderDecay.getValue() / SynthaxADSR.DECAY_MAX) * 10d);
    }

    private void onDecayDrag() {
        point3ADSR.setXValue((sliderAttack.getValue() / SynthaxADSR.ATTACK_MAX) * 10d +
                (sliderDecay.getValue() / SynthaxADSR.DECAY_MAX) * 10d);
    }

    private void onSustainDrag() {
        point3ADSR.setYValue(sliderSustain.getValue() * 30d);
        point4ADSR.setYValue(sliderSustain.getValue() * 30d);
    }

    private void onReleaseDrag() {
        point4ADSR.setXValue(40d - (sliderRelease.getValue() / SynthaxADSR.RELEASE_MAX) * 10d);
    }
    //endregion Methods for updating linechart

    //region initialize methods (click to open/collapse)

    private void initMasterGain() {
        sliderMasterGain.valueProperty().addListener((observableValue, number, t1) -> synthaxController.setMasterGain(t1.floatValue()));
    }

    private void initSamplePlayer() {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view/sampleplayer-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("skins.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("SynthaX Sample Player");
            stage.setScene(scene);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    Scene scene = samplePlayerView.getScene();
                    Stage stage = (Stage) scene.getWindow();
                    stage.hide();
                }
            });
            samplePlayerView = fxmlLoader.getController();
            samplePlayerView.setSynthaxView(this);
            samplePlayerView.setRate(bKnobSSRate.knobValueProperty().floatValue());
        } catch (IOException e) {
            e.printStackTrace();
        }

        samplePlayerStart.setOnAction(l -> {
            Scene scene = samplePlayerView.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.toFront();
            stage.show();
            samplePlayerView.setAllGainValues();
        });
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
            arrKnobBehaviorGain[i].setRotation(1.0f);
            arrSeqGainKnobs[i].setOnMouseDragged(arrKnobBehaviorGain[i]);
            arrKnobBehaviorGain[i].knobValueProperty().addListener((v, oldValue, newValue) -> {
                synthaxController.setSeqVelocity(finalI, newValue.floatValue());
            });
        }

        bKnobSSRate = new KnobBehavior(knobSSRate);
        bKnobSSRate.setRotation(0.5f);
        knobSSRate.setOnMouseDragged(bKnobSSRate);
        bKnobSSRate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setSeqBPM(newValue.floatValue());
            if (samplePlayerView.getSyncSelected()) {
                samplePlayerView.setRate(newValue.floatValue());
            }
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
            boolean synced = samplePlayerView.getSyncSelected();
            if (!synthaxController.sequencerIsRunning()) {
                if (samplePlayerView.getSequencerIsRunning() && synced) {
                    samplePlayerView.stopSequencer();
                }

                try {
                    Thread.sleep(280);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (synced) {
                    samplePlayerView.startSequencer();
                }
                synthaxController.sequencerOn();
                SSStartStop.setText("Stop");
                SSStartStop.setStyle("-fx-text-fill: #f78000");
            } else {
                synthaxController.sequencerOff();
                SSStartStop.setText("Start");
                SSStartStop.setStyle("-fx-text-fill: #d6d1c9");
                if (synced) {
                    samplePlayerView.stopSequencer();
                }
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

        initStepSequencerPresetButtons();
    }

    private void initStepSequencerPresetButtons() {
        synthaxController.updateSequencerPresetList();

        cmbPresets.setOnAction(actionEvent -> {
            synthaxController.onSelectPreset(cmbPresets.getValue());
        });
    }

    private void showSavePresetDialog() {
        String currentPresetName = cmbPresets.getValue();
        // show name selection pop-up
        String presetName = Dialogs.getTextInput("Preset Name", "Preset Name:", currentPresetName);
        if(presetName != null && !presetName.equals("")) {
            synthaxController.onSavePreset(presetName);
        }
    }

    public void showPresetSaveConflictPopup(String presetName) {
        int choice = Dialogs.getConfirmationYesNoCancel("Preset Exists", "Preset already exists, do you want to overwrite?");

        // I wan't a cancel state, give me int (0, 1, 2) instead of boolean.
        if(choice == Dialogs.YES_OPTION) {
            synthaxController.savePreset(presetName);
        }
        else if(choice == Dialogs.NO_OPTION) {
            synthaxController.savePresetAsNew(presetName);
        }
    }

    private void initNoise() {
        KnobBehavior bKnobNoiseGain = new KnobBehavior(knobNoiseGain);
        knobNoiseGain.setOnMouseDragged(bKnobNoiseGain);
        bKnobNoiseGain.setRotation(0.5f);
        bKnobNoiseGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            synthaxController.setNoiseGain(newValue.floatValue());
        });
    }

    private void initDelay() {
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

        sliderAttack.setMax(SynthaxADSR.ATTACK_MAX);
        sliderAttack.setMin(SynthaxADSR.ATTACK_MIN);
        sliderAttack.valueProperty().addListener((observableValue, number, newValue) -> {
            SynthaxADSR.setAttackValue(newValue.floatValue());
            onAttackDrag();
        });

        sliderDecay.setMax(SynthaxADSR.DECAY_MAX);
        sliderDecay.setMin(SynthaxADSR.DECAY_MIN);
        sliderDecay.valueProperty().addListener((observableValue, number, newValue) -> {
            SynthaxADSR.setDecayValue(newValue.floatValue());
            onDecayDrag();
        });

        sliderSustain.setMax(SynthaxADSR.SUSTAIN_MAX);
        sliderSustain.setValue(SynthaxADSR.getSustainValue());
        sliderSustain.valueProperty().addListener((observableValue, number, newValue) -> {
            SynthaxADSR.setSustainValue(newValue.floatValue());
            onSustainDrag();
        });

        sliderRelease.setMax(SynthaxADSR.RELEASE_MAX);
        sliderRelease.setMin(SynthaxADSR.RELEASE_MIN);
        sliderRelease.valueProperty().addListener((observableValue, number, newValue) -> {
            SynthaxADSR.setReleaseValue(newValue.floatValue());
            onReleaseDrag();
        });
    }

    private void initKeyHash() {
        keyStatus.put("a", new AtomicBoolean(false));
        keyStatus.put("w", new AtomicBoolean(false));
        keyStatus.put("s", new AtomicBoolean(false));
        keyStatus.put("e", new AtomicBoolean(false));
        keyStatus.put("d", new AtomicBoolean(false));
        keyStatus.put("f", new AtomicBoolean(false));
        keyStatus.put("t", new AtomicBoolean(false));
        keyStatus.put("g", new AtomicBoolean(false));
        keyStatus.put("y", new AtomicBoolean(false));
        keyStatus.put("h", new AtomicBoolean(false));
        keyStatus.put("u", new AtomicBoolean(false));
        keyStatus.put("j", new AtomicBoolean(false));
        keyStatus.put("k", new AtomicBoolean(false));
        keyStatus.put("l", new AtomicBoolean(false));
        keyStatus.put("ö", new AtomicBoolean(false));
        keyStatus.put("ä", new AtomicBoolean(false));
        keyStatus.put("o", new AtomicBoolean(false));
        keyStatus.put("p", new AtomicBoolean(false));
    }

    private void initKeyBoardListeners() {
        mainPane.setOnKeyPressed(event -> {
            String code = event.getText().toLowerCase();
            /*if (code.equals(" ")) {
                fakeSequencerStartStopClick();
            }
             */
            if (keyStatus.containsKey(code)) {
                if (keyStatus.get(code).compareAndSet(false, true)) {
                    MidiNote note = MidiHelpers.stringToMidi(code);
                    synthaxController.noteOn(note, 127);
                }
            }
        });
        mainPane.setOnKeyReleased(event -> {
            String code = event.getText().toLowerCase();
            if (keyStatus.containsKey(code)) {
                if (keyStatus.get(code).compareAndSet(true, false)) {
                    MidiNote note = MidiHelpers.stringToMidi(code);
                    synthaxController.noteOff(note);
                }
            }
        });
    }
    //endregion initialize methods
}
