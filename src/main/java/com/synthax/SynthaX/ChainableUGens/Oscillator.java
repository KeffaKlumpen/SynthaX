
package com.synthax.SynthaX.ChainableUGens;

import com.synthax.SynthaX.Waveforms;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Add;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *      ///// OSC 1
 *         WavePlayer osc1wp = new WavePlayer(ac, 150.f, Buffer.SINE);
 *         Gain gain1 = new Gain(ac, 1, 1f);
 *         gain1.addInput(osc1wp);
 *         // "osc1" represents the output from this first Oscillator.
 *         Add osc1 = new Add(ac, 1, gain1);
 *         //osc1.addInput(??) --- since osc1 is the first in the line, we do not add any input here
 *      ///// OSC 1 END
 *
 * @author Viktor Lenberg
 * @author Teodor Wegestål
 * @author Joel Eriksson Sinclair
 */

public class Oscillator extends ChainableUGen implements Initializable {
    @FXML private Slider sliderDetune;
    @FXML private Slider sliderGain;
    @FXML private ChoiceBox<Waveforms> waveFormChoiceBox;
    @FXML private Spinner<String> octaveSpinner;
    @FXML private RadioButton btnBypass;
    @FXML private Slider sliderAttack;
    @FXML private Slider sliderDecay;
    @FXML private Slider sliderSustain;
    @FXML private Slider sliderRelease;

    private WavePlayer wavePlayer;
    private Gain gain;
    private ADSR adsr;

    private String octaveOperand = "8'";
    private float detuneCent;

    @Override
    public void setup(){
        AudioContext ac = AudioContext.getDefaultContext();

        wavePlayer = new WavePlayer(ac, 150f, Buffer.SINE);

        adsr = new ADSR(ac);

        gain = new Gain(ac, 1, adsr.getEnvelope()); //getenvelope
        gain.addInput(wavePlayer);
        output = new Add(ac, 1, gain);
    }

    public void bypassOscillator(boolean b) {
        wavePlayer.pause(b);
    }

    /**
     * Calls methods checkOctave and checkDetune to alter the frequency before playing it
     * @param frequency of the note, provided by input method
     * @author Viktor Lenberg
     * @author Teodor Wegestål
     */
    @Override
    public void playSound(float frequency) {
        frequency = checkOctave(frequency);
        frequency = checkDetune(frequency);
        adsr.getEnvelope().addSegment(gain.getValue(), 4000);
        adsr.getEnvelope().addSegment(gain.getValue() / 2, 10000);
        wavePlayer.setFrequency(frequency);

        //adsr.envelop();
    }

    /**
     * Alters the frequency to the selected octave
     * @param frequency of the note, provided by input method
     * @return altered frequency
     * @author Viktor Lenberg
     * @author Teodor Wegestål
     */
    public float checkOctave(float frequency) {
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
    public float checkDetune(float frequency) {
        return (float)(frequency * (Math.pow(2, (detuneCent/1200))));
    }
    /**
     * Sets the selected waveform
     * @param wf waveform to be set
     * @author Viktor Lenberg
     * @author Teodor Wegestål
     */
    public void setWaveform(Waveforms wf) {
        wavePlayer.setBuffer(wf.getBuffer());
    }

    /**
     * initialize-method for the oscillator class
     * Sets values and adds listeners to GUI components
     * @author Teodor Wegestål
     * @author Viktor Lenberg
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
                gain.setGain(newGain);
                adsr.setPeakGain(newGain);
            }
        });

        btnBypass.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bypassOscillator(btnBypass.isSelected());
            }
        });

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
    }
}
