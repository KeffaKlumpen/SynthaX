package com.synthax.view;

import com.synthax.sample_player.controller.SamplePlayerController;
import com.synthax.view.controls.KnobBehavior;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.ToggleSwitch;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Class for Sample Player View
 * This class contains all the GUI components and their values
 * Handles events when GUI components are interacted with and forwards data
 * @author Teodor Wegestål
 * @author Viktor Lenberg
 * @author Axel Nilsson
 */
public class SamplePlayerView implements Initializable {
    // region FXML Pad Buttons (click to open/collapse)
    @FXML private Button pad0;
    @FXML private Button pad1;
    @FXML private Button pad2;
    @FXML private Button pad3;
    @FXML private Button pad4;
    @FXML private Button pad5;
    @FXML private Button pad6;
    @FXML private Button pad7;
    @FXML private Button pad8;
    //endregion FXML Pad Buttons

    //region FXML Sequencer Variables (click to open/collapse)
    @FXML private Label lblChannel1;
    @FXML private Label lblChannel2;
    @FXML private Label lblChannel3;
    @FXML private Label lblChannel4;
    @FXML private Label lblChannel5;
    @FXML private Label lblChannel6;
    @FXML private Label lblChannel7;
    @FXML private Label lblChannel8;
    @FXML private Label lblChannel9;
    @FXML private GridPane gridPane;
    @FXML private Button knobSamplePlayerRate;
    @FXML private Button btnSamplePlayerStart;
    //endregion FXML Sequencer Variables

    // region Pad Settings (click to open/collapse)
    @FXML private Button knobPadGain;
    @FXML private Button knobPadReverbSize;
    @FXML private Button knobPadReverbTone;
    @FXML private Button knobPadReverbAmount;
    @FXML private ToggleSwitch tglBypassReverb;
    @FXML private ComboBox<String> cmbAvailableSamples;

    private KnobBehavior behaviorPadGain;
    private KnobBehavior behaviorPadReverbSize;
    private KnobBehavior behaviorPadReverbTone;
    private KnobBehavior behaviorPadReverbAmount;
    // endregion Pad Settings

    @FXML private Slider sliderSamplePlayerGain;

    private SamplePlayerController samplePlayerController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        samplePlayerController = new SamplePlayerController(this);
        initPadButtons();
        initGridPane();
        initSamplePlayerGain();
        initSamplePlayerSequencer();
        initPadGain();
        initReverb();
        initAvailableSamples();
        initPadGainValues();
    }
    //region Initialize methods (click to open/collapse)
    private void initSamplePlayerSequencer() {
        KnobBehavior behaviorRate = new KnobBehavior(knobSamplePlayerRate);
        behaviorRate.setRotation(0.5f);
        knobSamplePlayerRate.setOnMouseDragged(behaviorRate);
        behaviorRate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //TODO Sample player sequencer class rate behavior
        });

        btnSamplePlayerStart.setOnMousePressed(l -> {
            //TODO start sample player sequencer
        });
    }

    private void initAvailableSamples() {
        cmbAvailableSamples.setItems(FXCollections.observableList(Arrays.stream(getAvailableSamples()).toList()));
        cmbAvailableSamples.valueProperty().addListener((observableValue, s, t1) -> samplePlayerController.setPadSample(t1));
    }

    private String[] getAvailableSamples() {
        ArrayList<String> samples = new ArrayList<>();

        File root = new File("src/main/resources/com/synthax/samples");

        File[] sampleFiles = root.listFiles();
        if (sampleFiles != null) {
            for (File file : sampleFiles) {
                String sampleName = file.getName();
                if (sampleName.endsWith(".wav")) {
                    sampleName = sampleName.substring(0, sampleName.length() - 4);
                    samples.add(sampleName);
                }
            }
        }
        return samples.toArray(new String[0]);
    }

    private void initReverb() {
        behaviorPadReverbSize = new KnobBehavior(knobPadReverbSize);
        knobPadReverbSize.setOnMouseDragged(behaviorPadReverbSize);
        behaviorPadReverbSize.knobValueProperty().addListener((v, oldValue, newValue) -> {
            samplePlayerController.setPadReverbSize(newValue.floatValue());
        });

        behaviorPadReverbTone = new KnobBehavior(knobPadReverbTone);
        knobPadReverbTone.setOnMouseDragged(behaviorPadReverbTone);
        behaviorPadReverbTone.knobValueProperty().addListener((v, oldValue, newValue) -> {
            samplePlayerController.setPadReverbTone(newValue.floatValue());
        });

        behaviorPadReverbAmount = new KnobBehavior(knobPadReverbAmount);
        knobPadReverbAmount.setOnMouseDragged(behaviorPadReverbAmount);
        behaviorPadReverbAmount.knobValueProperty().addListener((v, oldValue, newValue) -> {
            samplePlayerController.setPadReverbAmount(newValue.floatValue());
        });

        tglBypassReverb.selectedProperty().addListener((observableValue, aBoolean, t1) -> samplePlayerController.bypassPadReverb());

    }

    private void initPadGain() {
        behaviorPadGain = new KnobBehavior(knobPadGain);
        knobPadGain.setOnMouseDragged(behaviorPadGain);
        behaviorPadGain.setRotation(0.5f);
        behaviorPadGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            samplePlayerController.setPadGain(newValue.floatValue());
        });
    }

    private void initPadGainValues() {
        samplePlayerController.setAllGainValues();
    }

    private void initSamplePlayerGain() {
        sliderSamplePlayerGain.valueProperty().addListener((observableValue, number, t1) -> samplePlayerController.setMasterGain(t1.floatValue()));
    }

    private void initGridPane() {
        ToggleButton[][] toggleButtons = new ToggleButton[9][16];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 16; j++) {
                ToggleButton tb = new ToggleButton();
                tb.setPrefHeight(27);
                tb.setPrefWidth(27);
                tb.getStyleClass().add("tglSampleSeq");
                toggleButtons[i][j] = tb;
                gridPane.add(tb, j, i);
            }
        }
    }

    private void initPadButtons() {
        pad0.setOnAction(actionEvent -> {
            samplePlayerController.playPad(0);
            samplePlayerController.setCurrentPad(0);
        });
        pad1.setOnAction(actionEvent -> {
            samplePlayerController.playPad(1);
            samplePlayerController.setCurrentPad(1);
        });
        pad2.setOnAction(actionEvent -> {
            samplePlayerController.playPad(2);
            samplePlayerController.setCurrentPad(2);
        });
        pad3.setOnAction(actionEvent -> {
            samplePlayerController.playPad(3);
            samplePlayerController.setCurrentPad(3);
        });
        pad4.setOnAction(actionEvent -> {
            samplePlayerController.playPad(4);
            samplePlayerController.setCurrentPad(4);
        });
        pad5.setOnAction(actionEvent -> {
            samplePlayerController.playPad(5);
            samplePlayerController.setCurrentPad(5);
        });
        pad6.setOnAction(actionEvent -> {
            samplePlayerController.playPad(6);
            samplePlayerController.setCurrentPad(6);
        });
        pad7.setOnAction(actionEvent -> {
            samplePlayerController.playPad(7);
            samplePlayerController.setCurrentPad(7);
        });
        pad8.setOnAction(actionEvent -> {
            samplePlayerController.playPad(8);
            samplePlayerController.setCurrentPad(8);
        });
    }
    //endregion Initialize methods

    public void setSequencerLabel(String fileName, int padIndex) {
        switch (padIndex) {
            case 1 -> lblChannel1.setText("Pad 1: " + fileName);
            case 2 -> lblChannel2.setText("Pad 2: " + fileName);
            case 3 -> lblChannel3.setText("Pad 3: " + fileName);
            case 4 -> lblChannel4.setText("Pad 4: " + fileName);
            case 5 -> lblChannel5.setText("Pad 5: " + fileName);
            case 6 -> lblChannel6.setText("Pad 6: " + fileName);
            case 7 -> lblChannel7.setText("Pad 7: " + fileName);
            case 8 -> lblChannel8.setText("Pad 8: " + fileName);
            case 9 -> lblChannel9.setText("Pad 9: " + fileName);
        }
    }

    public void setValuesInPadView(String fileName, float gainValue, float reverbAmount, float reverbSize, float reverbTone, boolean reverbActive) {
        cmbAvailableSamples.getSelectionModel().select(fileName);
        behaviorPadGain.setRotation(gainValue);
        behaviorPadReverbAmount.setRotation(reverbAmount);
        behaviorPadReverbSize.setRotation(reverbSize);
        behaviorPadReverbTone.setRotation(reverbTone);
        tglBypassReverb.setSelected(reverbActive);
    }
}
