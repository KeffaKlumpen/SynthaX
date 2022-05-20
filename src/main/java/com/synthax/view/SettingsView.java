package com.synthax.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsView implements Initializable {
    @FXML private VBox presetsList;
    @FXML private Spinner<Integer> voiceCountSpinner;
    private SynthaxView synthaxView;


    // TODO: 2022-05-20 Only pass call to controller,
    //  then let controller create thread and call on SeqPresetLoader
    //  and when that thread is done - callBack to update GUI.
    @FXML
    public void onActionDelete() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        if (a.showAndWait().get() == ButtonType.OK) {
            for (int i = 0; i < presetsList.getChildren().size(); i++) {
                CheckBox c = (CheckBox) presetsList.getChildren().get(i);
                if (c.isSelected()) {
                    presetsList.getChildren().remove(i);
                    synthaxView.deletePreset(c.getText());
                    i--;
                }
            }
            synthaxView.updateSequencerPresetList();
        }
    }

    @FXML
    public void onActionChooseFile() {

    }

    @FXML
    public void onActionMonophonic() {
        
    }

    public void populatePresetsBox(String[] presetName, SynthaxView synthaxView) {
        this.synthaxView = synthaxView;
        for (String s : presetName) {
            presetsList.getChildren().add(new CheckBox(s));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }
}
