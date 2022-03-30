/*
  Author: Joel Eriksson Sinclair
  ID: ai7892
  Study program: Sys 21h
*/

package com.synthax.synthax_beads;

import com.synthax.synthax_beads.ChainableUGens.FrequencyModifier;
import com.synthax.synthax_beads.ChainableUGens.Oscillator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SynthaxController {
    @FXML
    public VBox oscillatorViews;

    private Synth synth;

    public SynthaxController() {
        synth = new Synth();
    }

    @FXML
    protected void keyPress(){
        System.out.println("Controller down");
        synth.keyPressed();
    }
    @FXML
    protected void keyRelease(){
        System.out.println("Controller up");
        synth.keyReleased();
    }

    @FXML
    public void addOscillator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("oscillator-view.fxml"));
            Node oscillatorView = fxmlLoader.load();
            Oscillator oscillator = fxmlLoader.getController();
            oscillator.setup();

            synth.addToChain(oscillator);

            oscillatorViews.getChildren().add(oscillatorView);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void addFM(){
        // FIXME: 2022-03-30 Make a fxml for FrequencyModifier.. Load stuff, etc.
        FrequencyModifier fm = new FrequencyModifier();
        fm.setup();

        synth.addToChain(fm);
    }

    public void close(){
        synth.close();
    }
}
