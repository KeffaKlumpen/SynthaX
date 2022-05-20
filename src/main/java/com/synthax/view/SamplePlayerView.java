package com.synthax.view;

import com.synthax.MainApplication;
import com.synthax.sample_player.controller.SamplePlayerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.io.IOException;
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
    @FXML private Slider sliderSamplePlayerGain;

    @FXML private VBox vBoxPadView = new VBox();

    private final SamplePlayerController samplePlayerController;

    public SamplePlayerView() {
        samplePlayerController = new SamplePlayerController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPadView();
        initPadButtons();
        initGridPane();
    }

    private void initGridPane() {
        int x = 0;
        int y = 0;
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

    private void initPadView() {
        try {
            URL fxmlLocation = MainApplication.class.getResource("view/padview.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Node padRoot = fxmlLoader.load();
            vBoxPadView.getChildren().add(padRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
