package com.synthax.synthax_beads;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("synthax-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        SynthaxController controller = fxmlLoader.getController();
        stage.setTitle("Synthax");
        stage.setScene(scene);
        stage.show();

        //controller.addOscillator();
        //controller.addOscillator(); j

        // auto close Synth when we exit window
        stage.setOnCloseRequest(event -> {
            controller.close();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}