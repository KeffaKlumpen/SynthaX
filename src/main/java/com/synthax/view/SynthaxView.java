package com.synthax.view;

import com.synthax.model.controls.KnobBehavior;

import com.synthax.model.controls.KnobBehaviorWave;
import com.synthax.MainApplication;
import com.synthax.controller.OscillatorController;
import com.synthax.controller.SynthaxController;
import com.synthax.model.ADSRValues;
import com.synthax.model.enums.MidiNote;
import com.synthax.util.MidiHelpers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * BAJS
 * @author Axel Nilsson
 * @author Luke Eales
 */
public class SynthaxView implements Initializable {
    @FXML private VBox oscillatorChainView;
    @FXML private Button knobNoiseGain;
    @FXML private Button knobDelayFeedback;
    @FXML private Button knobDelayTime;
    @FXML private Button knobDelayRepeat;
    @FXML private Button knobReverbSize;
    @FXML private Button knobReverbDecay;
    @FXML private Button knobReverbDW;
    @FXML private Button knobLFODepth;
    @FXML private Button knobLFORate;
    @FXML private Button knobLFOWaveForm;
    @FXML private Button knobFilterCutoff;
    @FXML private Button knobFilterResonance;
    @FXML private Button knobFilterEnvelope;
    @FXML private Button btnAddOscillator;
    @FXML private AnchorPane mainPane = new AnchorPane();
    @FXML private Slider sliderAttack;
    @FXML private Slider sliderDecay;
    @FXML private Slider sliderSustain;
    @FXML private Slider sliderRelease;
    @FXML private Slider sliderMasterGain;
    @FXML private NumberAxis xAxis = new NumberAxis();
    @FXML private NumberAxis yAxis = new NumberAxis();
    @FXML private LineChart<Number, Number> lineChartADSR = new LineChart<Number, Number>(xAxis,yAxis);

    private XYChart.Data<Number, Number> point1ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point2ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point3ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point4ADSR = new XYChart.Data<>();
    private XYChart.Data<Number, Number> point5ADSR = new XYChart.Data<>();

    private final SynthaxController synthaxController;
    private final int attackMax = 3000;
    private final int decayMax = 1500;
    private final int releaseMax = 2000;

    private final Map<KeyCode, AtomicBoolean> keyStatus = Map.of(KeyCode.A, new AtomicBoolean(false),
            KeyCode.S, new AtomicBoolean(false),
            KeyCode.D, new AtomicBoolean(false),
            KeyCode.F, new AtomicBoolean(false),
            KeyCode.G, new AtomicBoolean(false),
            KeyCode.H, new AtomicBoolean(false));

    public SynthaxView() {
        synthaxController = new SynthaxController();
    }

    @FXML
    public void onActionAddOscillator() {
        try {
            URL fxmlLocation = MainApplication.class.getResource("view/Oscillator-v.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Node oscillatorView = fxmlLoader.load();
            OscillatorController oscillatorController = fxmlLoader.getController();

            synthaxController.addOscillator(oscillatorController);

            oscillatorController.getBtnRemoveOscillator().setOnAction(event -> {
                synthaxController.removeOscillator(oscillatorController);
                oscillatorChainView.getChildren().remove(oscillatorView);
            });
            oscillatorController.getBtnMoveDown().setOnAction(event -> {
                Node[] childList = oscillatorChainView.getChildren().toArray(new Node[0]);
                int oscIndex = oscillatorChainView.getChildren().indexOf(oscillatorView);

                if(oscIndex < childList.length - 1){
                    Node nextOsc = childList[oscIndex + 1];
                    childList[oscIndex + 1] = oscillatorView;
                    childList[oscIndex] = nextOsc;

                    oscillatorChainView.getChildren().setAll(childList);

                    synthaxController.moveOscillatorDown(oscillatorController);
                }
            });
            oscillatorController.getBtnMoveUp().setOnAction(event -> {
                Node[] childList = oscillatorChainView.getChildren().toArray(new Node[0]);
                int oscIndex = oscillatorChainView.getChildren().indexOf(oscillatorView);

                if(oscIndex > 0){
                    Node prevOsc = childList[oscIndex - 1];
                    childList[oscIndex - 1] = oscillatorView;
                    childList[oscIndex] = prevOsc;

                    oscillatorChainView.getChildren().setAll(childList);

                    synthaxController.moveOscillatorUp(oscillatorController);
                }
            });

            oscillatorChainView.getChildren().add(oscillatorView);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //region Knob behavior (Click to open/collapse)
        KnobBehavior bKnobNoiseGain = new KnobBehavior(knobNoiseGain);
        knobNoiseGain.setOnMouseDragged(bKnobNoiseGain);
        bKnobNoiseGain.setValueZero();
        bKnobNoiseGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here.
        });

        KnobBehavior bKnobDelayFeedback = new KnobBehavior(knobDelayFeedback);
        knobDelayFeedback.setOnMouseDragged(bKnobDelayFeedback);
        bKnobDelayFeedback.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobDelayTime = new KnobBehavior(knobDelayTime);
        knobDelayTime.setOnMouseDragged(bKnobDelayTime);
        bKnobDelayTime.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobDelayRepeat = new KnobBehavior(knobDelayRepeat);
        knobDelayRepeat.setOnMouseDragged(bKnobDelayRepeat);
        bKnobDelayRepeat.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobReverbSize = new KnobBehavior(knobReverbSize);
        knobReverbSize.setOnMouseDragged(bKnobReverbSize);
        bKnobReverbSize.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobReverbDecay = new KnobBehavior(knobReverbDecay);
        knobReverbDecay.setOnMouseDragged(bKnobReverbDecay);
        bKnobReverbDecay.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobReverbDW = new KnobBehavior(knobReverbDW);
        knobReverbDW.setOnMouseDragged(bKnobReverbDW);
        bKnobReverbDW.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        //region LFO knob setup
        KnobBehavior bKnobLFODepth = new KnobBehavior(knobLFODepth);
        knobLFODepth.setOnMouseDragged(bKnobLFODepth);
        bKnobLFODepth.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobLFORate = new KnobBehavior(knobLFORate);
        knobLFORate.setOnMouseDragged(bKnobLFORate);
        bKnobLFORate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehaviorWave bKnobLFOWaveform = new KnobBehaviorWave(knobLFOWaveForm);
        knobLFOWaveForm.setOnMouseDragged(bKnobLFOWaveform);
        bKnobLFOWaveform.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });
        //endregion

        KnobBehavior bKnobFilterCutoff = new KnobBehavior(knobFilterCutoff);
        knobFilterCutoff.setOnMouseDragged(bKnobFilterCutoff);
        bKnobFilterCutoff.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobFilterResonance = new KnobBehavior(knobFilterResonance);
        knobFilterResonance.setOnMouseDragged(bKnobFilterResonance);
        bKnobFilterResonance.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });

        KnobBehavior bKnobFilterEnvelope = new KnobBehavior(knobFilterEnvelope);
        knobFilterEnvelope.setOnMouseDragged(bKnobFilterEnvelope);
        bKnobFilterEnvelope.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //code here
        });
        //endregion

        //region KeyBoard playing (Click to open/collapse)
        mainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                // If it's a valid key, send a noteOn message.
                if(keyStatus.containsKey(keyCode)){
                    if(keyStatus.get(keyCode).compareAndSet(false, true)){
                        MidiNote note = MidiHelpers.keyCodeToMidi(keyCode);
                        System.out.println("++++" + note.name());
                        synthaxController.noteOn(note, 128);
                    }
                }
            }
        });
        mainPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if(keyStatus.containsKey(keyCode)){
                    if(keyStatus.get(keyCode).compareAndSet(true, false)){
                        MidiNote note = MidiHelpers.keyCodeToMidi(keyCode);
                        System.out.println("----" + note.name());
                        synthaxController.noteOff(note);
                    }
                }
            }
        });
        //endregion

        //region ADSR sliders (Click to open/collapse)
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
        //endregion ((Click to open/collapse)

        sliderMasterGain.valueProperty().addListener((observableValue, number, t1) -> synthaxController.setMasterGain(t1.floatValue()));
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

    /**
     * Shifting the points in ADSR linechart to reflect the slider values.
     * @author Axel Nilsson
     * @author Luke Eales
     */
    //region Methods for updating linechart (click to open/collapse)
    private void onAttackDrag() {
        point2ADSR.setXValue((sliderAttack.getValue()/attackMax)*10d);
        point3ADSR.setXValue((sliderAttack.getValue()/attackMax)*10d+(sliderDecay.getValue()/decayMax)*10d);
    }

    private void onDecayDrag() {
        point3ADSR.setXValue((sliderAttack.getValue()/attackMax)*10d+(sliderDecay.getValue()/decayMax)*10d);
    }
    private void onSustainDrag() {

        point3ADSR.setYValue(sliderSustain.getValue()*30d);
        point4ADSR.setYValue(sliderSustain.getValue()*30d);
    }
    private void onReleaseDrag() {
        point4ADSR.setXValue(40d-(sliderRelease.getValue()/releaseMax)*10d);
    }
    //endregion
}
