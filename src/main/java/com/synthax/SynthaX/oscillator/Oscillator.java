
package com.synthax.SynthaX.oscillator;

import com.synthax.SynthaX.Waveforms;
import com.synthax.SynthaX.controls.KnobBehavior;
import com.synthax.SynthaX.controls.KnobBehaviorDetune;
import com.synthax.SynthaX.controls.KnobBehaviorWave;
import com.synthax.controller.OscillatorManager;
import com.synthax.model.ADSRValues;
import com.synthax.model.CombineMode;
import com.synthax.model.OctaveOperands;
import com.synthax.util.MidiHelpers;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
 * @author Luke Eales
 * @author Axel Nilsson
 */

public class Oscillator implements Initializable {

    private final OscillatorVoice[] voices;
    private final int voiceCount = 16;
    private int nextVoice = 0;
    private final Gain voiceOutput;
    private Glide voiceOutputGlide;
    private UGen output;
    private OctaveOperands octaveOperand = OctaveOperands.EIGHT;
    private FloatProperty detuneCent = new SimpleFloatProperty();
    private final int[] voicePlayingMidi = new int[128];
    boolean isPlaying;
    private float playedFrequency;

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
    @FXML private Button knobLFOdepth = new Button();
    @FXML private Button knobLFOrate = new Button();
    @FXML private Spinner<OctaveOperands> octaveSpinner = new Spinner<>();

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
    public void bypassOscillator(boolean onOff) {
        for (int i = 0; i < voiceCount; i++) {
            voices[i].bypass(onOff);
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
     * @author Luke Eales
     * @author Axel Nilsson
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tglBtnCombineAdd.setSelected(true);
        segBtnCombineMode.getButtons().addListener(new ListChangeListener<ToggleButton>() {
            @Override
            public void onChanged(Change<? extends ToggleButton> change) {
                if (tglBtnCombineAdd.isSelected()) {
                    setOutputType(CombineMode.ADD);
                    System.out.println("ADD");
                } else if (tglBtnCombineMult.isSelected()) {
                    setOutputType(CombineMode.MULT);
                    System.out.println("MULT");
                } /*else {
                    setOutputType(CombineMode.SUB); INTE IMPLEMENTERAT
                }*/
            }
        });


        KnobBehavior behaviorKnobGain = new KnobBehavior(knobGain);
        knobGain.setOnMouseDragged(behaviorKnobGain);
        behaviorKnobGain.setValueZero();
        behaviorKnobGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            voiceOutputGlide.setValue(newValue.floatValue());
            System.out.println("GAIN " + newValue.floatValue());
        });

        KnobBehaviorDetune behaviorKnobDetune = new KnobBehaviorDetune(knobDetune);
        knobDetune.setOnMouseDragged(behaviorKnobDetune);
        behaviorKnobDetune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //updateFrequency();
        } );
        detuneCent.bind(behaviorKnobDetune.knobValueProperty());

        KnobBehavior behaviorKnobLFOdepth = new KnobBehavior(knobLFOdepth);
        knobLFOdepth.setOnMouseDragged(behaviorKnobLFOdepth);
        //TODO implementera LFO

        KnobBehavior behaviorKnobLFOrate = new KnobBehavior(knobLFOrate);
        knobLFOrate.setOnMouseDragged(behaviorKnobLFOrate);
        //TODO implementera LFO

        KnobBehaviorWave behaviorKnobWave = new KnobBehaviorWave(knobWave);
        knobWave.setOnMouseDragged(behaviorKnobWave);
        behaviorKnobWave.knobValueProperty().addListener((v, oldValue, newValue) -> {
            setWaveform(Waveforms.values()[newValue.intValue()]);
        });
        tglSwitchOscillatorOnOff.setSelected(true);
        tglSwitchOscillatorOnOff.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean onOff = tglSwitchOscillatorOnOff.isSelected();
                bypassOscillator(onOff);
            }
        });

        SpinnerValueFactory<OctaveOperands> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(OctaveOperands.values()));
        valueFactory.setValue(OctaveOperands.EIGHT);
        octaveSpinner.setValueFactory(valueFactory);
        octaveSpinner.valueProperty().addListener(new ChangeListener<OctaveOperands>() {
            @Override
            public void changed(ObservableValue<? extends OctaveOperands> observableValue, OctaveOperands octaveOperands, OctaveOperands t1) {
                octaveOperand = t1;
                // TODO: update frequency of waveplayer
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
        return (float)(frequency * (Math.pow(2, (detuneCent.floatValue()/1200))));
    }
}
