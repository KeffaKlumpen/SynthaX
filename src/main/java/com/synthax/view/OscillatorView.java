package com.synthax.view;

import com.synthax.controller.OscillatorController;
import com.synthax.controller.OscillatorVoice;
import com.synthax.model.controls.KnobBehavior;
import com.synthax.model.controls.KnobBehaviorDetune;
import com.synthax.model.controls.KnobBehaviorWave;
import com.synthax.model.enums.CombineMode;
import com.synthax.model.enums.OctaveOperands;
import com.synthax.model.enums.Waveforms;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import org.controlsfx.control.SegmentedButton;
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
        oscillatorController = new OscillatorController();
    }

    public OscillatorController getOscillatorController() {
        return oscillatorController;
    }

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
        initOnOff();
        initOctaveSpinner();
    }

    private void initCombineModeButtons() {
        tglBtnCombineAdd.setSelected(true);
        oscillatorController.setOutputType(CombineMode.ADD);
        System.out.println("ADD");

        tglBtnCombineAdd.setOnAction(actionEvent -> {
            oscillatorController.setOutputType(CombineMode.ADD);
            System.out.println("ADD");
        });

        tglBtnCombineMult.setOnAction(actionEvent -> {
            oscillatorController.setOutputType(CombineMode.MULT);
            System.out.println("MULT");
        });
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

    private void initOctaveSpinner() {
        SpinnerValueFactory<OctaveOperands> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(OctaveOperands.values()));
        valueFactory.setValue(OctaveOperands.EIGHT);
        octaveSpinner.setValueFactory(valueFactory);
        octaveSpinner.valueProperty().addListener((observableValue, octaveOperands, t1) -> {
            oscillatorController.setOctaveOperand(t1);
        });
    }

    private void initOnOff() {
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
        behaviorKnobGain.setValueRotation(0.5f);
        behaviorKnobGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.setGain(newValue.floatValue());
        });
    }
}
