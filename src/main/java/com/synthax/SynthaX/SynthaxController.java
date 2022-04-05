package com.synthax.SynthaX;

import com.synthax.SynthaX.ChainableUGens.Oscillator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
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
    private Synth synth;
    private boolean playin;

    public SynthaxController() {
        synth = new Synth();
    }

    @FXML
    public void onActionAddOscillator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("oscillator-view.fxml"));
            Node oscillatorView = fxmlLoader.load();
            Oscillator oscillator = fxmlLoader.getController();
            oscillator.setup();

            synth.addToChain(oscillator);

            oscillatorChainView.getChildren().add(oscillatorView);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void onActionPlay() {
        if (!playin) {
            synth.keyPressed();
        } else {
            synth.keyReleased();
        }
        playin = !playin;
    }

    public void removeOscillator(Oscillator osc) {
        //remove oscillator from synth and from window
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.K) {
                synth.keyPressed();
            }
        });
        mainPane.setOnKeyReleased(event -> {
            synth.keyReleased();
        });
    }
}
