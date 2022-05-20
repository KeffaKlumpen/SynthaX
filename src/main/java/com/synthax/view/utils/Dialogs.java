package com.synthax.view.utils;

import com.synthax.MainApplication;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class Dialogs {

    public static Boolean getConfirmationBox(String title, String contentText) {
        Alert alertBox = new Alert(Alert.AlertType.CONFIRMATION);
        alertBox.getDialogPane().getStylesheets().add(MainApplication.class.getResource("skins.css").toExternalForm());
        alertBox.setTitle(title);
        alertBox.setHeaderText(contentText);
        alertBox.getDialogPane().getStyleClass().add("confirmationBox");
        Optional<ButtonType> result = alertBox.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Ok");
            return true;
        } else {
            System.out.println("Cancel");
            return false;
        }
    }

    public static String getTextInput(String title, String contentText, String promptText) {
        TextInputDialog inputDialog = new TextInputDialog(promptText);
        inputDialog.getDialogPane().getStylesheets().add(MainApplication.class.getResource("skins.css").toExternalForm());
        inputDialog.getDialogPane().getStyleClass().add("dialogPane");
        inputDialog.setContentText("Enter preset name:");
        inputDialog.setTitle(title);
        inputDialog.setHeaderText(contentText);
        Optional<String> result = inputDialog.showAndWait();
        while (result.isPresent()) {
            if (!result.get().equals("")) {
                return result.get();
            }
            result = inputDialog.showAndWait();
        }
        return null;
    }

}
