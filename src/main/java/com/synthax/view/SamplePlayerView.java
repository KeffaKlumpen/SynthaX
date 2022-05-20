package com.synthax.view;

import com.synthax.sample_player.controller.SamplePlayerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


import java.net.URL;
import java.util.ResourceBundle;

public class SamplePlayerView implements Initializable {

    @FXML private Button pad0;
    @FXML private Button pad1;
    @FXML private Button pad2;
    @FXML private Button pad3;
    @FXML private Button pad4;
    @FXML private Button pad5;
    @FXML private Button pad6;
    @FXML private Button pad7;
    @FXML private Button pad8;

    private SamplePlayerController samplePlayerController;

    public SamplePlayerView() {
        samplePlayerController = new SamplePlayerController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPadButtons();
    }

    private void initPadButtons() {
        pad0.setOnAction(actionEvent -> samplePlayerController.playPad(0));
        pad1.setOnAction(actionEvent -> samplePlayerController.playPad(1));
        pad2.setOnAction(actionEvent -> samplePlayerController.playPad(2));
        pad3.setOnAction(actionEvent -> samplePlayerController.playPad(3));
        pad4.setOnAction(actionEvent -> samplePlayerController.playPad(4));
        pad5.setOnAction(actionEvent -> samplePlayerController.playPad(5));
        pad6.setOnAction(actionEvent -> samplePlayerController.playPad(6));
        pad7.setOnAction(actionEvent -> samplePlayerController.playPad(7));
        pad8.setOnAction(actionEvent -> samplePlayerController.playPad(8));
    }
}
