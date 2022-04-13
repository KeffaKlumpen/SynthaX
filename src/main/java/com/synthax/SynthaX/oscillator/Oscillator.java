
package com.synthax.SynthaX.oscillator;

import com.synthax.SynthaX.Waveforms;
import com.synthax.controller.OscillatorManager;
import com.synthax.model.ADSRValues;
import com.synthax.model.CombineMode;
import com.synthax.util.MidiHelpers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Add;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Mult;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 * @author Joel Eriksson Sinclair
 */

public class Oscillator implements Initializable {
    @FXML private ChoiceBox<CombineMode> combineModeChoiceBox;
    @FXML private Button btnMoveDown;
    @FXML private Button btnMoveUp;
    @FXML private Slider sliderDetune;
    @FXML private Slider sliderGain;
    @FXML private ChoiceBox<Waveforms> waveFormChoiceBox;
    @FXML private Spinner<String> octaveSpinner;
    @FXML private Button btnRemoveOscillator;
    @FXML private RadioButton btnBypass;
    @FXML private Slider sliderAttack;
    @FXML private Slider sliderDecay;
    @FXML private Slider sliderSustain;
    @FXML private Slider sliderRelease;

    private final OscillatorVoice[] voices;
    private final int voiceCount = 16;
    private int nextVoice = 0;
    private final Gain voiceOutput;
    private UGen output;

    private String octaveOperand = "8'";
    private float detuneCent;

    private final int[] voicePlayingMidi = new int[128];

    /**
     * Setup internal chain structure.
     * @author Joel Eriksson Sinclair
     */
    public Oscillator(){
        voiceOutput = new Gain(1, 1f);

        voices = new OscillatorVoice[voiceCount];
        for (int i = 0; i < voiceCount; i++) {
            OscillatorVoice voice = new OscillatorVoice(Buffer.SINE);
            voiceOutput.addInput(voice.getOutput());
            voices[i] = voice;
        }

        output = new Add(1, voiceOutput);
    }

    /**
     * @param noteNumber Midi-note to be played.
     * @author Joel Eriksson Sinclair
     */
    public void noteOn(int noteNumber, int velocity){
        voicePlayingMidi[noteNumber] = nextVoice; // This only allows 1 voice per note-press..
        float freq = MidiHelpers.midiToFreq(noteNumber);

        freq = applyOctaveOffset(freq);
        freq = applyDetuning(freq);

        voices[nextVoice].playFreq(freq, velocity / 128f, ADSRValues.getAttackValue(), ADSRValues.getSustainValue(), ADSRValues.getDecayValue());

        nextVoice = ++nextVoice % voiceCount;
    }
    /**
     * @param noteNumber Midi-note to be released.
     * @author Joel Eriksson Sinclair
     */
    public void noteOff(int noteNumber){
        int voiceIndex = voicePlayingMidi[noteNumber];
        voices[voiceIndex].stopPlay(ADSRValues.getReleaseValue());
    }

    /**
     * @param voiceIndex
     * @author Joel Eriksson Sinclair
     */
    public void stopVoice(int voiceIndex){
        voices[voiceIndex].stopPlay(ADSRValues.getReleaseValue());
    }


    // FIXME: 2022-04-07 Bypassing an Mult Oscillator makes it so no sound reaches the output. (Multiplying with the 0-buffer).
    public void bypassOscillator(boolean b) {
        for (int i = 0; i < voiceCount; i++) {
            voices[i].bypass(b);
        }
    }

    /**
     * Sets the selected waveform
     * @param wf waveform to be set
     * @author Viktor Lenberg
     * @author Teodor Wegestål
     * @author Joel Eriksson Sinclair
     */
    public void setWaveform(Waveforms wf) {
        for (int i = 0; i < voiceCount; i++) {
            voices[i].setWavePlayerBuffer(wf.getBuffer());
        }
    }

    /**
     * @author Joel Eriksson Sinclair
     */
    public int getVoiceCount(){
        return voiceCount;
    }

    //region CombineMode Output
    /**
     * @author Joel Eriksson Sinclair
     */
    public void setOutputType(CombineMode combineMode){
        UGen newOutput = null;

        switch (combineMode){
            case ADD -> {
                newOutput = new Add(1, voiceOutput);
            }
            case MULT -> {
                newOutput = new Mult(1, voiceOutput);
            }
        }
        if(newOutput != null){
            output = newOutput;
            OscillatorManager.getInstance().setupInOuts(this);
        }
    }

    /**
     * Returns the UGen with the combined output from the oscillator and any input.
     * @return Add, Mult, Division, Subtract UGen
     * @author Joel Eriksson Sinclair
     */
    public UGen getOutput(){
        return output;
    }

    /**
     * Sets a given UGen to be the UGen to be combined with the Oscillator.
     * @param input The UGen to be combined with the Oscillator
     * @author Joel Eriksson Sinclair
     */
    public void setInput(UGen input){
        output.clearInputConnections();
        if(input != null){
            output.addInput(input);
        }
    }
    //endregion

    //region GUI stuff
    /**
     * initialize-method for the oscillator class
     * Sets values and adds listeners to GUI components
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     * @author Joel Eriksson Sinclair
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        waveFormChoiceBox.setItems(FXCollections.observableArrayList(Waveforms.values()));
        waveFormChoiceBox.setValue(Waveforms.SINE);
        waveFormChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setWaveform(waveFormChoiceBox.getValue());
            }
        });

        combineModeChoiceBox.setItems(FXCollections.observableArrayList(CombineMode.values()));
        combineModeChoiceBox.setValue(CombineMode.ADD);
        combineModeChoiceBox.setOnAction(event -> setOutputType(combineModeChoiceBox.getValue()));

        String[] octaves = {"2'", "4'", "8'", "16'", "32'"} ;
        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(octaves));
        valueFactory.setValue("8'");
        octaveSpinner.setValueFactory(valueFactory);
        octaveSpinner.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                octaveOperand = t1;
            }
        });

        sliderDetune.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                detuneCent = t1.floatValue();
            }
        });

        sliderGain.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                float newGain = t1.floatValue() / 100;
                voiceOutput.setGain(newGain);
            }
        });

        btnBypass.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bypassOscillator(btnBypass.isSelected());
            }
        });

        /// This is if we want per-oscillator ADSR.. Which we don't right now
        /*
        sliderAttack.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                adsr.setAttackValue(t1.floatValue());
            }
        });

        sliderDecay.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                adsr.setDecayValue(t1.floatValue());
            }
        });

        sliderSustain.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                adsr.setSustainValue(t1.floatValue());
            }
        });
         */
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

    //endregion

    //region frequency-altering-helpers
    /**
     * Alters the frequency to the selected octave
     * @param frequency of the note, provided by input method
     * @return altered frequency
     * @author Viktor Lenberg
     * @author Teodor Wegestål
     */
    public float applyOctaveOffset(float frequency) {
        switch (octaveOperand) {
            case "2'" -> {
                return frequency / 4;
            }
            case "4'" -> {
                return frequency / 2;
            }
            case "8'" -> {
                return frequency;
            }
            case "16'" -> {
                return frequency * 2;
            }
            case "32'" -> {
                return  frequency * 4;
            }
        }
        return frequency;
    }

    /**
     * Alters the frequency to the correct cent value
     * @param frequency of the note, provided by input method
     * @return the detuned frequency
     * @author Viktor Lenberg
     * @author Teodor Wegestål
     */
    public float applyDetuning(float frequency) {
        return (float)(frequency * (Math.pow(2, (detuneCent/1200))));
    }
    //endregion

    /**
     * @author Joel Eriksson Sinclair
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Oscillator{");
        sb.append("waveForm=").append(waveFormChoiceBox.getValue());
        sb.append('}');
        return sb.toString();
    }
}
