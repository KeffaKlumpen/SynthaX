package com.synthax.view;

import com.synthax.controller.OscillatorController;
import com.synthax.controller.VoiceController;
import com.synthax.view.controls.KnobBehavior;
import com.synthax.view.controls.KnobBehaviorDetune;
import com.synthax.view.controls.KnobBehaviorWave;
import com.synthax.model.enums.CombineMode;
import com.synthax.model.enums.OctaveOperands;
import com.synthax.model.enums.Waveforms;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Handles GUI-communication for OscillatorController
 * @author Joel Eriksson Sinclair
 * @author Teodor Wegestål
 * @author Viktor Lenberg
 * @author Axel Nilsson
 * @author Luke Eales
 */
public class OscillatorView implements Initializable {
    @FXML private ToggleButton tglBtnCombineAdd;
    @FXML private ToggleButton tglBtnCombineMult;
    @FXML private ToggleSwitch tglSwitchOscillatorOnOff;
    @FXML private Button btnMoveUp;
    @FXML private Button btnMoveDown;
    @FXML private Button btnRemoveOscillator;
    @FXML private Button knobGain = new Button();
    @FXML private Button knobWave = new Button();
    @FXML private Button knobDetune = new Button();
    @FXML private Button knobLFODepth = new Button();
    @FXML private Button knobLFORate = new Button();
    @FXML private Spinner<OctaveOperands> octaveSpinner = new Spinner<>();

    private final OscillatorController oscillatorController;

    public OscillatorView() {
        oscillatorController = new OscillatorController(VoiceController.VOICE_COUNT);
    }

    public OscillatorController getOscillatorController() {
        return oscillatorController;
    }

    public Button getBtnRemoveOscillator() {
        return btnRemoveOscillator;
    }

    public Button getBtnMoveDown(){
        return btnMoveDown;
    }

    public Button getBtnMoveUp(){
        return btnMoveUp;
    }

    //region Init methods (click to open/collapse)
    /**
     * initialize-method for the oscillator class
     * Sets values, behaviour and adds listeners to GUI components
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCombineModeButtons();
        initGainKnob();
        initDetuneKnob();
        initLFOKnobs();
        initWaveFormKnob();
        initOnOffSwitch();
        initOctaveSpinner();
    }

    private void initCombineModeButtons() {
        tglBtnCombineAdd.setSelected(true);
        oscillatorController.setOutputType(CombineMode.ADD);
        tglBtnCombineAdd.setOnAction(actionEvent -> {
            if (!tglBtnCombineAdd.isSelected() && !tglBtnCombineMult.isSelected()) {
                tglBtnCombineAdd.setSelected(true);
            }
            oscillatorController.setOutputType(CombineMode.ADD);
        });
        tglBtnCombineMult.setOnAction(actionEvent -> {
            if (!tglBtnCombineAdd.isSelected() && !tglBtnCombineMult.isSelected()) {
                tglBtnCombineMult.setSelected(true);
            }
            oscillatorController.setOutputType(CombineMode.MULT);
        });
    }

    private void initOctaveSpinner() {
        SpinnerValueFactory<OctaveOperands> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(OctaveOperands.values()));
        valueFactory.setValue(OctaveOperands.EIGHT);
        octaveSpinner.setValueFactory(valueFactory);
        octaveSpinner.valueProperty().addListener((observableValue, octaveOperands, newValue) -> {
            oscillatorController.setOctaveOperand(newValue);
        });
    }

    private void initOnOffSwitch() {
        tglSwitchOscillatorOnOff.setSelected(true);
        tglSwitchOscillatorOnOff.selectedProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.bypassOscillator(newValue);
        });
    }

    private void initWaveFormKnob() {
        KnobBehaviorWave behaviorKnobWave = new KnobBehaviorWave(knobWave);
        knobWave.setOnMouseDragged(behaviorKnobWave);
        behaviorKnobWave.knobValueProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.setWaveform(Waveforms.values()[newValue.intValue()]);
        });
    }

    private void initLFOKnobs() {
        KnobBehavior behaviorKnobLFOdepth = new KnobBehavior(knobLFODepth);
        knobLFODepth.setOnMouseDragged(behaviorKnobLFOdepth);
        behaviorKnobLFOdepth.knobValueProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.setLFODepth(newValue.floatValue());
        });

        KnobBehavior behaviorKnobLFOrate = new KnobBehavior(knobLFORate);
        knobLFORate.setOnMouseDragged(behaviorKnobLFOrate);
        behaviorKnobLFOrate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.setLFORate(newValue.floatValue());
        });
    }

    private void initDetuneKnob() {
        KnobBehaviorDetune behaviorKnobDetune = new KnobBehaviorDetune(knobDetune);
        knobDetune.setOnMouseDragged(behaviorKnobDetune);
        behaviorKnobDetune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.setDetuneCent(newValue.floatValue());
        } );
    }

    private void initGainKnob() {
        KnobBehavior behaviorKnobGain = new KnobBehavior(knobGain);
        knobGain.setOnMouseDragged(behaviorKnobGain);
        behaviorKnobGain.setRotation(0.5f); // Sets the knob to half, to match the initial value of MasterGain.
        behaviorKnobGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.setGain(newValue.floatValue());
        });
    }
    //endregion Init methods
}
