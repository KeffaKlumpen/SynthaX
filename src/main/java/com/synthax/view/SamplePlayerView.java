package com.synthax.view;

import com.synthax.MainApplication;
import com.synthax.sample_player.controller.SamplePlayerController;
import com.synthax.view.controls.KnobBehavior;
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
    private KnobBehavior behaviorRate;

    @FXML private Slider sliderSamplePlayerGain;

    @FXML private VBox vBoxPadView = new VBox();

    private SamplePlayerController samplePlayerController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPadView();
        initPadButtons();
        initGridPane();
        initSamplePlayerGain();
        initSamplePlayerSequencer();
        samplePlayerController = new SamplePlayerController(this);
    }

    private void initSamplePlayerSequencer() {
        behaviorRate = new KnobBehavior(knobSamplePlayerRate);
        behaviorRate.setRotation(0.5f);
        knobSamplePlayerRate.setOnMouseDragged(behaviorRate);
        behaviorRate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            //TODO Sample player sequencer class rate behavior
        });

        btnSamplePlayerStart.setOnMousePressed(l -> {
            //TODO start sample player sequencer
        });
    }

    private void initSamplePlayerGain() {
        sliderSamplePlayerGain.valueProperty().addListener((observableValue, number, t1) -> samplePlayerController.setMasterGain(t1.floatValue()));
    }

    private void initGridPane() {
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

    public void setSequencerLabel(String fileName, int padIndex) {
        switch (padIndex) {
            case 1 -> lblChannel1.setText("Pad 1: " + fileName);
            case 2 -> lblChannel2.setText("Pad 2: " + fileName);
            case 3 -> lblChannel3.setText("Pad 3: " + fileName);
            case 4 -> lblChannel4.setText("Pad 4: " + fileName);
            case 5 -> lblChannel5.setText("Pad 5: " + fileName);
            case 6 -> lblChannel6.setText("Pad 6: " + fileName);
            case 7 -> lblChannel7.setText("Pad 7: " + fileName);
            case 8 -> lblChannel8.setText("Pad 8: " + fileName);
            case 9 -> lblChannel9.setText("Pad 9: " + fileName);
        }
    }
}
