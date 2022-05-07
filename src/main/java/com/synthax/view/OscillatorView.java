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
 */
public class OscillatorView implements Initializable {
    @FXML private ToggleButton tglBtnCombineAdd;
    @FXML private ToggleButton tglBtnCombineSub;
    @FXML private ToggleButton tglBtnCombineMult;
    @FXML private SegmentedButton segBtnCombineMode;
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

    //region GUI stuff
    /**
     * initialize-method for the oscillator class
     * Sets values and adds listeners to GUI components
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     * @author Joel Eriksson Sinclair
     * @author Luke Eales
     * @author Axel Nilsson
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
    /**
     * Sets behaviour for toggle buttons for the combine modes
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     */
    private void initCombineModeButtons() {
        tglBtnCombineAdd.setSelected(true);
        oscillatorController.setOutputType(CombineMode.ADD);
        System.out.println("ADD");

        tglBtnCombineAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                oscillatorController.setOutputType(CombineMode.ADD);
                System.out.println("ADD");
            }
        });

        tglBtnCombineMult.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                oscillatorController.setOutputType(CombineMode.MULT);
                System.out.println("MULT");
            }
        });

        tglBtnCombineSub.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.err.println("NOT IMPLEMENTED");
            }
        });
    }

    /**
     * @return The button responsible for removing the oscillator.
     * @author Joel Eriksson Sinclair
     */
    public Button getBtnRemoveOscillator() {
        return btnRemoveOscillator;
    }

    /**
     * @return
     * @author Joel Eriksson Sinclair
     */
    public Button getBtnMoveDown(){
        return btnMoveDown;
    }

    /**
     * @return
     * @author Joel Eriksson Sinclair
     */
    public Button getBtnMoveUp(){
        return btnMoveUp;
    }

    /**
     * Sets behaviour for octave spinner
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     */
    private void initOctaveSpinner() {
        SpinnerValueFactory<OctaveOperands> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(OctaveOperands.values()));
        valueFactory.setValue(OctaveOperands.EIGHT);
        octaveSpinner.setValueFactory(valueFactory);
        octaveSpinner.valueProperty().addListener(new ChangeListener<OctaveOperands>() {
            @Override
            public void changed(ObservableValue<? extends OctaveOperands> observableValue, OctaveOperands octaveOperands, OctaveOperands t1) {
                oscillatorController.setOctaveOperand(t1);


                //octaveOperand = t1;
                //updateOctaveOffset(octaveOperands, t1);
            }
        });
    }
    private void initOnOff() {
        tglSwitchOscillatorOnOff.setSelected(true);
        tglSwitchOscillatorOnOff.selectedProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.bypassOscillator(newValue);
        });
    }

    /**
     * Sets behaviour for waveform knob
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     */
    private void initWaveFormKnob() {
        KnobBehaviorWave behaviorKnobWave = new KnobBehaviorWave(knobWave);
        knobWave.setOnMouseDragged(behaviorKnobWave);
        behaviorKnobWave.knobValueProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.setWaveform(Waveforms.values()[newValue.intValue()]);
        });
    }

    /**
     * Sets behaviour for LFO knobs
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     */
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

    /**
     * Sets behaviour for detune knob
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     */
    private void initDetuneKnob() {
        KnobBehaviorDetune behaviorKnobDetune = new KnobBehaviorDetune(knobDetune);
        knobDetune.setOnMouseDragged(behaviorKnobDetune);
        //detuneCent.bind(behaviorKnobDetune.knobValueProperty());
        behaviorKnobDetune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.setDetuneCent(newValue.floatValue());
        } );
    }

    /**
     * Sets behaviour for gain knob
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     */
    private void initGainKnob() {
        KnobBehavior behaviorKnobGain = new KnobBehavior(knobGain);
        knobGain.setOnMouseDragged(behaviorKnobGain);
        behaviorKnobGain.setValueRotation(0.5f);
        behaviorKnobGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            oscillatorController.setGain(newValue.floatValue());
        });
    }

    //endregion
}