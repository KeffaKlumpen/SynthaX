package com.synthax.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsView implements Initializable {
    @FXML private Button btnDelete;
    @FXML private VBox presetsList;

    @FXML
    public void onActionDelete() {
        for (int i = 0; i < presetsList.getChildren().size(); i++) {
            CheckBox c = (CheckBox) presetsList.getChildren().get(i);
            if (c.isSelected()) {
                presetsList.getChildren().remove(i);
                i--;
            }
        }
    }

    private void populatePresetsBox() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        presetsList.getChildren().addAll(new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Lukeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"),
                new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Luke"),
                new CheckBox("Luke"));


    }
}
