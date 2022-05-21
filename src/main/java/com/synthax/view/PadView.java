package com.synthax.view;

import com.synthax.view.controls.KnobBehavior;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PadView implements Initializable {

    @FXML private Button knobPadGain;

    @FXML private Button knobPadReverbSize;
    @FXML private Button knobPadReverbTone;
    @FXML private Button knobPadReverbAmount;
    @FXML private ComboBox<String> cmbAvailableSamples;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGain();
        initReverb();
        initAvailableSamples();
    }

    private void initAvailableSamples() {
        cmbAvailableSamples.setItems(FXCollections.observableList(Arrays.stream(getAvailableSamples()).toList()));
        cmbAvailableSamples.getSelectionModel().selectFirst();
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
                    System.out.println(sampleName);
                }
            }
        }
        return samples.toArray(new String[0]);
    }

    private void initReverb() {
        KnobBehavior behaviorPadReverbSize = new KnobBehavior(knobPadReverbSize);
        knobPadReverbSize.setOnMouseDragged(behaviorPadReverbSize);
        behaviorPadReverbSize.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //TODO Set Pad Reverb size
        });

        KnobBehavior behaviorPadReverbTone = new KnobBehavior(knobPadReverbTone);
        knobPadReverbTone.setOnMouseDragged(behaviorPadReverbTone);
        behaviorPadReverbTone.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //TODO Set Pad Reverb tone
        });

        KnobBehavior behaviorPadReverbAmount = new KnobBehavior(knobPadReverbAmount);
        knobPadReverbAmount.setOnMouseDragged(behaviorPadReverbAmount);
        behaviorPadReverbAmount.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //TODO Set Pad Reverb Amount
        });

    }

    private void initGain() {
        KnobBehavior behaviorPadGain = new KnobBehavior(knobPadGain);
        knobPadGain.setOnMouseDragged(behaviorPadGain);
        behaviorPadGain.setRotation(0.5f);
        behaviorPadGain.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //TODO Set individual Pad gain
        });
    }

    @FXML
    private void onActionReverbBypass(MouseEvent mouseEvent) {
    }
}
