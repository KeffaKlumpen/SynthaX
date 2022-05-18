package com.synthax.sample_player.view;

import com.synthax.sample_player.controller.SamplePlayerController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
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
        pad0.addActionListener(l -> samplePlayerController.playPad(0));
        pad1.addActionListener(l -> samplePlayerController.playPad(1));
        pad2.addActionListener(l -> samplePlayerController.playPad(2));
        pad3.addActionListener(l -> samplePlayerController.playPad(3));
        pad4.addActionListener(l -> samplePlayerController.playPad(4));
        pad5.addActionListener(l -> samplePlayerController.playPad(5));
        pad6.addActionListener(l -> samplePlayerController.playPad(6));
        pad7.addActionListener(l -> samplePlayerController.playPad(7));
        pad8.addActionListener(l -> samplePlayerController.playPad(8));
    }
}
