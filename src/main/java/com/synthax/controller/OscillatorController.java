package com.synthax.controller;

import com.synthax.model.enums.Waveforms;
import com.synthax.model.controls.KnobBehavior;
import com.synthax.model.controls.KnobBehaviorDetune;
import com.synthax.model.controls.KnobBehaviorWave;
import com.synthax.model.ADSRValues;
import com.synthax.model.enums.CombineMode;
import com.synthax.model.enums.MidiNote;
import com.synthax.model.enums.OctaveOperands;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;
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
public class OscillatorController implements Initializable {
    private final OscillatorVoice[] voices;
    private final int voiceCount = 16;
    private int nextVoice = 0;
    private final Gain voiceOutput;
    private Glide voiceOutputGlide;
    private UGen oscillatorOutput;
    private OctaveOperands octaveOperand = OctaveOperands.EIGHT;
    private FloatProperty detuneCent = new SimpleFloatProperty();
    private final int[] voicePlayingMidi = new int[128];

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

    /**
     * Setup internal chain structure.
     * @author Joel Eriksson Sinclair
     */
    public OscillatorController() {
        voiceOutputGlide = new Glide(AudioContext.getDefaultContext(), 0.5f, 50);
        voiceOutput = new Gain(1, voiceOutputGlide);

        // Instantiate voice objects and setup chain.
        voices = new OscillatorVoice[voiceCount];
        for (int i = 0; i < voiceCount; i++) {
            OscillatorVoice voice = new OscillatorVoice(Buffer.SINE);
            voiceOutput.addInput(voice.getNormalizedGain());
            voices[i] = voice;
        }

        oscillatorOutput = new Add(1, voiceOutput);
        voiceNormalizerThread();
    }

    private void voiceNormalizerThread() {
        /////// NORMALIZER THREAD lOl \\\\\\\
        Thread normalizer = new Thread(() -> {
            while (true){
                float totalGain = 0f;
                float[] currGains = new float[voiceCount];
                for (int i = 0; i < voiceCount; i++) {
                    float currGain = voices[i].getNaturalGain().getGain();
                    currGains[i] = currGain;
                    totalGain += currGain;
                }
                System.out.println("total: " + totalGain);
                if(totalGain != 0) {
                    for (int i = 0; i < voiceCount; i++) {
                        voices[i].getNormalizedGain().setGain(currGains[i] / totalGain);
                    }
                }
            }
        });
        normalizer.start();
    }

    /**
     * @param midiNote Midi-note to be played.
     * @author Joel Eriksson Sinclair
     */
    public void noteOn(MidiNote midiNote, int velocity){
        voicePlayingMidi[midiNote.ordinal()] = nextVoice; // This only allows 1 voice per note-press..
        float freq = midiNote.getFrequency();

        freq = applyOctaveOffset(freq);
        freq = applyDetuning(freq);

        voices[nextVoice].playFreq(freq, (float)(velocity / 127), ADSRValues.getAttackValue(), ADSRValues.getSustainValue(), ADSRValues.getDecayValue());

        nextVoice = ++nextVoice % voiceCount;
    }

    /**
     * @param midiNote Midi-note to be released.
     * @author Joel Eriksson Sinclair
     */
    public void noteOff(MidiNote midiNote){
        int voiceIndex = voicePlayingMidi[midiNote.ordinal()];
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
            oscillatorOutput = newOutput;
            OscillatorManager.getInstance().setupInOuts(this);
        }
    }

    /**
     * Returns the UGen with the combined output from the oscillator and any input.
     * @return Add, Mult, Division, Subtract UGen
     * @author Joel Eriksson Sinclair
     */
    public UGen getOscillatorOutput(){
        return oscillatorOutput;
    }

    /**
     * Sets a given UGen to be the UGen to be combined with the Oscillator.
     * @param input The UGen to be combined with the Oscillator
     * @author Joel Eriksson Sinclair
     */
    public void setInput(UGen input){
        oscillatorOutput.clearInputConnections();
        if(input != null){
            oscillatorOutput.addInput(input);
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
        setOutputType(CombineMode.ADD);
        System.out.println("ADD");

        tglBtnCombineAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setOutputType(CombineMode.ADD);
                System.out.println("ADD");
            }
        });

        tglBtnCombineMult.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setOutputType(CombineMode.MULT);
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
                octaveOperand = t1;
                // TODO: update frequency of waveplayer
            }
        });
    }

    /**
     * Sets behaviour for on/off toggle switch
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     */
    private void initOnOff() {
        tglSwitchOscillatorOnOff.setSelected(true);
        tglSwitchOscillatorOnOff.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean onOff = tglSwitchOscillatorOnOff.isSelected();
                bypassOscillator(onOff);
            }
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
            setWaveform(Waveforms.values()[newValue.intValue()]);
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
        //TODO implementera LFO

        KnobBehavior behaviorKnobLFOrate = new KnobBehavior(knobLFORate);
        knobLFORate.setOnMouseDragged(behaviorKnobLFOrate);
        //TODO implementera LFO
    }

    /**
     * Sets behaviour for detune knob
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     */
    private void initDetuneKnob() {
        KnobBehaviorDetune behaviorKnobDetune = new KnobBehaviorDetune(knobDetune);
        knobDetune.setOnMouseDragged(behaviorKnobDetune);
        behaviorKnobDetune.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //updateFrequency();
        } );
        detuneCent.bind(behaviorKnobDetune.knobValueProperty());
    }

    /**
     * Sets behaviour for gain knob
     * @author Teodor Wegestål
     * @author Viktor Lenberg
     */
    private void initGainKnob() {
        KnobBehavior behaviorKnobGain = new KnobBehavior(knobGain);
        knobGain.setOnMouseDragged(behaviorKnobGain);
        behaviorKnobGain.setValueZero();
        behaviorKnobGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            voiceOutputGlide.setValue(newValue.floatValue());
            System.out.println("GAIN " + newValue.floatValue());
        });
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
    private float applyOctaveOffset(float frequency) {
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
    private float applyDetuning(float frequency) {
        return (float)(frequency * (Math.pow(2, (detuneCent.floatValue()/1200))));
    }
    //endregion
}
