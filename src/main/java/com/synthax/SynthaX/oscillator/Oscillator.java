
package com.synthax.SynthaX.oscillator;

import com.synthax.SynthaX.Waveforms;
import com.synthax.SynthaX.controls.KnobBehavior;
import com.synthax.SynthaX.controls.KnobBehaviorDetune;
import com.synthax.SynthaX.controls.KnobBehaviorWave;
import com.synthax.controller.OscillatorManager;
import com.synthax.model.ADSRValues;
import com.synthax.model.CombineMode;
import com.synthax.model.OctaveOperands;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Add;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.Mult;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.ToggleSwitch;

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
    @FXML private Spinner<OctaveOperands> octaveSpinner;
    @FXML private Button btnRemoveOscillator;
    @FXML private RadioButton btnBypass;
    @FXML private Slider sliderAttack;
    @FXML private Slider sliderDecay;
    @FXML private Slider sliderSustain;
    @FXML private Slider sliderRelease;

    private final OscillatorVoice[] voices;
    private final int voiceCount = 16;
    private int currentVoice = 0;
    private final Gain voiceOutput;
    private Glide voiceOutputGlide;
    private UGen output;

    private OctaveOperands octaveOperand = OctaveOperands.EIGHT;
    private float detuneCent;

    boolean isPlaying;
    private float playedFrequency;


    //NEW GUI COMPONENTS-----------------------------
    @FXML private SegmentedButton segBtnCombineMode;
    @FXML private ToggleButton tglBtnCombineAdd;
    @FXML private ToggleButton tglBtnCombineSub;
    @FXML private ToggleButton tglBtnCombineMult;
    @FXML private ToggleSwitch switchBypass;
    //@FXML private Button btnMoveUp;
    //@FXML private Button btnMoveDown;
    @FXML private Button knobGain = new Button();
    @FXML private Button knobWave = new Button();
    @FXML private Button knobDetune = new Button();
    @FXML private Button knobLFOdepth = new Button();
    @FXML private Button knobLFOrate = new Button();
    //@FXML private Spinner<String> octaveSpinner;

    //NEW GUI COMPONENTS------------------------------

    /**
     * Setup internal chain structure.
     * @author Joel Eriksson Sinclair
     */
    public Oscillator(){
        voiceOutputGlide = new Glide(AudioContext.getDefaultContext(), 0.5f, 50);
        voiceOutput = new Gain(1, voiceOutputGlide);

        voices = new OscillatorVoice[voiceCount];
        for (int i = 0; i < voiceCount; i++) {
            OscillatorVoice voice = new OscillatorVoice(Buffer.SINE);
            voiceOutput.addInput(voice.getOutput());
            voices[i] = voice;
        }

        output = new Add(1, voiceOutput); // set this to GUI value..
    }

    // FIXME: 2022-04-07 Bypassing an Mult Oscillator makes it so no sound reaches the output. (Multiplying with the 0-buffer).
    public void bypassOscillator(boolean b) {
        for (int i = 0; i < voiceCount; i++) {
            voices[i].bypass(b);
        }
    }

    /**
     * Calls methods checkOctave and checkDetune to alter the frequency before playing it
     * @param frequency of the note, provided by input method
     * @author Viktor Lenberg
     * @author Teodor Wegestål
     * @author Joel Eriksson Sinclair
     */
    public void playFrequency(float frequency) {
        playedFrequency = frequency;
        System.out.println("playing: " + currentVoice);
        isPlaying = true;

        frequency = applyOctaveOffset(frequency);   //bryta ut det här till en egen metod, då den behöver kallas på av listeners oxå
        frequency = applyDetuning(frequency);

        voices[currentVoice++].playFreq(frequency, 1f, ADSRValues.getAttackValue(), ADSRValues.getSustainValue(), ADSRValues.getDecayValue());
        currentVoice = currentVoice % voiceCount;
    }

    /**
     * @param voiceIndex
     * @author Joel Eriksson Sinclair
     */
    public void stopVoice(int voiceIndex){
        isPlaying = false;
        voices[voiceIndex].stopPlay(ADSRValues.getReleaseValue());
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
        //NEW GUI START-----------------------
        segBtnCombineMode.getButtons().addAll(tglBtnCombineAdd,tglBtnCombineSub,tglBtnCombineMult);

        KnobBehavior behaviorKnobGain = new KnobBehavior(knobGain);
        knobGain.setOnMouseDragged(behaviorKnobGain);
        //Om man vill ha en listener på knobValue:
        //behaviorKnobGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
        //            KOD HÄR EXEMPELVIS:
        //            System.out.println(newValue);
        //});
        //Om man vill binda ihop knobValue med ex en annan instansvariabel
        //behaviorKnobGain.knobValueProperty().bind(ANNAN PROPERTY AV SAMMA TYP)

        KnobBehaviorDetune behaviorKnobDetune = new KnobBehaviorDetune(knobDetune);
        knobGain.setOnMouseDragged(behaviorKnobDetune);

        KnobBehavior behaviorKnobLFOdepth = new KnobBehavior(knobLFOdepth);
        knobGain.setOnMouseDragged(behaviorKnobLFOdepth);

        KnobBehavior behaviorKnobLFOrate = new KnobBehavior(knobLFOrate);
        knobGain.setOnMouseDragged(behaviorKnobLFOrate);

        KnobBehaviorWave behaviorKnobWave = new KnobBehaviorWave(knobWave);
        knobGain.setOnMouseDragged(behaviorKnobWave);

        //NEW GUI END-----------------------------



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

        SpinnerValueFactory<OctaveOperands> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(OctaveOperands.values()));
        valueFactory.setValue(OctaveOperands.EIGHT);
        octaveSpinner.setValueFactory(valueFactory);
        octaveSpinner.valueProperty().addListener(new ChangeListener<OctaveOperands>() {
            @Override
            public void changed(ObservableValue<? extends OctaveOperands> observableValue, OctaveOperands octaveOperands, OctaveOperands t1) {
                octaveOperand = t1;
                if (isPlaying) {
                    float frequency = applyDetuning(playedFrequency);
                    System.out.println(octaveOperand);
                    frequency = applyOctaveOffset(frequency);
                    for (OscillatorVoice voice: voices) {
                        voice.getWavePlayer().setFrequency(frequency);
                    }
                }
            }
        });

        sliderDetune.setMin(-50);
        sliderDetune.setMax(50);
        sliderDetune.setValue(0);
        sliderDetune.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                detuneCent = t1.floatValue();
                if (isPlaying) {
                    float frequency = applyDetuning(playedFrequency);
                    frequency = applyOctaveOffset(frequency);
                    for (OscillatorVoice voice: voices) {
                        voice.getWavePlayer().setFrequency(frequency);
                    }
                }
            }
        });


        sliderGain.setValue(50);
        sliderGain.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                float newGain = t1.floatValue() / 100;
                voiceOutputGlide.setValue(newGain);
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
            case TWO -> {
                return frequency / OctaveOperands.TWO.getValue();
            }
            case FOUR -> {
                return frequency / OctaveOperands.FOUR.getValue();
            }
            case EIGHT -> {
                return frequency * OctaveOperands.EIGHT.getValue();
            }
            case SIXTEEN -> {
                return frequency * OctaveOperands.SIXTEEN.getValue();
            }
            case THIRTYTWO -> {
                return  frequency * OctaveOperands.THIRTYTWO.getValue();
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
