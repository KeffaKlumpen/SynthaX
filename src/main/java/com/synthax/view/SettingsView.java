package com.synthax.view;

import com.synthax.view.utils.Dialogs;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsView implements Initializable {
    @FXML private ToggleSwitch toggleMonophonic;
    @FXML private VBox presetsList;
    @FXML private Spinner<Integer> voiceCountSpinner;
    private SynthaxView synthaxView;


    // TODO: 2022-05-20 Only pass call to controller,
    //  then let controller create thread and call on SeqPresetLoader
    //  and when that thread is done - callBack to update GUI.
    @FXML
    public void onActionDelete() {
        int choice = Dialogs.getConfirmationYesCancel("Remove Preset", "This will remove the selected presets, are you sure?");

        if (choice == Dialogs.YES_OPTION) {
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

    private void setMonophonicState(boolean monophonic) {
        if(monophonic) {
            synthaxView.setOscVoiceCount(0);
        } else {
            synthaxView.setOscVoiceCount(voiceCountSpinner.getValue());
        }
    }

    public void populatePresetsBox(String[] presetName, SynthaxView synthaxView) {
        this.synthaxView = synthaxView;
        for (String s : presetName) {
            presetsList.getChildren().add(new CheckBox(s));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleMonophonic.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            setMonophonicState(newValue);
        });

        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 64, 8);
        voiceCountSpinner.setValueFactory(svf);
        voiceCountSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            synthaxView.setOscVoiceCount(newValue);
            if(toggleMonophonic.isSelected()) {
                toggleMonophonic.setSelected(false);
            }
        });
    }
}
