package com.synthax.view;

import com.synthax.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsView implements Initializable {
    @FXML private VBox presetsList;
    @FXML private Spinner<Integer> voiceCountSpinner;
    private SynthaxView synthaxView;
    private Alert alertDialog = new Alert(Alert.AlertType.CONFIRMATION);

    @FXML
    public void onActionDelete() {
        if (alertDialog.showAndWait().get() == ButtonType.OK) {
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
        alertDialog.getDialogPane().getStylesheets().add(MainApplication.class.getResource("skins.css").toExternalForm());
        alertDialog.setTitle("gay");

    }
}
