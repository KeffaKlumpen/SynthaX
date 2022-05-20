package com.synthax.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PadView implements Initializable {

    @FXML private Button knobPadGain;

    @FXML private Button knobPadReverbSize;
    @FXML private Button knobPadReverbTone;
    @FXML private Button knobPadReverbAmount;
    @FXML private ComboBox comboBoxPadFile;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGain();
        initReverb();
        initFile();
    }

    private void initFile() {
    }

    private void initReverb() {
    }

    private void initGain() {
    }

    @FXML
    private void onActionReverbBypass(MouseEvent mouseEvent) {
    }
}
