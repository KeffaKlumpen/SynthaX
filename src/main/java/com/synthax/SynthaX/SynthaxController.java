package com.synthax.SynthaX;

import com.synthax.SynthaX.ChainableUGens.Oscillator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SynthaxController implements Initializable {

    @FXML public VBox oscillatorChainView;
    @FXML private Button btnAddOscillator;
    @FXML private Button btnPlay;
    @FXML private AnchorPane mainPane = new AnchorPane();
    @FXML private Button knob = new Button();
    private final Synth synth;
    private boolean playin;
    private double rotation = 0.0;
    private double y = 0.0;


    public SynthaxController() {
        synth = new Synth();
    }

    @FXML
    public void onActionAddOscillator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("oscillator-view.fxml"));
            Node oscillatorView = fxmlLoader.load();
            Oscillator oscillator = fxmlLoader.getController();

            synth.addOscillator(oscillator);
            oscillator.getBtnRemoveOscillator().setOnAction(event -> {
                synth.removeOscillator(oscillator);
                oscillatorChainView.getChildren().remove(oscillatorView);
            });

            oscillator.getBtnMoveDown().setOnAction(event -> {
                synth.moveOscillatorDown(oscillator);
            });
            oscillator.getBtnMoveUp().setOnAction(event -> {
                synth.moveOscillatorUp(oscillator);
            });

            oscillatorChainView.getChildren().add(oscillatorView);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onActionPlay() {
        if (!playin) {
            synth.keyPressed();
        } else {
            synth.keyReleased();
        }
        playin = !playin;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.K) {
                synth.keyPressed();
            } else if (event.getCode() == KeyCode.A) {
                synth.playNote('C');
            } else if (event.getCode() == KeyCode.S) {
                synth.playNote('D');
            } else if (event.getCode() == KeyCode.D) {
                synth.playNote('E');
            }
        });
        mainPane.setOnKeyReleased(event -> {
            synth.keyReleased();
        });
        knob.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                boolean b = y - mouseEvent.getScreenY() > 0;
                y = mouseEvent.getScreenY();

                if (!b) {
                    if (rotation != 301) {
                        rotation = (rotation - 2) % 360;
                    }
                } else {
                    if (rotation != 238)
                        rotation = (rotation + 2) % 360;
                }

                if (rotation >= 300 && rotation < 315) {
                    knob.setRotate(301);
                } else if (rotation >= 315 && rotation < 340) {
                    knob.setRotate(328);
                } else if (rotation >= 340 || rotation < 8) {
                    knob.setRotate(355);
                } else if (rotation >= 8 && rotation < 34) {
                    knob.setRotate(21);
                } else if (rotation >= 34 && rotation < 62) {
                    knob.setRotate(49);
                } else if (rotation >= 62 && rotation < 88) {
                    knob.setRotate(76);
                } else if (rotation >= 88 && rotation < 115) {
                    knob.setRotate(103);
                } else if (rotation >= 115 && rotation < 143) {
                    knob.setRotate(130);
                } else if (rotation >= 143 && rotation < 160) {
                    knob.setRotate(157);
                } else if (rotation >= 160 && rotation < 196) {
                    knob.setRotate(183);
                } else if (rotation >= 196 && rotation < 223) {
                    knob.setRotate(211);
                } else  {
                    knob.setRotate(238);
                }
            }
        });
    }
}
